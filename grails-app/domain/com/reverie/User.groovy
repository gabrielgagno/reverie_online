package com.reverie

class User {
    String id
    String email
    String username
    String password
    String firstName
    String lastName

    static hasMany = [habitQueue:Habit, priorityQueue:Task]
    static constraints = {
        email unique: true
        username unique: true
    }

    static mapping = {
        id(generator: "uuid", length: 36)
        version false
    }
}
