package com.reverie

class SessionController {

    def utilityService
    def sessionService
    def index(int errNo) {
        def sessionInd
        if(session["id"]){
            sessionInd = 1
        }
        else{
            sessionInd = 0
        }
        if(errNo>0){
            render(view:'index', model:[isSession:sessionInd, error:errNo])
        }
        else{
            render(view:'index', model:[isSession:sessionInd])
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
