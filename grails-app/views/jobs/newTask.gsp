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
                        <g:field type="text" name="jobName" required="required" placeholder="Job Name" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <g:textArea name="jobNotes" placeholder="Job Notes" class="form-control" />
                    </div>
                        <input id="datetimepicker" type="text" >
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <g:field type="text" name="completionTime" required="required" placeholder="Est. Completion Time"  class="form-control" />
                    </div>
                    <div class="form-group">
                        <g:field type="text" name="minOperationDuration" required="required" placeholder="Min. Subtask Duration" class="form-control" />
                    </div>
                    <div class="form-group">
                        <g:submitButton name="save" value="Save" class="btn btn-primary"/>
                        <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
                    </div>
                </div>
            </g:form>
        </div>
    </div>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery.datetimepicker.css')}" />
<g:javascript src="jquery.js" />
<g:javascript src="jquery.datetimepicker.js" />
<script type="text/javascript">
    var j = jQuery.noConflict();
    j('document').ready(
            j('#datetimepicker').datetimepicker()
    );
</script>
</body>
</html>