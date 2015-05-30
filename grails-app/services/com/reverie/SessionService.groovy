package com.reverie

import grails.transaction.Transactional
import org.apache.catalina.users.AbstractUser

@Transactional
class SessionService {

    def getCurrentUser(String id) {
        return User.findById(id)
    }

    def getUserTasks(User user){
        return Task.findAllByOwner(user)
    }

    def getTasksByDeeadline(User user){
        return Task.findAllByOwner(user, [sort: "deadline"])
    }

    def getUserHabits(User user){
        return Habit.findAllByOwner(user)
    }

    def getUserSubTasks(User owner){
        def ownedTasks = Task.findAllByOwner(owner)
        def ownedHabits = Habit.findAllByOwner(owner)
        def list = Job.findAllByOwner(owner)
        if(list.size()==0){
            return []
        }
        def query = SubTask.where {
            motherTask in list
        }
        return query.findAll()
    }

    def saveSettings(String id, String firstName, String lastName, String email, String password, int priority){
        def user = getCurrentUser(id)
        println(user.password)
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

    def addUser(String firstName, String lastName, String email, String password, int priority, String username){
        User user = new User()
        user.firstName = firstName
        user.lastName = lastName
        user.email = email
        user.password = password
        user.username = username
        if(priority==1){
            user.deadlineConstant = 2
            user.completionTimeConstant = 1
        }
        else{
            user.deadlineConstant = 1
            user.completionTimeConstant = 2
        }
        user.save(failOnError: true)
    }
}
