package final_project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @Column(name = "email_code")
    private String code;
    @JoinColumn(name = "sender_email")
    @ManyToOne(optional = false)
    private User sender;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "email_recipients",
            joinColumns = @JoinColumn(name = "email_code"),
            inverseJoinColumns = @JoinColumn(name = "recipient_email")
    )
    private List<User> recipients = new ArrayList<>();
    @Basic(optional = false)
    private String subject;
    @Basic(optional = false)
    private String body;
    @Basic
    @Column(name = "is_read")
    private boolean isRead;

    @Basic(optional = false)
    @Column(name = "sent_date")
    private LocalDate sentDate;

    protected static ArrayList<Email> allEmails = new ArrayList<>();

    public Email(User sender, List<User> recipients, String subject, String body){
        this.code = CodeGenerator.generateCode();
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.isRead = false;
        this.sentDate = LocalDate.now();
    }
    public Email(){}

    //getter and setter methods
    //Emails' fields cannot be changed after being sent, so there will be no setter except for isRead field
    public String getCode() {
        return code;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead() {
        isRead = true;
    }


    public void setRecipients(List<User> recipients) {
        this.recipients = recipients;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }
}
