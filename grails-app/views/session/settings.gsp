<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/16/2015
  Time: 11:53 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Settings - Reverie Online</title>
    <meta name="layout" content="clientBase" />
</head>

<body>
    <div class="container-fluid">
        <g:form method="POST" controller="session" action="saveSettings" class="form-horizontal">
            <div class="form-group">
                <label for="firstName" class="col-lg-push-2 col-lg-3">First Name: </label>
                <div class="col-lg-6">
                    <g:textField name="firstName" id="firstName" placeholder="First Name" required="required" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="lastName" class="col-lg-push-2 col-lg-3">Last Name: </label>
                <div class="col-lg-6">
                    <g:textField name="lastName" id="lastName" placeholder="Last Name" required="required" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="Email" class="col-lg-push-2 col-lg-3">E-mail: </label>
                <div class="col-lg-6">
                    <g:field type="email" name="email" id="email" placeholder="E-mail" required="required" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="userName" class="col-lg-push-2 col-lg-3">Username: </label>
                <div class="col-lg-6">
                    <g:textField name="userName" id="userName" placeholder="Username" required="required" class="form-control" readonly="readonly"/>
                </div>
            </div>
            <div class="form-group">
                <label for="pw" class="col-lg-push-2 col-lg-3">Username: </label>
                <div class="col-lg-6">
                    <g:field type="password" name="pw" id="pw" placeholder="Password" required="required" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-push-2 col-lg-3">Priority: </label>
                <div class="col-lg-6">
                    <g:radio name="priority" value="1" class="radio-button" id="deadlineRadio"/> <label class="col-lg-push-2" for="deadlineRadio">Deadline</label>
                    <g:radio name="priority" value="2" class="radio-button" id="completionTimeRadio"/> <label class="col-lg-push-2" for="completionTimeRadio">Completion Time</label>
                </div>
            </div>
            <div class="form-group">
                <div class="col-lg-push-2 col-lg-9">
                    <g:submitButton name="submit" value="Save Changes" class="btn btn-primary" />
                    <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
                </div>
            </div>
        </g:form>
    </div>
</body>
</html>