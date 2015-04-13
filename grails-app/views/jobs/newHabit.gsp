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
                <div class="col-lg-6">
                    <div class="form-group">
                        <label class="control-label col-lg-3" for="jobName">Habit Name: </label>
                        <div class="col-lg-9">
                            <g:field type="text" name="jobName" id="jobName" required="required" placeholder="Habit Name" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-lg-3" for="jobNotes">Notes: </label>
                        <div class="col-lg-9">
                            <g:textArea name="jobNotes" placeholder="Notes" id="jobNotes" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-lg-3" for = "rangeStart">Range Start: </label>
                        <div class="col-lg-9">
                            <input id="rangeStart" type="text" readOnly="readonly" required="required" name="rangeStart" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-lg-3" for="rangeEnd">Range Start: </label>
                        <div class="col-lg-9">
                            <input id="rangeEnd" type="text" readOnly="readonly" required="required" name="rangeEnd" />
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <label class="control-label col-lg-4" for="completionTimeHour">Completion Time: </label>
                        <div class="input-group">
                            <g:field type="number" name="completionTimeHour" id="completionTimeHour" class="form-control input-md" min="0" placeholder="HH" required="required"/>
                            <span class="input-group-btn" style="width:0px;"></span>
                            <select name="completionTimeMinute" class="form-control input-md" style="margin-left:-2px" required="required">
                                <option value="00">00</option>
                                <option value="30">30</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-lg-4" for="minOperationDurationHour">Minimum Division: </label>
                        <div class="input-group">
                            <g:field type="number" name="completionTimeHour" id="minOperationDurationHour" class="form-control input-md" min="0" placeholder="HH" required="required"/>
                            <span class="input-group-btn" style="width:0px;"></span>
                            <select name="minOperationDurationMinute" class="form-control input-md" style="margin-left:-2px" required="required">
                                <option value="00">00</option>
                                <option value="30">30</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-offset-6">
                            <g:submitButton name="save" value="Save" class="btn btn-primary"/>
                            <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
                        </div>
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
            j('#rangeStart').datetimepicker(
                    {
                        inline:true,
                        minDate: 0,
                        step:30
                    }
            ),
            j('#rangeEnd').datetimepicker(
                    {
                        inline:true,
                        minDate: 0,
                        step:30
                    }
            )
    );
</script>
</body>
</html>