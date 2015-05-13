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
    <div class="row">
        <p class="col-lg-offset-1 col-lg-6 text-justify alert-info">
            Habits are those tasks that need to be and are necessarily fixed to a certain point of time and duration of time. The main difference is that
            habits don't have deadlines and that they could routinely repeat in a regular interval (daily, weekly, etc). Examples of this are sleeping habits,
            fixed eating schedules, meetings that cannot (and should not be) moved, and other routine activities done at a certain point of day. See <g:link controller="jobs" action="addTask">Tasks</g:link> for comparison.
        </p>
    </div>
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