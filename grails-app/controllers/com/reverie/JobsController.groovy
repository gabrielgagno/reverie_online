package com.reverie

import org.joda.time.LocalDateTime

class JobsController {

    def sessionService
    def utilityService
    def schedulerService

    def newTask(){
        render(view:'newTask', model:[isSession:1])
    }

    def newHabit(){
        render(view:'newHabit', model:[isSession:1])
    }

    def addTask(String jobName, String jobNotes, String deadline, int completionTimeHour, int completionTimeMinute, int minOperationDurationHour, int minOperationDurationMinute){
        utilityService.addTask(sessionService.getCurrentUser((String) session.getAttribute("id")),jobName, jobNotes, deadline, completionTimeHour, completionTimeMinute, minOperationDurationHour, minOperationDurationMinute)
        utilityService.computeWeights(Task.list(), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer())
    }

    def addHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        utilityService.addHabit(sessionService.getCurrentUser((String) session.getAttribute("id")), jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
    }
}
