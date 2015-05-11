package com.reverie

abstract class Job {
    String id
    String jobName
    String jobNotes
    static belongsTo = [owner:User]
    static constraints = {

    }

    static mapping = {
        id(generator: "uuid")
        version false
    }
}
