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
            if fit  //before deadline and fits exactly
                fit schedule
                index = 0
            else
                if end of list
                    skip to the end of next habit
                    index = 0
                else
                    index++
             */
        Task[] tasks = Task.list()
        Habit[] habits = Habit.list()
        //clear all subTasks
        SubTask.executeUpdate("delete from SubTask st where st.motherTask in (:tasks)", [tasks: Task.list()])
        int scheduled = habits.length
        int totalNum = scheduled + tasks.length
        HashMap<String, Float> minOpsArray = new HashMap<String, Float>()
        //initialize hashMaps
        for(Task t : tasks){
            minOpsArray.put(t.id, t.completionTime)
        }
        while(scheduled!=totalNum){
            //make new subtask here
            if(fit()){
                //scheduler
                scheduled++
                i = 0
            }
            else{

            }
        }
        //then schedule subtasks of task
    }

    def fit(){

    }

    def fitToSchedule(SubTask subTask, LocalDateTime dateStart){
        subTask.subTaskStart = dateStart
    }
}
