package edu.epidata.todo.users;

import java.util.*;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.epidata.todo.log.LogService;

import javax.annotation.PostConstruct;

@Service
public class UserService {
    @Autowired
    private LogService logSvc;

    public static final User DEFAULT_USER = new User("Admin", 0);

    @Autowired
    private CurrentUserService currentSvc;

    @Autowired
    private UserRepository repo;

    @PostConstruct
    public void setup() {
        repo.save(DEFAULT_USER);
    }

    //Hay que cambiar result.sort(Comparator.comparing(User::getId)); por
    //result.sort(Comparator.comparing(User::getName));
    public List<User> getUsers() {
        ArrayList<User> result = new ArrayList<>();
        for (User user : repo.findAll()) {
            result.add(user);
        }
        result.sort(Comparator.comparing(User::getName));
        return result;
    }

    public User addUser(User user) {
    	if( user == null )
    		return null;
		logSvc.addLog("Se agregó el usuario " + user.getName(), user);
		return this.repo.save(user);
    }

    public void clearUsers() {
        this.repo.deleteAll();
        this.logSvc.clear();
    }

    /**
     * Logea y crea un nuevo usuario con un nombre dado.
     * Busca el id máximo actual en la base y le suma 1 para asignar dicho ID al nuevo usuario.
     * Además, utiliza dicho usuario como usuario actual de la sessión.
     *
     * @param name El nombre del usuario.
     * @return un nuevo User
     */
    // El post incremento hace que no se sume uno a la variable porque setea
    // antes de incrementar, osea, lastId = lastId++ no funciona.
    public User login(String name) {
        User last = repo.findFirstByOrderByIdDesc().orElse(null);
        int lastId = last == null ? 0 : last.getId();
        //Aca se soluciono el error
        lastId = ++lastId;
        User user = new User(name, lastId);
        currentSvc.setCurrent(user);
        return addUser(user);
    }

    public void setLogSvc(LogService logSvc) {
        this.logSvc = logSvc;
    }
}
