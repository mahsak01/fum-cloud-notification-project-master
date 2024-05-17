package ir.fum.cloud.notification.core.domain.model.helper;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> implements Serializable {

    @Builder.Default
    private Boolean hasError = false;
    private String errorDescription;
    private int errorCode;
    private String message;
    private T content;
    @Builder.Default
    private long totalCount = 0L;
    private String trackerId;
    @Builder.Default
    private transient int status = 200;
    private double price;


}
