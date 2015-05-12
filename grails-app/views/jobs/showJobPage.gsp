<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 5/12/2015
  Time: 2:01 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="clientBase" />
    <title>Job Page</title>
</head>

<body>
    <div class="container-fluid">
        <div class="col-lg-offset-3 col-lg-6">
            <h4 class="h4">
                <g:if test="${isHabit==0}">Task</g:if>
                <g:else>Habit</g:else>
            </h4>
            <h3 class="h3">${jobName}</h3>
            <g:if test="${isHabit==0}">
                <p>
                    <span style="font-weight: bold;">Due Date: </span>${deadline}<br />
                    <span style="font-weight: bold;">Completion Time: </span>${completionTime} hours<br />
                </p>
            </g:if>
            <g:else>
                <p>
                    <span style="font-weight: bold;">Range Start: </span>${from}<br />
                    <span style="font-weight: bold;">Range End: </span>${to}<br />
                    <span style="font-weight: bold;">Frequency: </span>${frequency}<br />
                </p>
            </g:else>
            <h4 class="h4">Job Notes:</h4>
            <p>${jobNotes}</p>

            <g:if test="${isHabit==0}">
                <g:if test="${done==true}">
                    <p class="btn btn-default"><i class="glyphicon glyphicon-check"></i> Done</p>
                </g:if>
                <g:else>
                    <g:link controller="jobs" action="markAsDone" id="${id}" class="btn btn-success">Mark as Done</g:link>
                </g:else>
                <g:link controller="jobs" action="editTask" class="btn btn-info" id="${id}">Edit</g:link>
                <g:link controller="jobs" action="deleteTask" class="btn btn-danger" id="${id}">Delete</g:link>
            </g:if>
            <g:else>
                <g:link controller="jobs" action="editHabit" class="btn btn-info" id="${id}">Edit</g:link>
                <g:link controller="jobs" action="deleteHabit" class="btn btn-danger" id="${id}">Delete</g:link>
            </g:else>
        </div>
    </div>
</body>
</html>