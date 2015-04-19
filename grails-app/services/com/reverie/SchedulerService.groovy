package com.reverie

import grails.transaction.Transactional

@Transactional
class SchedulerService {

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

    }

    def fit(){

    }

    def fitToSchedule(){

    }
}
