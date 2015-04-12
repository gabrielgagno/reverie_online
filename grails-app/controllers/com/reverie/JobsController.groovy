package com.reverie

import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class JobsController {

    def sessionService
    def utilityService
    def schedulingService

    def newTask(){
        render(view:'newTask', model:[isSession:1])
    }

    def newHabit(){

    }

    def addTask(String jobName, String jobNotes, String deadline, int completionTimeHour, int completionTimeMinute, int minOperationDurationHour, int minOperationDurationMinute){
        utilityService.addTask(User.findById((String) session.getAttribute("id")),jobName, jobNotes, deadline, completionTimeHour, completionTimeMinute, minOperationDurationHour, minOperationDurationMinute)
        //TODO redraw
    }

    def addHabit(){

    }
}
