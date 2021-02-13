package edu.epidata.todo.todo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.epidata.todo.log.LogService;
import edu.epidata.todo.users.CurrentUserService;
import edu.epidata.todo.users.UserService;

@Service
public class TodoService {
    @Autowired
    private LogService svc;

    @Autowired
    CurrentUserService currentSvc;

    @Autowired
    private TodoRepository repo;

    public List<ToDo> getTodoList() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void addTODO(ToDo todo) {
        svc.addLog("Se agregó el todo " + todo.getContent(), todo.getUser());
        repo.save(todo);
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }

    /**
     * Elimina los ToDos más viejos que sec
     *
     * @param sec Segundos que debe tener el ToDo más viejo.
     */
    //Habia un error en la logica del segundosDelTodo con el segundo pasado como parametro, era mayor y no menor.
    public synchronized void deleteOldMessages(int sec) {
        Date current = new Date();
        for (ToDo todo : getTodoList()) {
            long segundosDelTodo = (current.getTime() - todo.getDate().getTime()) / 1000;
            if (segundosDelTodo > sec) {
                svc.addLog("Se borró automáticamente el todo, " + todo.getContent(), UserService.DEFAULT_USER);
                repo.delete(todo);
            }
        }
    }

    /**
     * Una vez que Spring crea este servicio, se crea un timer que cada 1 segundo
     * borra los ToDos m�s viejos (con edad mayor a 30 segundos)
     */
    @PostConstruct
    public void init() {
        Timer timer = new Timer("Delete Old Messages Thread");
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                deleteOldMessages(30);
            }
        }, 0, 1000);
    }

    /**
     * Se crea un ToDo con contenido. El usuario se obtiene de la sesión actual.
     *
     * @param content El contenido del ToDo
     * @return
     */
    //No agregaba el contenido con nuevo.setContent(content)
    public ToDo createNewTodo(String content) {
        ToDo nuevo = new ToDo();
        nuevo.setUser(currentSvc.getCurrent());
        nuevo.setId(UUID.randomUUID());
        nuevo.setContent(content);
        addTODO(nuevo);
        return nuevo;
    }
}
