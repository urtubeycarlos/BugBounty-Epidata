package edu.epidata.todo.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.epidata.todo.users.User;

/**
 * El servicio de logs.
 */
@Service
public class LogService {
	private final HashMap<User, List<Log>> logs = new HashMap<>();

	public List<Log> getLogs() {
		return logs.values().stream().flatMap(list -> list.stream()).collect(Collectors.toList());
	}

	/**
	 * Este m�todo agrega un log a la lista de logs.
	 * 
	 * @param action La acci�n a logear
	 * @param user   El usuario que gener� la acci�n
	 */
	//Agregaba dos veces el log
	public synchronized void addLog(String action, User user) {
		Log log = new Log(UUID.randomUUID(), action, user);
		List<Log> list = logs.get(user);
		if (list == null) {
			list = new ArrayList<>();
			logs.put(user, list);
		}
		list.add(log);
//		list.add(log);
	}

	/**
	 * Limpia la lista de logs.
	 */
	public void clear() {
		this.logs.clear();
	}

	public List<Log> getUserLogs(User user) {
		return this.logs.get(user);
	}
}
