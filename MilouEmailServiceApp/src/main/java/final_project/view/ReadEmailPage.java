package final_project.view;

import final_project.model.InvalidOperationException;

import java.util.Scanner;

public class ReadEmailPage {
    protected static void show(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("[A]ll emails, [U]nread emails, [S]ent emails, Read by [C]ode: ");
        String input = scanner.nextLine();
        input = input.toLowerCase();
        switch (input){
            case "a", "all emails" -> {
                HomePage.userController.readAllEmails();
                HomePage.show();
            }
            case "u", "unread emails" -> {
                HomePage.userController.readUnreadEmails();
                HomePage.show();
            }
            case "s", "sent emails" -> {
                HomePage.userController.readSentEmails();
                HomePage.show();
            }
            case "c", "read by code" -> {
                System.out.println("Code: ");
                String code = scanner.nextLine();
                try {
                    HomePage.userController.readEmailByCode(code);
                }
                catch (InvalidOperationException e){
                    System.out.println(e.getMessage());
                    show();
                }
                HomePage.show();
            }
        }
    }
}
