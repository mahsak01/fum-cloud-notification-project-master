package ir.fum.cloud.notification.core.data.hibernate.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOT2_SEND_MAIL_CONFIG")
public class MailConfig extends BaseEntity {

    @Column(name = "USER_ID", nullable = false)
    private long userId;
    @Column(name = "HOST", nullable = false)
    private String host;
    @Column(name = "PORT", nullable = false)
    private String port;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "ADDRESS", nullable = false)
    private String address;
    @Builder.Default
    private boolean removed = false;

}
