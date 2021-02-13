package edu.epidata.todo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import edu.epidata.todo.log.LogService;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUserService {
	@Autowired
    private LogService svc;

	private User current;

	public synchronized User getCurrent() {
		if (this.current == null)
			this.current = new User();
		return this.current;
	}

	public void setCurrent(User user) {
		svc.addLog("Log-in de usuario", user);
		current = user;
	}
}
