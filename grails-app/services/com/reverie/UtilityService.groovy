package com.reverie

import grails.transaction.Transactional
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class UtilityService {
    def sessionService
    def addTask(User owner, String jobName, String jobNotes, String deadline, float completionTimeHour, int completionTimeMinute, float minOperationDurationHour, int minOperationDurationMinute) {

        //process competionTime and minOperationDuration
        if(completionTimeMinute==30){
            completionTimeHour+=0.5
        }
        if(minOperationDurationMinute==30){
            minOperationDurationHour+=0.5
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        def t = new Task()
        t.owner = owner
        t.jobName = jobName
        t.jobNotes = jobNotes
        t.deadline = fmt.parseLocalDateTime(deadline)
        t.completionTime = completionTimeHour
        t.minOperationDuration = minOperationDurationHour
        println(t.owner)
        t.save()
    }

    def addHabit(Habit h){
        h.save()
    }
}
