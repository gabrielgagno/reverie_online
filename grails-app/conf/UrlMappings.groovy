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
        "500"(view:'/error')
	}
}
