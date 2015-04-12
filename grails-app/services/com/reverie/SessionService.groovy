package com.reverie

import grails.transaction.Transactional

@Transactional
class SessionService {

    def getCurrentUser(String id) {
        return User.findById(id)
    }
}
