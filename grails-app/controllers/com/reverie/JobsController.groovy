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
        println(completionTimeHour + " " + completionTimeMinute)
        utilityService.addTask(sessionService.getCurrentUser((String) session.getAttribute("id")),jobName, jobNotes, deadline, completionTimeHour, completionTimeMinute, minOperationDurationHour, minOperationDurationMinute)
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        //schedulerService.reDraw(new LocalDateTime(2015, 4, 27, 16, 0), sessionService.getCurrentUser((String) session.getAttribute("id"))) //testing
        redirect(controller: 'session', action: 'index')
    }

    def addHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        utilityService.addHabit(sessionService.getCurrentUser((String) session.getAttribute("id")), jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
        if(Task.list().size()>0) {
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        }
        redirect(controller: 'session', action: 'index')
    }

    //fetch subtask
}
