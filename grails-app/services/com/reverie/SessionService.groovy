package com.reverie

import grails.transaction.Transactional

@Transactional
class SessionService {

    def getCurrentUser(String id) {
        return User.findById(id)
    }

    def getUserTasks(User user){
        return Task.findAllByOwner(user)
    }

    def getUserHabits(User user){
        return Habit.findAllByOwner(user)
    }

    def getUserSubTasks(User owner){
        def ownedTasks = Task.findAllByOwner(owner)
        def ownedHabits = Habit.findAllByOwner(owner)

        def query = SubTask.where {
            (inList("motherTask", ownedTasks) || inList("motherTask", ownedHabits))
        }

        return query.list(sort: "subTaskStart")
    }

    def saveSettings(String id, String firstName, String lastName, String email, String password, int priority){
        User user = getCurrentUser(id)
        user.firstName = firstName
        user.lastName = lastName
        user.email = email
        user.password = password
        if(priority==1){
            user.deadlineConstant = 2
            user.completionTimeConstant = 1
        }
        else{
            user.deadlineConstant = 1
            user.completionTimeConstant = 2
        }
        user.save()
    }
}
