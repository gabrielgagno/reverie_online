package com.reverie

class SessionController {

    def sessionService
    def schedulerService
    def utilityService
    /**
     * leads to the index page of the application
     * @param errNo
     * @return
     */
    def index(int errNo) {
        def sessionInd
        Task[] tasks
        Habit[] habits
        SubTask[] subTasks
        int subTaskSizeRedacted
        if(session["id"]){
            sessionInd = 1
            User user = sessionService.getCurrentUser((String) session.getAttribute("id"))
            schedulerService.refresh(user)
            tasks = sessionService.getUserTasks(user)
            habits = sessionService.getUserHabits(user)
            subTasks = sessionService.getUserSubTasks(user)
            subTaskSizeRedacted = subTasks.length - 1
            def x = utilityService.reporter(sessionService.getCurrentUser((String) session.getAttribute("id")))
            flash.header = x[0]
            flash.message = x[1]
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
    /**
     * login action of the application invoked upon logging in
     * @param uNameField
     * @param uPassField
     * @return
     */
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
    /**
     * leads to the settings page
     * @return
     */
    def settings(){
        if(session["id"]){
            def user = sessionService.getCurrentUser((String) session.getAttribute("id"))
            render(view: 'settings', model:[isSession: 1, firstName: session.getAttribute("firstName"), lastName: session.getAttribute("lastName"), email: user.email, username: user.username, deadline: user.deadlineConstant, completion:user.completionTimeConstant, password:user.password])
        }
        else{
            redirect(action: 'index', model:[isSession: 0])
        }
    }
    /**
     * action invoked upon saving settings
     * @param firstName
     * @param lastName
     * @param email
     * @param pw
     * @param priority
     * @return
     */
    def saveSettings(String firstName, String lastName, String email, String pw, int priority){
        sessionService.saveSettings((String) session["id"], firstName, lastName, email, pw, priority)
        redirect(action: 'index', model:[isSession: 1])
    }
    /**
     * action invoked upon logging out
     * @return
     */
    def logout(){
        session.invalidate()
        redirect(action: 'index', model:[isSession: 0])
    }
    /**
     * renders the signup page
     * @return
     */
    def signup(){
        render(view: 'signup', model: [isSession: 0])
    }
    /**
     * invokes the signing up action
     * @param firstName
     * @param lastName
     * @param email
     * @param pw
     * @param priority
     * @param userName
     * @return
     */
    def signupAction(String firstName, String lastName, String email, String pw, int priority, String userName){
        sessionService.addUser(firstName, lastName, email, pw, priority, userName)
        redirect(action: 'index', model:[isSession: 0])
    }
}
