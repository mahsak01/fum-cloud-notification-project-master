package ir.fum.cloud.notification.core.data.hibernate.entity;

import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "NOT2_MAIL_ITEM")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class MailItem extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4134029653232426311L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIL_REQUEST", nullable = false)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MailRequest mailRequest;

    @Column(name = "RECEIVER", nullable = false)
    private String receiver;

    @Column(name = "SEND_DATE")
    private LocalDateTime sendDate;

    @Column(name = "STATE")
    private RequestState state;

    @Column(name = "META_DATA", length = 4000)
    private String metaData;
    @Column(name = "MESSAGE_ID")
    private String messageId;


}
