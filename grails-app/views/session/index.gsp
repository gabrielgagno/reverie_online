<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/11/2015
  Time: 11:11 PM
--%>

<%@ page import="com.reverie.Task; com.reverie.Habit; com.reverie.SubTask" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="clientBase" />
    <title>
        <g:if test="${isSession==1}">
            ${session.getAttribute("firstName")} ${session.getAttribute("lastName")} - Reverie Online
        </g:if>
        <g:else>
            Reverie Online - The Automated Task Scheduler
        </g:else>
    </title>
    <link rel="stylesheet" type="text/css" href="<g:resource dir="css" file="fullcalendar.css"/>" />
    <g:javascript src="moment.min.js" />
    <g:javascript src="fullcalendar.js" />
</head>

<body>
    <div class="container-fluid">
        <g:if test="${isSession==1}">
            <div id="calends" class="col-lg-offset-2 col-lg-8">

            </div>
            <script type="application/javascript">
                var j = jQuery.noConflict();
                j(document).ready(function() {

                    // page is now ready, initialize the calendar...

                    j('#calends').fullCalendar({
                        defaultView: 'agendaWeek',
                        header: {
                            left: 'prev, next today',
                            center: 'title',
                            right: 'month, agendaWeek, agendaDay'
                        },
                        eventLimit: true,
                        allDaySlot: false,
                        eventClick: function(calEvent, jsEvent, view){
                            j('#modalType').html(calEvent.eventType)
                            j('#modalTitle').html(calEvent.title);
                            if(calEvent.eventDL!=null){
                                j('#deadline').html("Due on: " + calEvent.eventDL);
                                j('#duration').html("Duration: " + calEvent.dur + " hours");
                            }
                            else{
                                j("#duration").html("Frequency: " + calEvent.freq);
                            }
                            j('#modalBody').html(calEvent.description);
                            j('#modalGuy').attr("href", j('#modalGuy').attr("href") + "/" + calEvent.id);
                            j('#fullCalModal').modal('show');
                        },
                        events: [
                            <g:each in="${tasks}">
                                {
                                    //deadlines
                                    id: '${it.id}',
                                    title: 'Deadline for ${it.jobName}',
                                    description: '${it.jobNotes}',
                                    eventDL: '${((com.reverie.Task) it).deadline}',
                                    start: '${it.deadline}',
                                    end: '${it.deadline}',
                                    color: '#670D0D'
                                },
                            </g:each>
                            <g:each in="${subTasks}" status="i" var="it">
                                {
                                    tempId: '${it.id}',
                                    <g:if test="${it.motherTask instanceof com.reverie.Task}" >
                                    color: '#670D0D',
                                    eventType: 'Task',
                                    dur: '${((com.reverie.Task) it.motherTask).completionTime}',
                                    eventDL: '${((com.reverie.Task) it.motherTask).deadline}',
                                    </g:if>
                                    <g:elseif test="${it.motherTask instanceof com.reverie.Habit}">
                                    eventType: 'Habit',
                                    freq: '${((com.reverie.Habit) it.motherTask).frequency.toLowerCase()}',
                                    </g:elseif>
                                    id: '${it.motherTask.id}',
                                    title: '${it.motherTask.jobName}',
                                    description: '${it.motherTask.jobNotes}',
                                    start: '${it.subTaskStart.toString()}',
                                    end: '${it.subTaskEnd.toString()}'
                                }
                                <g:if test="${i<len}">
                                    ,
                                </g:if>
                            </g:each>
                        ]
                    })

                });
            </script>
        </g:if>
        <div id="fullCalModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span> <span class="sr-only">close</span></button>
                        <h4 id="modalType"></h4>
                        <h3 id="modalTitle" class="modal-title"></h3>
                        <h4 id="deadline"></h4>
                    </div>
                    <div id="modalBody" class="modal-body"></div>
                    <div id="duration" class="modal-body"></div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <g:link controller="jobs" action="showJobPage" elementId="modalGuy" class="btn btn-primary">Go to Job</g:link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>