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
                        <label class="control-label col-lg-3" for="rangeEnd">Range End: </label>
                        <div class="col-lg-9">
                            <input id="rangeEnd" type="text" readOnly="readonly" required="required" name="rangeEnd" />
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="col-lg-3">
                        <div class="form-group">
                            <label class="control-label col-lg-4" for="startHour">Start Time: </label>
                            <div class="input-group">
                                <input type="text" name="startHour" id="startHour" required="required" readonly = "readonly"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3">
                        <div class="form-group">
                            <label class="control-label col-lg-4" for="endHour">End Time: </label>
                            <div class="input-group">
                                <div class="input-group">
                                    <input type="text" name="endHour" id="endHour" required="required" readonly = "readonly"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-lg-2" for="frequency">Frequency: </label>
                        <div class="col-lg-6">
                            <select name="frequency" required="required" class="form-control" id="frequency">
                                <option value="ONCE">Once</option>
                                <option value="DAILY">Daily</option>
                                <option value="WEEKLY">Weekly</option>
                                <option value="MONTHLY">Monthly</option>
                                <option value="ANNUALLY">Annually</option>
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
                        //minDate: 0,
                        datepicker: true,
                        timepicker: false,
                        format:'Y/m/d'
                    }
            ),
            j('#rangeEnd').datetimepicker(
                    {
                        //minDate: 0,
                        datepicker: true,
                        timepicker: false,
                        format: 'Y/m/d'
                    }
            ),
            j('#startHour').datetimepicker(
                    {
                        //minDate: 0,
                        datepicker: false,
                        step:30,
                        format: 'H:i'
                    }
            ),
            j('#endHour').datetimepicker(
                    {
                        //minDate: 0,
                        datepicker: false,
                        step:30,
                        format: 'H:i'
                    }
            )
    );
</script>
</body>
</html>