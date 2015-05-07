package com.reverie

class SessionController {

    def sessionService
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
            for(SubTask st : subTasks){
                println(st.motherTask.jobName + " " + st.subTaskStart.toString() + " " + st.subTaskEnd.toString())
            }
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
            render(view:'index', model:[isSession:sessionInd, tasks: tasks, habits: habits, subTasks: subTasks, len: subTaskSizeRedacted])
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
        render(view: 'settings', model:[isSession: 1])
    }

    def saveSettings(){

    }
}
