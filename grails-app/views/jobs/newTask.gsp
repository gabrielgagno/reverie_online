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
    <div class="container-fluid">
        <div class="row">
            <g:form controller="jobs" action="addTask" method="POST" class="form-horizontal">
                <g:render template="taskForm" />
            </g:form>
        </div>
    </div>
<g:render template="taskJs" />
</body>
</html>