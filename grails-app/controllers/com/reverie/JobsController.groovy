package com.reverie

import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class JobsController {

    def sessionService
    def utilityService

    def newTask(){
        render(view:'newTask')
    }

    def newHabit(){

    }

    def addTask(String jobName, String jobNotes, String deadline, String deadlineTime, float completionTime, float minOperationDuration){
        utilityService.addTask(jobName, jobNotes, deadline, deadlineTime, completionTime, minOperationDuration)
        //TODO redraw
    }

    def addHabit(){

    }
}
