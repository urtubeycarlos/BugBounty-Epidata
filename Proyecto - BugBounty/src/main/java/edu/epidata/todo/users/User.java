package edu.epidata.todo.users;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Integer id;

    private String name;

    public User() {
    }

    public User(String name) {
        this(name, 0);
    }

    public User(String name, Integer id) {
        super();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
    	if( this == o )
    		return true;
    	if( o == null )
    		return false;
    	if( !(o instanceof User) )
    		return false;
    	User u2 = (User) o;
    	return Objects.equals(this.getName(), u2.getName());
    }
    
    @Override
    public int hashCode() {
    	return Objects.hashCode( this.getName() );
    }
}
