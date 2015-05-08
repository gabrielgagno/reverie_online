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
        "500"(view:'/error')
	}
}
