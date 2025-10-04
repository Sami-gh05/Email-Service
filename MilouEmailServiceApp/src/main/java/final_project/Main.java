package final_project;

import final_project.controller.UserController;
import final_project.framework.SingletonSessionFactory;
import final_project.model.Email;
import final_project.model.InvalidOperationException;
import final_project.model.User;
import final_project.view.LoginPage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws InvalidOperationException {
        List<User> allUsers = SingletonSessionFactory.get().fromTransaction(session ->
                session.createNativeQuery("select * from users", User.class).getResultList());
        User.setAllUsers(allUsers);
        LoginPage.show();
        SingletonSessionFactory.close();
    }
}
