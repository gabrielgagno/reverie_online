package com.reverie

import grails.plugin.jodatime.binding.DateTimeConverter
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class JobsController {

    def sessionService
    def utilityService
    def schedulerService

    def newTask(){
        render(view:'newTask', model:[id: "", isSession:1, jobName: "", jobNotes: "", deadline: "", completionHr:0, completionMin:0, minDivHr: 0, minDivMin:0])
    }

    def newHabit(){
        render(view:'newHabit', model:[isSession:1])
    }

    def addTask(String jobName, String jobNotes, String deadline, int completionTimeHour, int completionTimeMinute, int minOperationDurationHour, int minOperationDurationMinute){
        println(completionTimeHour + " " + completionTimeMinute)
        //deadline: YYYY/MM/DD HH:MM
        utilityService.addTask(sessionService.getCurrentUser((String) session.getAttribute("id")),jobName, jobNotes, deadline, completionTimeHour, completionTimeMinute, minOperationDurationHour, minOperationDurationMinute)
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        //schedulerService.reDraw(new LocalDateTime(2015, 4, 27, 16, 0), sessionService.getCurrentUser((String) session.getAttribute("id"))) //testing
        redirect(controller: 'session', action: 'index')
    }

    def addHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        utilityService.addHabit(sessionService.getCurrentUser((String) session.getAttribute("id")), jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
        if(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))).size()>0) {
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        }
        redirect(controller: 'session', action: 'index')
    }

    def jobsList(){
        def tasks = Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
        def habits = Habit.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
        render(view: 'jobsList', model:[isSession: 1, tasks: tasks, habits:habits])
    }

    def editTask(String id){
        def task = Task.findById(id)
        def cArr = utilityService.floatToHoursMins(task.completionTime)
        def mDArr = utilityService.floatToHoursMins(task.minOperationDuration)
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        [id: task.id, isSession:1, jobName: task.jobName, jobNotes: task.jobNotes, deadline: fmt.print(task.deadline), completionHr:cArr[0], completionMin:cArr[1], minDivHr: mDArr[0], minDivMin:mDArr[1]]
    }

    def doEditTask(String idContainer, String jobName, String jobNotes, String deadline, int completionTimeHour, int completionTimeMinute, int minOperationDurationHour, int minOperationDurationMinute){
        //delete subtasks
        def subTasks = SubTask.findAllByMotherTask(Task.findById(idContainer))
        SubTask.deleteAll(subTasks)
        utilityService.editTask(idContainer, jobName, jobNotes, deadline, completionTimeHour, completionTimeMinute, minOperationDurationHour, minOperationDurationMinute)
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        //schedulerService.reDraw(new LocalDateTime(2015, 4, 27, 16, 0), sessionService.getCurrentUser((String) session.getAttribute("id"))) //testing
        jobsList()
        //redirect(controller: 'session', action: 'index')
    }

    def deleteTask(String id){
        def task = Task.findById(id)
        def subtasks = SubTask.findAllByMotherTask(task)
        SubTask.deleteAll(subtasks)
        task.delete()
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        jobsList()
    }

    def editHabit(String id){
        def habit = Habit.findById(id)
        println(habit.start.toString())
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm")
        [id: habit.id, isSession: 1, jobName: habit.jobName, jobNotes: habit.jobNotes, rangeStart: habit.rangeStart.toString(), rangeEnd: habit.rangeEnd.toString(), start: timeFormatter.print(habit.start), end: timeFormatter.print(habit.end), frequency: habit.frequency]
    }

    def doEditHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        def subTasks = SubTask.findAllByMotherTask(Habit.findById((String) params.id))
        SubTask.deleteAll(subTasks)
        utilityService.editHabit((String) params.id, jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        jobsList()
    }

    def deleteHabit(String id){
        def habit = Habit.findById(id)
        def subtasks = SubTask.findAllByMotherTask(habit)
        SubTask.deleteAll(subtasks)
        habit.delete()
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        jobsList()
    }
    //fetch subtask
}
