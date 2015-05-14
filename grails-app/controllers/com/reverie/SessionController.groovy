package com.reverie

class SessionController {

    def sessionService
    def schedulerService
    def utilityService
    def index(int errNo) {
        def sessionInd
        Task[] tasks
        Habit[] habits
        SubTask[] subTasks
        int subTaskSizeRedacted
        if(session["id"]){
            sessionInd = 1
            User user = sessionService.getCurrentUser((String) session.getAttribute("id"))
            tasks = sessionService.getUserTasks(user)
            habits = sessionService.getUserHabits(user)
            subTasks = sessionService.getUserSubTasks(user)
            subTaskSizeRedacted = subTasks.length - 1
            //for(SubTask st : subTasks){
                //println(st.motherTask.jobName + " " + st.subTaskStart.toString() + " " + st.subTaskEnd.toString())
            //}
        }
        else{
            sessionInd = 0
            tasks = null
            habits = null
            subTasks = null
            subTaskSizeRedacted = 0
        }
        if(errNo>0){
            render(view:'index', model:[isSession:sessionInd, error:errNo])
        }
        else{
            def time = utilityService.createDatePointer().toString()
            render(view:'index', model:[isSession:sessionInd, tasks: tasks, habits: habits, subTasks: subTasks, len: subTaskSizeRedacted, time: time])
        }
    }

    def login(String uNameField, String uPassField){
        def user = User.findByUsernameAndPassword(uNameField, uPassField)
        if(user!=null){
            session.setAttribute("id", user.id)
            session.setAttribute("firstName", user.firstName)
            session.setAttribute("lastName", user.lastName)
            session.setAttribute("deadlineConstant", user.deadlineConstant)
            session.setAttribute("completionConstant", user.completionTimeConstant)
            redirect(action:'index')
        }
        else{
            redirect(action:'index', params: [errNo:2])
        }
    }

    def settings(){
        if(session["id"]){
            def user = sessionService.getCurrentUser((String) session.getAttribute("id"))
            render(view: 'settings', model:[isSession: 1, firstName: session.getAttribute("firstName"), lastName: session.getAttribute("lastName"), email: user.email, username: user.username, deadline: user.deadlineConstant, completion:user.completionTimeConstant, password:user.password])
        }
        else{
            redirect(action: 'index', model:[isSession: 0])
        }
    }

    def saveSettings(String firstName, String lastName, String email, String pw, int priority){
        sessionService.saveSettings((String) session["id"], firstName, lastName, email, pw, priority)
        redirect(action: 'index', model:[isSession: 1])
    }

    def logout(){
        session.invalidate()
        redirect(action: 'index', model:[isSession: 0])
    }

    def signup(){
        render(view: 'signup', model: [isSession: 0])
    }

    def signupAction(String firstName, String lastName, String email, String pw, int priority, String userName){
        sessionService.addUser(firstName, lastName, email, pw, priority, userName)
        redirect(action: 'index', model:[isSession: 0])
    }
}
