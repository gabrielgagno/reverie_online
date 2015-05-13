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
            <p class="col-lg-offset-1 col-lg-6 text-justify alert-info">
                In this study, Tasks are the jobs that has a deadline and a completion time. These are the ones expected to be automatically scheduled
                by this application. Examples of this include writing a paper or finishing an assignment before a particular deadline. See <g:link controller="jobs" action="addHabit">Habits</g:link> for comparison.
            </p>
        </div>
        <div class="row">
            <g:form controller="jobs" action="addTask" method="POST" class="form-horizontal">
                <g:render template="taskForm" />
            </g:form>
        </div>
    </div>
<g:render template="taskJs" />
</body>
</html>