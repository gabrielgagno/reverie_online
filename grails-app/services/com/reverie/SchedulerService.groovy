package com.reverie

import grails.transaction.Transactional
import org.joda.time.LocalDateTime

@Transactional
class SchedulerService {
    def utilityService
    def reDraw(LocalDateTime datePointer){
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
        Task[] tasks = Task.list()
        Habit[] habits = Habit.list()
        //clear all subTasks
        SubTask.executeUpdate("delete from SubTask st where st.motherTask in (:tasks)", [tasks: Task.list()])
        int scheduled = habits.length
        int totalNum = scheduled + tasks.length
        HashMap<String, Float> completionArray = new HashMap<String, Float>()
        //initialize hashMaps
        for(Task t : tasks){
            minOpsArray.put(t.id, t.completionTime)
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
                timeArray = utilityService.floatToHoursMins(completionArray.get(tasks[index].id))
            }
            else{
                // time of subtask is equal to minOpDuration
                timeArray = utilityService.floatToHoursMins(tasks[index].minOperationDuration)
            }

            //beginning of real algorithm
            if(fit(index, habits, tasks, datePointer, timeArray)){
                //subtract for real the time
                completionArray.put(tasks[index].id, (float) completionArray.get(tasks[index].id)-tasks[index].minOperationDuration)
                SubTask st = new SubTask()
                st.motherTask = tasks[index]
                st.subTaskStart = datePointer
                st.subTaskEnd = datePointer.plusHours(timeArray[0]).plusMinutes(timeArray[1])
                st.save(flush:true)
                if(completionArray.get(tasks[index])>=0){
                    scheduled++
                }
                index = scheduled
            }
            else{
                if(index == tasks.length){
                    datePointer = utilityService.findNextHabit(habits, datePointer)
                    index = scheduled
                }
                else{
                    index++
                }
            }
            //end of real algorithm
        }
        //then schedule subtasks of task
    }

    boolean fit(int taskIndex, Habit[] habits, Task[] tasks, LocalDateTime tStart, int[] timeArray){
        LocalDateTime tDead = tasks[taskIndex].deadline
        LocalDateTime tEnd = tStart.plusHours(timeArray[0]).plusMinutes(timeArray[1])
        int habitSize = habits.length
        for(int i=0;i<habitSize;i++){
            if(habits[i].start.isAfter(tStart.toLocalTime())){
                boolean isOverlap = false;
                isOverlap = habits[i].start.isAfter(tStart.toLocalTime()) && habits[i].end.isBefore(tEnd.toLocalTime())
                if(isOverlap){
                    if(habits[i].frequency.equals("ONCE") || habits[i].frequency.equals("DAILY")){
                        return false
                    }
                }
                else{
                    SubTask[] subHabits = SubTask.findAllWhere(motherTask: habits[i])
                    if(utilityService.habitSameDay(subHabits, tStart)){
                        return false
                    }
                }
            }
        }
        return true;
    }

    def fitToSchedule(SubTask subTask, LocalDateTime dateStart, int[] timeArray){
        subTask.subTaskStart = dateStart
    }
}
