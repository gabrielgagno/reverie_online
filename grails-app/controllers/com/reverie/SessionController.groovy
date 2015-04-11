package com.reverie

class SessionController {

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
            redirect(action:'index')
        }
        else{
            redirect(action:'index', params: [errNo:2])
        }
    }
}
