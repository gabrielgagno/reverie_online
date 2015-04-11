package com.reverie

import org.joda.time.LocalDate
import org.joda.time.LocalTime

class Habit extends Job{
    LocalDate rangeStart
    LocalDate rangeEnd
    LocalTime start
    LocalTime end
    String frequency

    static constraints = {
    }
    static belongsTo = [owner:User]
    static mapping = {
        id(generator: "uuid")
        version false
    }
}
