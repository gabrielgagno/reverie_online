package com.reverie

import org.joda.time.LocalDateTime
import org.joda.time.LocalTime

class Task extends Job{
    LocalDateTime deadline
    double weight
    float completionTime
    //LocalTime completionLocalTime
    //float minOperationDuration
    //LocalTime minOpDurationLocalTime
    boolean hardDeadline
    boolean done
    String taskColor
    static hasMany = [subTaskList : SubTask]
    static constraints = {
    }

    static mapping = {
        id(generator: "uuid")
        version false
    }
}
