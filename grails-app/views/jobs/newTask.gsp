<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/12/2015
  Time: 2:56 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="clientBase" />
    <title>Add New Task</title>
</head>

<body>
    <div class="container">
        <div class="row">
            <g:form controller="jobs" action="addTask" method="POST">
                <div class="col-lg-6">
                    <div class="form-group">
                        <g:field type="text" name="jobName" required="required" placeholder="Job Name" class="form-control" />
                    </div>
                    <div class="form-group">
                        <g:textArea name="jobNotes" placeholder="Job Notes" />
                    </div>
                    <div class="form-group">
                        <g:field type="date" name="deadline" required="required" placeholder="Deadline" />
                        <g:field type="text" name="deadlineTime" required="required" placeholder="Deadline Time (HH:MM) 24 Hr." />
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <g:field type="text" name="completionTime" required="required" placeholder="Est. Completion Time" />
                    </div>
                    <div class="form-group">
                        <g:field type="text" name="minOperationDuration" required="required" placeholder="Min. Subtask Duration" />
                    </div>
                    <div class="form-group">
                        <g:submitButton name="save" value="Save" class="btn btn-primary"/>
                        <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
                    </div>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>