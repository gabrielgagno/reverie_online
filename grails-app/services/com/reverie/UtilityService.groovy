package com.reverie

import grails.transaction.Transactional

@Transactional
class UtilityService {

    def addTask(Task t) {
        t.save()
    }

    def addHabit(Habit h){
        h.save()
    }
}
