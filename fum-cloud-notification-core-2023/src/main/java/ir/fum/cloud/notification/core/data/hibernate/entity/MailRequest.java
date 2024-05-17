package ir.fum.cloud.notification.core.data.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.jsoup.Jsoup;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By F.Khojasteh on 2/8/2020
 */

@Entity
@Table(name = "NOT2_MAIL_REQUEST")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class MailRequest extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private RequestModel request;

    @Lob
    @Column(name = "BODY")
    private String body;

    @Lob
    @Column(name = "TEXT")
    private String text;

    @Column(name = "SUBJECT")
    private String subject;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIL_CONFIG", nullable = false)
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private MailConfig mailConfig;

    @OneToMany(
            mappedBy = "mailRequest",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<MailItem> mailItemSet = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "NOT2_BCC", joinColumns = @JoinColumn(name = "NOT2_MAIL_REQUEST_ID"))
    @Column(name = "BCC")
    private List<String> bcc;

    @ElementCollection
    @CollectionTable(name = "NOT2_CC", joinColumns = @JoinColumn(name = "NOT2_MAIL_REQUEST_ID"))
    @Column(name = "CC")
    private List<String> cc;

    @Column(name = "REPLY_ADDRESS")
    private String replyAddress;


    public Set<MailItem> getMailItemSet() {
        return mailItemSet;
    }

    public void setMailItemSet(Set<MailItem> mailItemSet) {
        this.mailItemSet = mailItemSet;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        String temp = body.replaceAll("<div class=\"notification\".*فایل پیوست :", "");
        temp = temp.replaceAll("<div class=\"notification\".*پیوست:", "");
        this.text = Jsoup.parse(temp).text();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public String getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

}
