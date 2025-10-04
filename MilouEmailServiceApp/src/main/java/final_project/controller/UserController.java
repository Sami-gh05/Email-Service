package final_project.controller;

import final_project.framework.SingletonSessionFactory;
import final_project.model.Email;
import final_project.model.InvalidOperationException;
import final_project.model.User;

import java.util.*;

public class UserController {
    private User currentUser;
    public UserController(String name, String email, String password) throws InvalidOperationException {
        currentUser = new User(name, email, password);
    }
    public UserController(User user){
        currentUser = user;
    }
    private void readEmail(Email email) {
        System.out.println("+ " + email.getSender().getEmail() + " - " + email.getSubject() + " (" + email.getCode() + ")\n");
    }
    private void readSentEmail(Email email){
        System.out.print("+ ");
        for(User u : email.getRecipients()){
            System.out.print(u.getEmail() + ", ");
        }
        System.out.println(" - " + email.getSubject() + " (" + email.getCode() + ")");

    }
    private void readFullEmail(Email email){
        System.out.println("Code: " +  email.getCode());
        System.out.print("Recipient(s): ");
        for(User u : email.getRecipients()){
            System.out.print(u.getEmail() + ", ");
        }
        System.out.println("\nSubject: " + email.getSubject());
        System.out.println("Date: " + email.getSentDate().toString());
        System.out.println("\n" + email.getBody());
        email.setRead();
    }

    public void sendEmail(ArrayList<User> recipients, String subject, String body){
        Email email = new Email(currentUser, recipients, subject, body);
        SingletonSessionFactory.get().inTransaction(session -> {session.persist(email);});
    }
    public void sendEmail(Email email){
        SingletonSessionFactory.get().inTransaction(session -> session.persist(email));
    }

    public void readAllEmails(){
        System.out.println("All Emails:");
        List<Email> allEmails = SingletonSessionFactory.get().fromTransaction(session ->
            session.createNativeQuery("select e.email_code, e.sender_email, e.subject, e.body, e.is_read, e.sent_date from emails e\n" +
                    "join email_recipients er on e.email_code = er.email_code\n" +
                    "where er.recipient_email = :given_email" , Email.class)
                            .setParameter("given_email", currentUser.getEmail())
                                    .getResultList());

        allEmails.sort((d1, d2) -> d2.getSentDate().compareTo(d1.getSentDate()));
        for(Email email : allEmails)
            readEmail(email);
    }

    public void readUnreadEmails(){
        System.out.println("Unread Emails:");
        List<Email> allEmails = SingletonSessionFactory.get().fromTransaction(session ->
                session.createNativeQuery("select e.email_code, e.sender_email, e.subject, e.body, e.is_read, e.sent_date from emails e\n" +
                                "join email_recipients er on e.email_code = er.email_code\n" +
                                "where er.recipient_email = :given_email and e.is_read = 0" , Email.class)
                        .setParameter("given_email", currentUser.getEmail())
                        .getResultList());

        allEmails.sort((d1, d2) -> d2.getSentDate().compareTo(d1.getSentDate()));
        for(Email email : allEmails)
            readEmail(email);
    }

    public void readSentEmails(){
        System.out.println("Sent Emails:");
        List<Email> allEmails = SingletonSessionFactory.get().fromTransaction(session ->
                session.createNativeQuery("select * from emails e\n" +
                                "where e.sender_email = :given_email" , Email.class)
                        .setParameter("given_email", currentUser.getEmail())
                        .getResultList());

        allEmails.sort((d1, d2) -> d2.getSentDate().compareTo(d1.getSentDate()));
        for(Email email : allEmails)
            readSentEmail(email);
    }

    public void readEmailByCode(String code) throws InvalidOperationException {
        Email email = SingletonSessionFactory.get().fromTransaction(session -> session.get(Email.class, code));
        if(!email.getSender().getEmail().equals(currentUser.getEmail()) && !email.getRecipients().contains(currentUser))
            readFullEmail(email);
        else
            throw new InvalidOperationException("You cannot read this email.");
    }

    public void replyEmail(String code, String body) throws InvalidOperationException {
        Email email = SingletonSessionFactory.get().fromTransaction(session -> session.get(Email.class, code));
        if(email == null)
            throw new InvalidOperationException("Code was invalid");
        if(!email.getRecipients().contains(currentUser))
            throw new InvalidOperationException("You don't have access to this email.");

        List<User> replyRecipients = new ArrayList<>(email.getRecipients());
        replyRecipients.remove(currentUser);
        replyRecipients.add(email.getSender());

        String newSubject = "[Re] " + email.getSubject();
        Email reply = new Email(currentUser, replyRecipients, newSubject, body);
        sendEmail(reply);
        System.out.println("Successfully sent your reply to email " + code + ".");
        System.out.println("Code: " + reply.getCode());
    }

    public void forwardEmail(String code, List<User> forwardRecipients) throws InvalidOperationException {
        Email email = SingletonSessionFactory.get().fromTransaction(session -> session.get(Email.class, code));
        if(email == null)
            throw new InvalidOperationException("Code was invalid");
        if(!email.getSender().getEmail().equals(currentUser.getEmail()) && !email.getRecipients().contains(currentUser))
            throw new InvalidOperationException("You don't have access to this email.");

        Email newEmail = new Email(currentUser, forwardRecipients, "[Fw] " + email.getSubject(), email.getBody());
        SingletonSessionFactory.get().inTransaction(session -> session.persist(newEmail));
    }



    public User getCurrentUser() {
        return currentUser;
    }
}
