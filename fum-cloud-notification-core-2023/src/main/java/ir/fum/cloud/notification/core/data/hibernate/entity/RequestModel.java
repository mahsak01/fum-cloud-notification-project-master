package ir.fum.cloud.notification.core.data.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created By F.Khojasteh on 2/8/2020
 */

@Entity
@Table(name = "NOT2_REQUEST", indexes = {@Index(name = "messageTypeIndex", columnList = "MESSAGE_TYPE")})
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class RequestModel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4134029653232426310L;
    @Column(name = "NOT_USER", nullable = false)
    private long userId;

    @Column(name = "NOTIFICATION_ID", nullable = false)
    private String notificationId;


    @Column(name = "JOB_ID")
    private String jobId;

    @Column(name = "MESSAGE_TYPE", nullable = false)
    private Integer messageType;


    @OneToOne(
            mappedBy = "request",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private MailRequest mailRequest;


}
