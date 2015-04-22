package com.reverie

import grails.transaction.Transactional
import org.joda.time.LocalDateTime

@Transactional
class SchedulerService {
    def utilityService
    def reDraw(){
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

        //then schedule subtasks of task
    }

    def fit(){

    }

    def fitToSchedule(LocalDateTime dateStart, LocalDateTime dateEnd){

    }
}
