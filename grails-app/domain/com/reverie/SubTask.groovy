package com.reverie

import org.joda.time.LocalDateTime

class SubTask {
    LocalDateTime subTaskStart
    LocalDateTime subTaskEnd
    static belongsTo = [motherTask : Job]
    static constraints = {
    }
}
