package com.reverie

import grails.transaction.Transactional
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class UtilityService {

    def addTask(String jobName, String jobNotes, String deadline, String deadlineTime, float completionTime, float minOperationDuration) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm")
        def t = new Task()
        t.owner = sessionService.getCurrentUser((String) session.getAttribute("id"))
        t.jobName = jobName
        t.jobNotes = jobNotes
        t.deadline = fmt.parseLocalDateTime(deadline + " " + deadlineTime)
        t.completionTime = completionTime
        t.minOperationDuration = minOperationDuration
        //TODO calculate
        t.save()
    }

    def addHabit(Habit h){
        h.save()
    }
}
