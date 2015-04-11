package com.reverie

class ScheduleSet {

    static hasMany = [schedule:Job, priorityQueue:Task, habitQueue:Habit]
    static belongsTo = [owner:User]
    static constraints = {
    }
}
