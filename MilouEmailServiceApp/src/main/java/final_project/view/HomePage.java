package final_project.view;

import final_project.controller.UserAuthenticator;
import final_project.controller.UserController;
import final_project.model.Email;
import final_project.model.InvalidOperationException;
import final_project.model.User;

import java.util.ArrayList;
import java.util.Scanner;


public class HomePage {
    protected static UserController userController;
    protected static void show(){
        System.out.println("[S]end, [V]iew, [R]eply, [F]orward, [L]og out: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        input = input.toLowerCase();

        switch (input) {
            case "s", "send" -> {
                System.out.println("Recipient(s): ");
                String recipients = scanner.nextLine();
                String[] recipientsEmail = recipients.split(" ");

                for(int i = 0; i < recipientsEmail.length; i++) {
                    if (recipientsEmail[i].contains(",")) {
                        recipientsEmail[i] = recipientsEmail[i].replace(",", "");
                    }
                    if(!recipientsEmail[i].contains("@"))
                        recipientsEmail[i] = recipientsEmail[i].concat("@milou.com");
                    recipientsEmail[i] = recipientsEmail[i].trim();
                    recipientsEmail[i] = recipientsEmail[i].toLowerCase();
                }

                ArrayList<User> recipientsEntity = new ArrayList<>();

                for(String s : recipientsEmail){
                    for(User u : User.getAllUsers()){
                        if(u.getEmail().equals(s))
                            recipientsEntity.add(u);
                    }
                }

                System.out.println("Subject: ");
                String subj = scanner.nextLine();
                System.out.println("Body");
                String body = scanner.nextLine();

                Email email = new Email(userController.getCurrentUser(), recipientsEntity, subj, body);
                userController.sendEmail(email);
                System.out.println("Successfully sent your email.");
                System.out.println("Code: " + email.getCode());
                show();
                break;
            }
            case "v", "view" -> {
                ReadEmailPage.show();
                break;
            }
            case "r", "reply" -> {
                System.out.println("Code: ");
                String code = scanner.nextLine();
                System.out.println("Body: ");
                String body = scanner.nextLine();
                try{
                    userController.replyEmail(code, body);
                }
                catch (InvalidOperationException e){
                    System.out.println(e.getMessage());
                }
                show();
                break;
            }
            case "f", "forward" -> {
                System.out.println("Code: ");
                String code = scanner.nextLine();
                System.out.println("Recipient(s)");
                String recipients = scanner.nextLine();
                String[] recipientsEmail = recipients.split(" ");

                for(int i = 0; i < recipientsEmail.length; i++) {
                    if (recipientsEmail[i].contains(",")) {
                        recipientsEmail[i] = recipientsEmail[i].replace(",", "");
                    }
                    if(!recipientsEmail[i].contains("@"))
                        recipientsEmail[i] = recipientsEmail[i].concat("@milou.com");
                    recipientsEmail[i] = recipientsEmail[i].trim();
                    recipientsEmail[i] = recipientsEmail[i].toLowerCase();
                }

                ArrayList<User> recipientsEntity = new ArrayList<>();

                for(String s : recipientsEmail){
                    for(User u : User.getAllUsers()){
                        if(u.getEmail().equals(s))
                            recipientsEntity.add(u);
                    }
                }
                try{
                    userController.forwardEmail(code, recipientsEntity);
                }
                catch (InvalidOperationException e){
                    System.out.println(e.getMessage());
                    show();
                }
                show();
            }
            case "l", "log out" -> {
                UserAuthenticator.logOut();
            }
            default -> {
                System.out.println("Please insert a correct command.");
                show();
            }
        }
    }
}
