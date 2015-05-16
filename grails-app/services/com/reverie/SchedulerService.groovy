package com.reverie

import grails.transaction.Transactional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.Duration

@Transactional
class SchedulerService {
    def utilityService
    def reDraw(LocalDateTime datePointer, User owner){
        Random random = new Random()
        int i=0, j=0
        int index = 0
        int complTime
        int timeBeforeDeadline
        float upperRandCeil
        //move datepointer to the end of first habit encountered
        SubTask[] subHabitsTemp = utilityService.getAllSubHabits(owner)
        for(SubTask sTemp : subHabitsTemp){
            if(utilityService.overlapFinder(datePointer, sTemp)){
                datePointer = sTemp.subTaskEnd
            }
        }
        Task[] tasksTemp = Task.findAllByOwnerAndDone(owner, false, [sort: "weight"])
        def list = []
        tasksTemp.each {
            if(it.deadline.isAfter(datePointer)){
                list << it
            }
        }
        Task[] tasks = list
        for(Task t : tasks){
            println(t.jobName)
        }
        Habit[] habits = Habit.findAllByOwner(owner)
        //clear all subTasks of task
        if(Task.findAllByOwnerAndDone(owner, false).size()!=0) {
            def sts = SubTask.findAllByMotherTaskInList(Task.findAllByOwnerAndDone(owner, false))
            SubTask.deleteAll(sts)
        }
        int scheduled = 0
        int totalNum = tasks.length
        HashMap<String, Float> completionArray = new HashMap<String, Float>()
        //initialize hashMaps
        for(Task t : tasks){
            completionArray.put(t.id, t.completionTime)
            println("COMPLETION ARRAY ELEMENT: " + t.id + " " + t.completionTime)
        }
        float currDuration
        while(scheduled!=totalNum){
            int[] timeArray
            if(completionArray.get(tasks[index].id)<1){
                // time remaining in completionArray is <1
                currDuration = completionArray.get(tasks[index].id)
                timeArray = utilityService.floatToHoursMins(completionArray.get(tasks[index].id))
            }
            else{
                //opposite
                currDuration = 1.0f
                timeArray = utilityService.floatToHoursMins(1.0f)
            }
            complTime = tasks[index].completionTime
            timeBeforeDeadline = new Duration(datePointer.toDateTime(DateTimeZone.UTC), tasks[index].deadline.toDateTime(DateTimeZone.UTC)).getStandardHours()
            SubTask[] subTasks = SubTask.findAllByMotherTaskInList(Job.findAllByOwner(owner), [sort: "subTaskStart"])
            for(SubTask st : subTasks){
                println(st.motherTask.jobName)
            }
            ArrayList<LocalDateTime> freeTimes = utilityService.findFreeTimes(timeBeforeDeadline, datePointer, subTasks)
            upperRandCeil = (complTime + freeTimes.size())/2
            println("COMPLTIME: " + complTime + " TIMEBEFREDEAD: " + timeBeforeDeadline + " upperrand: " + upperRandCeil)
            while(completionArray.get(tasks[index].id)>0){
                int x = random.nextInt((int) upperRandCeil)
                println("CURR DATE POINTER: " + datePointer)
                def randomizedDatePointer = freeTimes.get(x)
                println("RAND DATE POINTER: " + randomizedDatePointer)
                println("X: " + x)
                if(fit(owner, randomizedDatePointer, timeArray)){
                    completionArray.put(tasks[index].id, (float) completionArray.get(tasks[index].id)-currDuration)
                    SubTask st = new SubTask()
                    st.motherTask = tasks[index]
                    st.subTaskStart = randomizedDatePointer
                    st.subTaskEnd = randomizedDatePointer.plusHours(timeArray[0]).plusMinutes(timeArray[1])
                    st.save(failOnError: true)
                    if(completionArray.get(tasks[index].id)<=0){
                        scheduled++
                    }
                }
            }
            index = utilityService.findResetIndex(completionArray, tasks)
            /*
            if(fit(owner, randomizedDatePointer, timeArray)){

            }
            else{
                if(index == tasks.length){

                }
                else{

                }
            }*/
        }
    }

    boolean fit(User owner, LocalDateTime tStart, int[] timeArray){
        def jobs = Job.findAllByOwner(owner)
        SubTask[] subTasks = SubTask.findAllByMotherTaskInList(jobs, [sort: "subTaskStart"])
        println("SUBTASKS")
        for(SubTask st : subTasks){
            println(st.subTaskStart.toString() + " " + st.subTaskEnd.toString())
        }
        int i
        for(i=0;i<subTasks.length;i++){
            if(tStart.compareTo(subTasks[i].subTaskStart)<=0){
                break;
            }
        }
        def tEnd = tStart.plusHours(timeArray[0]).plusMinutes(timeArray[1])
        println("TIME: " + tStart + " " + tEnd)
        println("I: " + i)
        if(i<subTasks.length){
            if(tEnd.compareTo(subTasks[i].subTaskStart)>0){
                println("HERE DIDNT FIT")
                return false
            }
        }
        println("FITSKY")
        return true
    }

    def fitToSchedule(SubTask subTask, LocalDateTime dateStart, int[] timeArray){
        subTask.subTaskStart = dateStart
    }
}
