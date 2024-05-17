package ir.fum.cloud.notification.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created By Khojasteh on 9/29/2019
 */

@Slf4j
public class GeneralUtils {


    private static final String HALF_SPACE = "‌";


    public static boolean isNullOrEmpty(String param) {
        return param == null || param.isEmpty() || param.replaceAll(HALF_SPACE, "").trim().isEmpty();
    }

    public static <k, T> boolean isNullOrEmpty(Map<k, T> param) {
        return param == null || param.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(Set<T> param) {
        return param == null || param.isEmpty();
    }


    public static <T> boolean isNullOrEmpty(List<T> param) {
        return param == null || param.stream().allMatch(Objects::isNull);
    }

    public static <T> boolean isNullOrEmpty(T[] param) {
        return param == null || param.length == 0 || Arrays.stream(param).allMatch(Objects::isNull);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> param) {
        return param == null || param.isEmpty();
    }


    public static <T> boolean isNullOrZero(Integer number) {
        return number == null || number == 0L;
    }


    public static boolean emailIsValid(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        return email.matches(regex);
    }

    public static boolean phoneIsValid(String mobileNumber) {
        String regex1 = "^(\\+98|0|0098|98)?9\\d{9}$";
        String regex2 = "09000000000";
        String regex3 = "09653214569";

        return mobileNumber.matches(regex1) && !mobileNumber.matches(regex2) && !mobileNumber.matches(regex3);
    }


    public static boolean isNumeric(String s) {
        return StringUtils.isNumeric(s);
    }

    public static <E extends Enum<E>> boolean isValidParam(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        return false;
    }


    public static long getTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime).getTime();
    }


    public static synchronized String generateUniqueId() {
        return UUID.randomUUID().toString();
    }


}
