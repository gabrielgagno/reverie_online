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
        if(session.getAttribute("id")) {
            render(view: 'newTask', model: [id: "", isSession: 1, jobName: "", jobNotes: "", deadline: "", completionHr: 0, completionMin: 0])
        }
        else{
            redirect(controller: 'session', action: 'index')
        }
    }

    def newHabit(){
        if(session.getAttribute("id")) {
            render(view: 'newHabit', model: [isSession: 1])
        }
        else{
            redirect(controller: 'session', action: 'index')
        }
    }

    def addTask(String jobName, String jobNotes, String deadline, int completionTimeHour){
        //deadline: YYYY/MM/DD HH:MM
        if(session.getAttribute("id")){
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
        }
        redirect(controller: 'session', action: 'index')
    }

    def addHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        if(session.getAttribute("id")){
            utilityService.addHabit(sessionService.getCurrentUser((String) session.getAttribute("id")), jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
            if(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))).size()>0) {
                utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
                schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
            }
        }
        redirect(controller: 'session', action: 'index')
    }

    def jobsList(){
        if(session.getAttribute("id")){
            def tasks = Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
            def habits = Habit.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id")))
            render(view: 'jobsList', model:[isSession: 1, tasks: tasks, habits:habits])
        }
        else{
            redirect(controller: 'session', action: 'index')
        }
    }

    def editTask(String id){
        if(session.getAttribute("id")){
            def task = Task.findById(id)
            def cArr = utilityService.floatToHoursMins(task.completionTime)
            DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
            [id: task.id, isSession:1, jobName: task.jobName, jobNotes: task.jobNotes, deadline: fmt.print(task.deadline), completionHr:cArr[0], completionMin:cArr[1]]
        }
        else{
            redirect(controller: 'session', action: 'index')
        }
    }

    def doEditTask(String idContainer, String jobName, String jobNotes, String deadline, int completionTimeHour){
        //delete subtasks
        if(session.getAttribute("id")){
            def subTasks = SubTask.findAllByMotherTask(Task.findById(idContainer))
            SubTask.deleteAll(subTasks)
            utilityService.editTask(idContainer, jobName, jobNotes, deadline, completionTimeHour)
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
            //schedulerService.reDraw(new LocalDateTime(2015, 4, 27, 16, 0), sessionService.getCurrentUser((String) session.getAttribute("id"))) //testing
            jobsList()
        }
        else{
            redirect(controller: 'session', action: 'index')
        }

    }

    def deleteTask(String id){
        if(session.getAttribute("id")){
            def task = Task.findById(id)
            def subtasks = SubTask.findAllByMotherTask(task)
            SubTask.deleteAll(subtasks)
            task.delete()
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
            jobsList()
        }
        else{
            redirect(controller: 'session', action: 'index')
        }

    }

    def editHabit(String id){
        if(session.getAttribute("id")){
            def habit = Habit.findById(id)
            println(habit.start.toString())
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm")
            [id: habit.id, isSession: 1, jobName: habit.jobName, jobNotes: habit.jobNotes, rangeStart: habit.rangeStart.toString(), rangeEnd: habit.rangeEnd.toString(), start: timeFormatter.print(habit.start), end: timeFormatter.print(habit.end), frequency: habit.frequency]
        }
        else{
            redirect(controller: 'session', action: 'index')
        }
    }

    def doEditHabit(String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        if(session.getAttribute("id")){
            def subTasks = SubTask.findAllByMotherTask(Habit.findById((String) params.id))
            SubTask.deleteAll(subTasks)
            utilityService.editHabit((String) params.id, jobName, jobNotes, rangeStart, rangeEnd, startHour, endHour, frequency)
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
            jobsList()
        }
        else{
            redirect(controller: 'session', action: 'index')
        }

    }

    def deleteHabit(String id){
        if(session.getAttribute("id")){
            def habit = Habit.findById(id)
            def subtasks = SubTask.findAllByMotherTask(habit)
            SubTask.deleteAll(subtasks)
            habit.delete()
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
            jobsList()
        }
        else{
            redirect(controller: 'session', action: 'index')
        }

    }

    def showJobPage(String id){
        if(session.getAttribute("id")){
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
        else{
            redirect(controller: 'session', action: 'index')
        }

    }

    def markAsDone(String id){
        if(session.getAttribute("id")){
            def t = Task.findById(id)
            t.done = true
            t.save(failOnError: true)
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        }
        redirect(controller: 'session', action: 'index')
    }

    def reShuffle(){
        if(session.getAttribute("id")){
            utilityService.computeWeights(Task.findAllByOwner(sessionService.getCurrentUser((String) session.getAttribute("id"))), (int) session.getAttribute("deadlineConstant"), (int) session.getAttribute("completionConstant"))
            schedulerService.reDraw(utilityService.createDatePointer(), sessionService.getCurrentUser((String) session.getAttribute("id")))
        }
        redirect(controller: 'session', action: 'index')
    }
}
