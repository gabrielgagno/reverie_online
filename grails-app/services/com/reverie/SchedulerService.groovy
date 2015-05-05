package com.reverie

import grails.transaction.Transactional
import org.joda.time.LocalDateTime

@Transactional
class SchedulerService {
    def utilityService
    def reDraw(LocalDateTime datePointer, User owner){
        int i=0, j=0
        int index = 0
        /*
        while scheduled!=totalNum
            if fit //before deadline and fits exactly
                index = 0 //return to start of task list
            else
                if end of task list
                    skip to the end of next habit
                    index = 0 //return to start of task list
                else
                    index++ //next task in list
         */
        SubTask[] subHabitsTemp = utilityService.getAllSubHabits(owner)
        for(SubTask sTemp : subHabitsTemp){
            if(utilityService.overlapFinder(datePointer, sTemp)){
                datePointer = sTemp.subTaskEnd
            }
        }
        Task[] tasksTemp = Task.findAllByOwner(owner, [sort: "weight"])
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
        //clear all subTasks
        SubTask.executeUpdate("delete from SubTask st where st.motherTask in (:tasks)", [tasks: Task.findAllByOwner(owner)])
        int scheduled = habits.length
        int totalNum = scheduled + tasks.length
        HashMap<String, Float> completionArray = new HashMap<String, Float>()
        //initialize hashMaps
        for(Task t : tasks){
            completionArray.put(t.id, t.completionTime)
            println("COMPLETION ARRAY ELEMENT: " + t.id + " " + t.completionTime)
        }

        while(scheduled!=totalNum){
            //make new subtask here
            //check first if minOperationDuration >= completionTime in HashMap
            //if yes
            //  time of subtask is equal to completionTime in HashMap
            //if no
            // time of subtask is equal to minOpDuration
            int[] timeArray
            if(tasks[index].minOperationDuration >= completionArray.get(tasks[index].id)){
                //  time of subtask is equal to completionTime in HashMap
                println("GREAT" + completionArray.get(tasks[index].id))
                timeArray = utilityService.floatToHoursMins(completionArray.get(tasks[index].id))
            }
            else{
                // time of subtask is equal to minOpDuration
                println("REMAIN")
                timeArray = utilityService.floatToHoursMins(tasks[index].minOperationDuration)
            }
            //beginning of real algorithm

            if(fit(index, habits, tasks, datePointer, timeArray)){
                //subtract for real the time
                println("fitCheck")
                completionArray.put(tasks[index].id, (float) completionArray.get(tasks[index].id)-tasks[index].minOperationDuration)
                SubTask st = new SubTask()
                st.motherTask = tasks[index]
                st.subTaskStart = datePointer
                st.subTaskEnd = datePointer.plusHours(timeArray[0]).plusMinutes(timeArray[1])
                datePointer = datePointer.plusHours(timeArray[0]).plusMinutes(timeArray[1])
                subHabitsTemp = utilityService.getAllSubHabits(owner)
                for(SubTask sTemp : subHabitsTemp){
                    if(utilityService.overlapFinder(datePointer, sTemp)){
                        datePointer = sTemp.subTaskEnd
                    }
                }
                st.save(flush:true)
                println("NEW DATEPOINTER: " + datePointer.toString())
                println("COMPLETION ARRAY NOW: " + tasks[index].id + " " + completionArray.get(tasks[index].id))
                if(completionArray.get(tasks[index].id)<=0){
                    println("scheduledOne")
                    scheduled++
                }
                index = utilityService.findResetIndex(completionArray, tasks)
            }
            else{
                if(index == tasks.length){
                    println("nextHabit")
                    datePointer = utilityService.findNextHabit(habits, datePointer)
                    index = utilityService.findResetIndex(completionArray, tasks)
                }
                else{
                    println("not")
                    index = utilityService.findNextIndex(completionArray, tasks, index)
                }
                println("index: " + index)
            }
            //end of real algorithm
        }
    }

    boolean fit(int taskIndex, Habit[] habits, Task[] tasks, LocalDateTime tStart, int[] timeArray){
        SubTask[] res = []
        if(habits.size()!=0) {
            def query = SubTask.where {
                inList("motherTask", habits)
            }
            res = query.list(sort: "subTaskStart")
        }
        int i
        /*
        for(i=0;i<res.length;i++){
            //TODO this
            println(utilityService.overlapFinder(tStart, res[i]))
            if(utilityService.overlapFinder(tStart, res[i])){
                tStart = res[i].subTaskEnd
                break
            }
        }*/
        for(i=0;i<res.length;i++){
            if(tStart.isBefore(res[i].subTaskStart)){
                break;
            }
        }

        def tEnd = tStart.plusHours(timeArray[0]).plusMinutes(timeArray[1])
        println("I: " + i)
        if(i<res.length){
            println(tEnd.toString() + " " + res[i].subTaskStart.toString())
            if(tEnd.compareTo(res[i].subTaskStart)>0){
                return false
            }
        }
        return true;
    }

    def fitToSchedule(SubTask subTask, LocalDateTime dateStart, int[] timeArray){
        subTask.subTaskStart = dateStart
    }
}
