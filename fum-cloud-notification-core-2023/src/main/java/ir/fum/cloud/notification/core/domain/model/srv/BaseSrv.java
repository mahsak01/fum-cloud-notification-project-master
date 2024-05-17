package ir.fum.cloud.notification.core.domain.model.srv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BaseSrv {

    private Long id;
    private Long insertTimestamp;
    private Long updateTimestamp;

}