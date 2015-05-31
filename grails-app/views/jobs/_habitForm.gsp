<div class="col-lg-6">
    <div class="form-group">
        <label class="control-label col-lg-3" for="jobName">Habit Name: </label>
        <div class="col-lg-9">
            <g:field type="text" name="jobName" id="jobName" required="required" placeholder="Habit Name" class="form-control" value="${jobName}"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-lg-3" for="jobNotes">Notes: </label>
        <div class="col-lg-9">
            <g:textArea name="jobNotes" placeholder="Notes" id="jobNotes" class="form-control" value="${jobNotes}"/>
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
                <%
                    def oLabel = ""
                    def dLabel = ""
                    def wLabel = ""
                    def mLabel = ""
                    def yLabel = ""
                    if(frequency.equals("ONCE")){
                        oLabel = " selected"
                    }
                    else if(frequency.equals("DAILY")){
                        dLabel = " selected"
                    }
                    else if(frequency.equals("WEEKLY")){
                        wLabel =  " selected"
                    }
                    else if(frequency.equals("MONTHLY")){
                        mLabel = " selected"
                    }
                    else if(frequency.equals("ANNUALLY")){
                        yLabel = " selected"
                    }
                    %>
                <option value="ONCE"${oLabel}>Once</option>
                <option value="DAILY"${dLabel}>Daily</option>
                <option value="WEEKLY"${wLabel}>Weekly</option>
                <option value="MONTHLY"${mLabel}>Monthly</option>
                <option value="ANNUALLY"${yLabel}>Annually</option>
            </select>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-lg-6">
        <div class="form-group">
            <label class="control-label col-lg-4">Weekly Frequency: </label>
            <div class="form-group">
                <g:checkBox name="wkFreq" value="7" id="sun" disabled="true" /><label for="sun" class="control-label">Sun</label>
                <g:checkBox name="wkFreq" value="1" id="mon" disabled="true" /><label for="mon" class="control-label">Mon</label>
                <g:checkBox name="wkFreq" value="2" id="tue" disabled="true" /><label for="tue" class="control-label">Tue</label>
                <g:checkBox name="wkFreq" value="3" id="wed" disabled="true" /><label for="wed" class="control-label">Wed</label>
                <g:checkBox name="wkFreq" value="4" id="thu" disabled="true" /><label for="thu" class="control-label">Thu</label>
                <g:checkBox name="wkFreq" value="5" id="fri" disabled="true" /><label for="fri" class="control-label">Fri</label>
                <g:checkBox name="wkFreq" value="6" id="sat" disabled="true" /><label for="sat" class="control-label">Sat</label>
            </div>
        </div>
        <div class="form-group">
            <div class="col-lg-offset-6">
                <g:submitButton name="save" value="Save" class="btn btn-primary"/>
                <g:link controller="session" action="index" class="btn btn-danger">Cancel</g:link>
            </div>
        </div>
    </div>
</div>