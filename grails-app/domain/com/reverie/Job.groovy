package com.reverie

abstract class Job {
    String id
    String jobName
    String jobNotes
    static constraints = {

    }

    static mapping = {
        id(generator: "uuid")
        version false
    }
}
