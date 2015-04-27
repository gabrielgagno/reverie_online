package com.reverie

class SessionController {

    def utilityService
    def sessionService
    def index(int errNo) {
        def sessionInd
        Task[] taskList
        SubTask[] subTaskList
        Habit[] habitList
        if(session["id"]){
            sessionInd = 1
            taskList = Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
            habitList = Habit.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
            subTaskList = utilityService.findAllOwnedSubTasks(sessionService.getCurrentUser((String) session.getAttribute("id")))
            for(Task t : taskList){
                println(t.jobName)
            }
            for(Habit t : habitList){
                println(t.jobName)
            }
            for(SubTask subTask : subTaskList){
                println("JOB NAME: " + subTask.motherTask.jobName + " " + subTask.subTaskStart.toString() + " " + subTask.subTaskEnd.toString())
            }
        }
        else{
            sessionInd = 0
            taskList = null
            habitList = null
            subTaskList = null
        }
        if(errNo>0){
            render(view:'index', model:[isSession:sessionInd, error:errNo])
        }
        else{
            render(view:'index', model:[isSession:sessionInd, tasks: taskList, habits: habitList, subTasks: subTaskList])
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

    }
}
