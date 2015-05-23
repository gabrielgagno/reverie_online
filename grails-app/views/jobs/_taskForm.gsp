<%@ page import="com.reverie.Task" %>
<div class="col-lg-6">
    <div class="form-group">
        <label class="control-label col-lg-3" for="jobName">Task Name: </label>
        <div class="col-lg-9">
            <g:field type="text" name="jobName" id="jobName" required="required" placeholder="Job Name" class="form-control" value="${jobName}"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-lg-3" for="jobNotes">Job Notes: </label>
        <div class="col-lg-9">
            <g:textArea name="jobNotes" placeholder="Job Notes" id="jobNotes" class="form-control" value="${jobNotes}"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-lg-3">Deadline: </label>
        <div class="col-lg-9">
            <input id="datetimepicker" type="text" readOnly="readonly" required="required" name="deadline" />
        </div>
    </div>
</div>
<div class="col-lg-6">
    <div class="form-group">
        <label class="control-label col-lg-4" for="completionTimeHour">Completion Time: </label>
        <div class="input-group">
            <g:field type="number" name="completionTimeHour" id="completionTimeHour" class="form-control input-md" min="1" placeholder="HH" required="required" value="${completionHr}"/>
            <span class="input-group-btn" style="width:0;"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-lg-offset-6">
            <g:field type="hidden" name="idContainer" value="${id}" />
            <g:submitButton name="save" value="Save" class="btn btn-primary"/>
            <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
        </div>
    </div>
</div>