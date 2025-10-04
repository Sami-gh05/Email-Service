package final_project.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Basic(optional = false)
    private String name;
    @Id
    private String email;
    @Basic(optional = false)
    private String password;

    private static List<User> allUsers = new ArrayList<>();

    public User(String name, String email, String password) throws InvalidOperationException {
        this.name = name;
        this.email = email;
        setPassword(password);
        allUsers.add(this);
    }

    public User(){}


    //getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidOperationException {
        if(password.length() < 8)
            throw new InvalidOperationException("Password should be 8 or more characters");
        this.password = password;
    }

    public static List<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(List<User> allUsers) {
        User.allUsers = allUsers;
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }
}
