package ir.fum.cloud.notification.core.exception.runtime;

import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonProcessException extends NotificationRuntimeException {

    private String message;

    public JsonProcessException(String message) {
        this.message = message;
    }

    public JsonProcessException(Exception e, String simpleClassName) {
        String eMessage = e.getMessage();
        if (e instanceof JsonMappingException && eMessage.startsWith("Can not deserialize instance")) {
            message = "Invalid json format - ";
            int index = eMessage.indexOf("line:");
            message += eMessage.substring(index, eMessage.indexOf("]", index));
            message += " - " + simpleClassName;

        } else {
            message = "Invalid json format";
        }
        super.initCause(e);
    }

    public JsonProcessException(Exception e) {
        message = "Invalid json format";
        super.initCause(e);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
