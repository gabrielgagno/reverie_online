package com.reverie

import org.joda.time.LocalDateTime
import org.joda.time.LocalTime

class Task extends Job{
    LocalDateTime deadline
    double weight
    int numOperations
    float completionTime
    LocalTime completionLocalTime
    float minOperationDuration
    LocalTime minOpDurationLocalTime
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
