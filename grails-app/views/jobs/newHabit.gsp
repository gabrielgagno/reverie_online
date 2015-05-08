<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/12/2015
  Time: 10:52 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="clientBase" />
    <title>Add New Habit</title>
</head>

<body>
    <div class="container-fluid">
        <div class="row">
            <g:form controller="jobs" action="addHabit" method="POST" class="form-horizontal">
                <g:render template="habitForm" />
            </g:form>
        </div>
    </div>
    <g:render template="habitJs" />
</body>
</html>