package ir.fum.cloud.notification.core.data.hibernate.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
class BaseEntity implements Serializable {

    private static final long serialVersionUID = 4134029653232426300L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @CreationTimestamp
    @Column(name = "INSERTTIME")
    private LocalDateTime insertTime;

    @UpdateTimestamp
    @Column(name = "UPDATETIME")
    private LocalDateTime updateTime;

    @Column(name = "ACTIVE")
    @Builder.Default
    private boolean active = true;

    private long version;

}
