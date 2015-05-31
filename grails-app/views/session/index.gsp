<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/11/2015
  Time: 11:11 PM
--%>

<%@ page import="org.joda.time.LocalDateTime; com.reverie.Task; com.reverie.Habit; com.reverie.SubTask" contentType="text/html;charset=UTF-8" %>
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
    <g:if test="${isSession==0}">
        <link rel="stylesheet" type="text/css" href="<g:resource dir="css" file="cover.css"/>" />
    </g:if>
    <g:javascript src="moment.min.js" />
    <g:javascript src="fullcalendar.js" />
    <g:javascript src="jquery.classyslider.min.js" />
    <g:javascript src="randomcolor.min.js" />
    <link rel="stylesheet" type="text/css" href="<g:resource dir="css" file="jquery.classyslider.min.css" />" />
</head>

<body>
    <div class="container-fluid">
        <g:if test="${isSession==1}">
            <div class="col-lg-12">
                <p class="text-center alert-danger">
                    <g:if test="${flash.header}">${flash.header}</g:if>
                    <br />
                    <g:if test="${flash.message!=null}">${flash.message}</g:if>
                </p>
            </div>
            <g:link controller="jobs" action="reShuffle" class="btn btn-success">Reshuffle</g:link>
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
                            var x = j('#modalGuy').attr("href");
                            j('#modalGuy').attr("href", x + "/" + calEvent.id);
                            j('#fullCalModal').modal('show');
                        },
                        events: [
                            <g:each in="${subTasks}" status="i" var="it">
                                {

                                    tempId: '${it.id}',
                                    <g:if test="${it.motherTask instanceof com.reverie.Task}" >
                                    color: '${((com.reverie.Task) it.motherTask).taskColor}',
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
        </g:if>
        <g:else>
            <div class="site-wrapper">
                <div class="site-wrapper-inner">
                    <div class="cover-container">
                        <div class="inner cover">
                            <h1 class="cover-heading">Tired of Planning your Day?</h1>
                            <h3>Let us help you out.</h3>
                            <p class="lead">
                                We are Reverie, the world's pioneering automated personal task scheduler. We are the experts in our field
                                and we are at the forefront of research in automated task scheduling. We are also the first automated task scheduler
                                to go commercial. We are continuously improving, and we need your help in doing that. You can help us by allowing us
                                to help you organize your day. Together, we become partners in an endeavour like never before.
                            </p>
                            <p class="lead">
                                <g:link controller="session" action="signup" class="btn btn-lg btn-success">Join Us Now</g:link>
                                <a href="#" class="btn btn-lg btn-info">Learn More</a>
                            </p>
                        </div>
                    </div>

                </div>

            </div>
        </g:else>
    </div>
</body>
</html>