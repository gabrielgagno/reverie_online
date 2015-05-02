<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/11/2015
  Time: 11:11 PM
--%>

<%@ page import="com.reverie.SubTask" contentType="text/html;charset=UTF-8" %>
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
    <g:javascript src="jquery.js" />
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

                    $('#calends').fullCalendar({
                        defaultView: 'agendaWeek',
                        header: {
                            left: 'prev, next today',
                            center: 'title',
                            right: 'month, agendaWeek, agendaDay'
                        },
                        eventLimit: true,
                        allDaySlot: false,
                        events: [
                            <g:each in="${subTasks}" status="i" var="it">
                                {
                                    id: '${it.id}',
                                    name: '${it.motherTask.jobName}',
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
    </div>
</body>
</html>