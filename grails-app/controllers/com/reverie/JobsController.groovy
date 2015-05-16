package com.reverie

import grails.plugin.jodatime.binding.DateTimeConverter
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Hours
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class JobsController {

    def sessionService
    def utilityService
    def schedulerService

    def newTask(){
        render(view:'newTask', model:[id: "", isSession:1, jobName: "", jobNotes: "", deadline: "", completionHr:0, completionMin:0])
    }

    def newHabit(){
        render(view:'newHabit', model:[isSession:1])
    }

    def addTask(String jobName, String jobNotes, String deadline, int completionTimeHour){
        //deadline: YYYY/MM/DD HH:MM
        def datePtr = utilityService.createDatePointer()
        println(datePtr.toString())
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        def dLine = fmt.parseLocalDateTime(deadline)
        def timeBeforeDeadline = new Duration(datePtr.toDateTime(DateTimeZone.UTC), dLine.toDateTime(DateTimeZone.UTC)).getStandardHours()
        SubTask[] x = SubTask.findAllByMotherTaskInList(Job.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), [sort: "subTaskStart"])
        def arr = utilityService.findFreeTimes((int) timeBeforeDeadline, datePtr, x)
        int freeTimes = arr.size()
        println(timeBeforeDeadline)
        println(completionTimeHour)
        println("FREETIMES IN JOBSCONTROOLER: " + freeTimes)
        if(freeTimes<completionTimeHour){
            flash.message = "Deadline is too close. Please adjust some of your other tasks or habits."
        }
        else{
            utilityService.addTask(sessionService.getCurrentUser((String) session.getAttribute("id")),jobName, jobNotes, deadline, completionTimeHour)
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        }
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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        [id: task.id, isSession:1, jobName: task.jobName, jobNotes: task.jobNotes, deadline: fmt.print(task.deadline), completionHr:cArr[0], completionMin:cArr[1]]
    }

    def doEditTask(String idContainer, String jobName, String jobNotes, String deadline, int completionTimeHour){
        //delete subtasks
        def subTasks = SubTask.findAllByMotherTask(Task.findById(idContainer))
        SubTask.deleteAll(subTasks)
        utilityService.editTask(idContainer, jobName, jobNotes, deadline, completionTimeHour)
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

    def showJobPage(String id){
        Job job = Job.findById(id)
        if(job instanceof Task){
            Task t = Task.findById(id)
            DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm")
            [isSession: 1, id: t.id, isHabit: 0, jobName: t.jobName, jobNotes: t.jobNotes, deadline: dateFmt.print(t.deadline), completionTime: t.completionTime, done: t.done]
        }
        else{
            DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd")
            DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm")
            Habit h = Habit.findById(id)
            [isSession: 1, id: h.id, isHabit: 1, jobName: h.jobName, jobNotes: h.jobNotes, from: dateFmt.print(h.rangeStart), to: dateFmt.print(h.rangeEnd), frequency: h.frequency]
        }
    }

    def markAsDone(String id){
        def t = Task.findById(id)
        t.done = true
        t.save(failOnError: true)
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        redirect(controller: 'session', action: 'index')
    }

    def reShuffle(){
        utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
        schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        redirect(controller: 'session', action: 'index')
    }
}
