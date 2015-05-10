<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 5/8/2015
  Time: 4:09 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit Habit</title>
    <meta name="layout" content="clientBase" />
</head>

<body>
<div class="container-fluid">
    <div class="row">
        <g:form controller="jobs" action="doEditHabit" method="POST" class="form-horizontal" params="${[id: id]}">
            <g:render template="habitForm" />
        </g:form>
    </div>
</div>
<g:render template="habitJs" />
</body>
</html>