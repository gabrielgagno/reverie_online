<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 5/8/2015
  Time: 8:18 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>My Jobs</title>
    <meta name="layout" content="clientBase" />
</head>

<body>
    <div class="container-fluid">
        <h1 class="h1">My Jobs</h1>
        <div class="row">
            <div class="col-lg-6">
                <h2 class="h2">Tasks</h2>
                <table class="table table-hover">
                    <tr>
                        <th>Task</th>
                        <th colspan="2" class="text-center">Actions</th>
                    </tr>
                    <g:each in="${tasks}">
                        <tr>
                            <td>${it.jobName}</td>
                            <td><g:link controller="jobs" action="editTask" params="${[id: it.id]}" class="btn btn-info"><i class="glyphicon glyphicon-pencil"></i></g:link></td>
                            <td><g:link controller="jobs" action="deleteTask" params="${[id: it.id]}" class="btn btn-info"><i class="glyphicon glyphicon-trash"></i></g:link></td>
                        </tr>
                    </g:each>
                </table>
            </div>
            <div class="col-lg-6">
                <h2 class="h2">Habits</h2>
                <table class="table table-hover">
                    <tr>
                        <th>Habits</th>
                        <th colspan="2">Actions</th>
                    </tr>
                    <g:each in="${habits}">
                        <tr>
                            <td>${it.jobName}</td>
                            <td><g:link controller="jobs" action="editHabit" params="${[id: it.id]}" class="btn btn-info"><i class="glyphicon glyphicon-pencil"></i></g:link></td>
                            <td><g:link controller="jobs" action="deleteHabit" params="${[id: it.id]}" class="btn btn-danger"><i class="glyphicon glyphicon-trash"></i></g:link></td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </div>
</body>
</html>