package final_project.view;

import final_project.controller.UserAuthenticator;
import final_project.controller.UserController;
import final_project.model.InvalidOperationException;

import java.util.Scanner;

public class LoginPage {
    public static void show() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("[L]ogin, [S]ign up:");
        String input = scanner.nextLine();
        input = input.toLowerCase();
        if(input.equals("l") || input.equals("login")){

            System.out.println("Email: ");
            String email = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();

            if(!email.contains("@"))
                email = email.concat("@milou.com");
            email = email.trim();
            email = email.toLowerCase();

            try{
                UserAuthenticator.login(email, password);
            }
            catch (InvalidOperationException e){
                System.out.println(e.getMessage());
                show();
            }
            HomePage.userController = new UserController(UserAuthenticator.getLoggedIn());
            HomePage.userController.readUnreadEmails();
            HomePage.show();
        }
        else if(input.equals("s") || input.equals("sign up")){
            System.out.println("Name: ");
            String name = scanner.nextLine();
            System.out.println("Email: ");
            String email = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();

            if(!email.contains("@"))
                email = email.concat("@milou.com");
            email = email.trim();
            email = email.toLowerCase();

            try{
                UserAuthenticator.signUp(name, email, password);
            }
            catch (InvalidOperationException e){
                System.out.println(e.getMessage());
                show();
            }
            show();
        }
        else{
            System.out.println("Please insert a correct command");
            show();
        }
    }
}
