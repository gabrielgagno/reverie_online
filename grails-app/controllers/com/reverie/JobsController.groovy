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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm")
        def t = new Task()
        t.owner = sessionService.getCurrentUser((String) session.getAttribute("id"))
        t.jobName = jobName
        t.jobNotes = jobNotes
        t.deadline = fmt.parseLocalDateTime(deadline + " " + deadlineTime)
        t.completionTime = completionTime
        t.minOperationDuration = minOperationDuration
        t.save()
        //TODO redraw
    }

    def addHabit(){

    }
}
