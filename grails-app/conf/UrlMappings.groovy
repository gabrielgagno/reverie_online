class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "session", action: "index")
        "/profile"(controller: "session", action: "profile")
        "/settings"(controller: "session", action: "settings")
        "/myjobs"(controller: "jobs", action: "jobsList")
        "/new/task"(controller: "jobs", action: "newTask")
        "/new/habit"(controller: "jobs", action: "newHabit")
        "/edit/task"(controller: "jobs", action: "editTask")
        "/edit/habit"(controller: "jobs", action: "editHabit")
        "/delete/task"(controller: "jobs", action: "deleteTask")
        "/delete/habit"(controller: "jobs", action: "deleteHabit")
        "500"(view:'/error')
        "404"(view:'notFound')
	}
}
