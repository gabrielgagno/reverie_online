package com.reverie

import org.joda.time.LocalDateTime

class Task extends Job{
    LocalDateTime deadline
    double weight
    int numOperations
    float minOperationDuration
    boolean hardDeadline

    static hasMany = [subTaskList : SubTask]
    static belongsTo = [owner:User]
    static constraints = {
    }

    static mapping = {
        id(generator: "uuid")
        version false
    }
}
