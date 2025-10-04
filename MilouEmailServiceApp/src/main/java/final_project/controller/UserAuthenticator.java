package final_project.controller;

import final_project.framework.SingletonSessionFactory;
import final_project.model.InvalidOperationException;
import final_project.model.User;
import final_project.view.LoginPage;
import jakarta.persistence.NoResultException;

public class UserAuthenticator {
    private static User loggedIn = null;
    public static void login(String email, String password) throws InvalidOperationException {
        try{
            User user = (User) SingletonSessionFactory.get().fromTransaction(session ->
                    session.createNativeQuery("select * from users\n" +
                                    "where email = :email", User.class)
                            .setParameter("email", email)
                            .getSingleResult());
        }
        catch (NoResultException e){
            System.out.println("This username does not exist");
            LoginPage.show();
        }
        User user = (User) SingletonSessionFactory.get().fromTransaction(session ->
                session.createNativeQuery("select * from users\n" +
                                "where email = :email", User.class)
                        .setParameter("email", email)
                        .getSingleResult());

        if(!user.getPassword().equals(password)){
            throw new InvalidOperationException("Password is incorrect");
        }
        loggedIn = user;
        System.out.println("Welcome back, " + loggedIn.getName() + "!");
    }

    public static void signUp(String name, String email, String password) throws InvalidOperationException {
        for(User u : User.getAllUsers()){
            if(u.getEmail().equals(email)){
                throw new InvalidOperationException("Username is already taken");
            }
        }
        User newUser = new User(name, email, password);
        SingletonSessionFactory.get().inTransaction(session -> {
            session.persist(newUser);
            System.out.println("Your new account is created.");
            System.out.println("Go ahead and login!");
        });
    }

    public static void logOut(){
        loggedIn = null;
        LoginPage.show();
    }

    public static User getLoggedIn() {
        return loggedIn;
    }

}
