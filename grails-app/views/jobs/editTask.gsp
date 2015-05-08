<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 5/8/2015
  Time: 4:08 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit Task</title>
    <meta name="layout" content="clientBase" />
</head>

<body>
<div class="container-fluid">
    <div class="row">
        <g:form controller="jobs" action="doEditTask" method="POST" class="form-horizontal">
            <g:render template="taskForm" />
        </g:form>
    </div>
</div>
<g:render template="taskJs" />
</body>
</html>