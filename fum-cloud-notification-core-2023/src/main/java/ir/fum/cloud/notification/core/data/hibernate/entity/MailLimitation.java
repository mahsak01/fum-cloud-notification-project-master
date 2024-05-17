package ir.fum.cloud.notification.core.data.hibernate.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "NOT2_MAIL_LIMITATION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"RECEIVER", "SENDER"}))
@SuperBuilder
@NoArgsConstructor
public class MailLimitation extends BaseEntity {

    @Column(name = "RECEIVER", nullable = false)
    private String receiver;

    @Column(name = "SENDER", nullable = false)
    private String sender;

    @Column(name = "LAST_UPDATE_TIME_COUNTER")
    private LocalDateTime lastUpdateTimeCounter;

    @Version
    @ColumnDefault("0")
    @Column(name = "OPT_LOCK_VERSION")
    private long optLockVersion;

    @Column(name = "COUNTER", nullable = false)
    private int counter;
}