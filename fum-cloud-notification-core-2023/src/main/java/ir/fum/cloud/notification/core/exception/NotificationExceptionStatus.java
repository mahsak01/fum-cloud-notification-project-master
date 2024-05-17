package ir.fum.cloud.notification.core.exception;

import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Getter
public class NotificationExceptionStatus {

    public static final NotificationExceptionStatus TOOKA_ERROR = new NotificationExceptionStatus(
            "INVALID_INPUT", 101, "ورودی(ها) نامعتبر است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus INVALID_INPUT = new NotificationExceptionStatus(
            "INVALID_INPUT", 101, "ورودی(ها) نامعتبر است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus MISSING_INPUT = new NotificationExceptionStatus(
            "MISSING_INPUT", 102, "پارامترهای الزامی را وارد کنید.", ResponseCode.UNPROCESSABLE_ENTITY
    );
    public static final NotificationExceptionStatus INVALID_APP_ID = new NotificationExceptionStatus(
            "INVALID_APP_ID", 106, "برنامه کاربردی با این شناسه موجود نیست.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus ACCESS_DENIED = new NotificationExceptionStatus(
            "ACCESS_DENIED", 107, "دسترسی غیر مجاز", ResponseCode.FORBIDDEN
    );
    public static final NotificationExceptionStatus INVALID_HASH = new NotificationExceptionStatus(
            "INVALID_HASH", 108, "هش(های) وارد شده صحیح نیست.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus UNAUTHORIZED = new NotificationExceptionStatus(
            "UNAUTHORIZED", 109, "دسترسی غیر مجاز", ResponseCode.UNAUTHORIZED
    );
    public static final NotificationExceptionStatus CONFIG_ID_NOT_EXIST = new NotificationExceptionStatus(
            "CONFIG_ID_NOT_EXIST", 122, "کانفیگ وجود ندارد.", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus ALREADY_EXIST = new NotificationExceptionStatus(
            "ALREADY_EXIST", 123, "کانفیگ قبلا وارد شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus TIME_EXCEPTION = new NotificationExceptionStatus(
            "TIME_EXCEPTION", 124, "زمان وارد شده صحیح نمی باشد.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus INVALID_EMAIL_ADDRESS = new NotificationExceptionStatus(
            "INVALID_EMAIL_ADDRESS", 125, "فرمت آدرس ایمیل وارد شده صحیح نیست.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus INVALID_PHONE_NUMBER = new NotificationExceptionStatus(
            "INVALID_PHONE_NUMBER", 126, "فرمت شماره وارد شده صحیح نیست.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus INVALID_CONDITION = new NotificationExceptionStatus(
            "INVALID_CONDITION", 128, "شرط وارد شده معتبر نیست.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus DASHBOARD_ERROR = new NotificationExceptionStatus(
            "DASHBOARD_ERROR", 131, "خطا در اشتراک گذاری داشبورد", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus IS_NOT_POSITIVE = new NotificationExceptionStatus(
            "IS_NOT_POSITIVE", 135, "افست و سایز را مثبت وارد کنید.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus INVALID_SERVICE_NAME = new NotificationExceptionStatus(
            "INVALID_SERVICE_NAME", 141, "نام سرویس وارد شده اشتباه است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus ATTACH_FAILED = new NotificationExceptionStatus(
            "ATTACH_FAILED", 145, "اضافه کردن فایل با خطا مواجه شد.", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus HASH_ACCESS_DENIED = new NotificationExceptionStatus(
            "HASH_ACCESS_DENIED", 146, "شما دسترسی به این فایل(ها) را ندارید.", ResponseCode.UNAUTHORIZED
    );
    public static final NotificationExceptionStatus ALREADY_CONFIRMED = new NotificationExceptionStatus(
            "ALREADY_CONFIRMED", 152, "شرایط قبلا تایید شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus UNCONFIRMED_TERM = new NotificationExceptionStatus(
            "UNCONFIRMED_TERM", 153, "شرایط استفاده از سرویس ها تایید نشده  است.", ResponseCode.FORBIDDEN
    );
    public static final NotificationExceptionStatus DUPLICATE_SMS_REQUEST = new NotificationExceptionStatus(
            "DUPLICATE_SMS_REQUEST", 156, "شماره تلفن و متن پیام تکراری است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus ALREADY_ACTIVATED = new NotificationExceptionStatus(
            "ALREADY_ACTIVATED", 159, "کانفیگ قبلا فعال شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus ALREADY_DEACTIVATED = new NotificationExceptionStatus(
            "ALREADY_DEACTIVATED", 160, "کانفیگ قبلا غیرفعال شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus INVALID_CONFIGS = new NotificationExceptionStatus(
            "INVALID_CONFIGS", 161, "یک یا چند کانفیگ اشتباه است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus DATA_BASE_ERROR = new NotificationExceptionStatus(
            "DATA_BASE_ERROR", 165, "خطا در پایگاه داده اتفاق افتاده است.", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus PHONE_NOT_FOUND = new NotificationExceptionStatus(
            "PHONE_NOT_FOUND", 173, "شماره همراه خود را وارد کنید.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus EMAIL_NOT_FOUND = new NotificationExceptionStatus(
            "EMAIL_NOT_FOUND", 174, "آدرس ایمیل خود را وارد کنید.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus EMAIL_NOT_SEND = new NotificationExceptionStatus(
            "EMAIL_NOT_SEND", 175, "ارسال ایمیل با خطا مواجه شد.", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus POD_SPACE_ERROR = new NotificationExceptionStatus(
            "POD_SPACE_ERROR", 180, "خطا در پاد اسپیس", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus INVALID_REQUEST = new NotificationExceptionStatus(
            "INVALID_REQUEST", 181, "درخواست نامعتبر است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus CORE_CONNECTION_ERROR = new NotificationExceptionStatus(
            "CORE_CONNECTION_ERROR", 182, "خطا در اتصال به سرویس های core", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus SSO_CONNECTION_ERROR = new NotificationExceptionStatus(
            "SSO_CONNECTION_ERROR", 183, "خطا در اتصال به سرویس های sso", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus VARIABLE_TYPE_NOT_FOUND = new NotificationExceptionStatus(
            "VARIABLE_TYPE_NOT_FOUND", 184, "این نوع متغیر موجود نیست.", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus OPERATOR_NOT_FOUND = new NotificationExceptionStatus(
            "OPERATOR_NOT_FOUND", 185, "این عملگر موجود نیست.", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus ALREADY_DELETED = new NotificationExceptionStatus(
            "ALREADY_DELETED", 186, "قبلا حذف شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus NOT_FOUND = new NotificationExceptionStatus(
            "NOT_FOUND", 187, "درخواست شما یافت نشد.", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus INVALID_ACTION_TYPE = new NotificationExceptionStatus(
            "INVALID_ACTION_TYPE", 189, "نوع گروه داده شده با نوع عمل خواسته شده متناسب نیست.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus INVALID_TOKEN = new NotificationExceptionStatus(
            "INVALID_TOKEN", 190, "توکن نامعتبر", ResponseCode.UNAUTHORIZED
    );
    public static final NotificationExceptionStatus FIRE_ID_NOT_EXIST = new NotificationExceptionStatus(
            "FIRE_ID_NOT_EXIST", 191, "", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus EVENT_INFO_NOT_COMPLETE = new NotificationExceptionStatus(
            "EVENT_INFO_NOT_COMPLETE", 192, "", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus DUPLICATE_INPUT = new NotificationExceptionStatus(
            "DUPLICATE_INPUT", 193, "", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus ALLOCATED_EVENT = new NotificationExceptionStatus(
            "ALLOCATED_EVENT", 194, "رویداد مورد نظر به کاربری اختصاص داده شده است.", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus LAST_ITEM = new NotificationExceptionStatus(
            "LAST_ITEM", 195, "نمی‌توان آخرین آیتم را حذف کرد.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus IN_USE = new NotificationExceptionStatus(
            "IN_USE", 196, "در حال حاضر این آیتم در حال استفاده است.", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus ENDPOINT_NOT_FOUND = new NotificationExceptionStatus(
            "ENDPOINT_NOT_FOUND", 197, "مسیر درخواستی پیدا نشد.", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus BUSINESS_ACTIVATION = new NotificationExceptionStatus(
            "BUSINESS_ACTIVATION", 198, "", ResponseCode.FORBIDDEN
    );
    public static final NotificationExceptionStatus CUSTOMER_NOT_FOUND = new NotificationExceptionStatus(
            "CUSTOMER_NOT_FOUND", 199, "این مشتری موجود نیست", ResponseCode.NOT_FOUND
    );
    public static final NotificationExceptionStatus CUSTOMER_CONFLICT = new NotificationExceptionStatus(
            "CUSTOMER_CONFLICT", 200, "", ResponseCode.CONFLICT
    );
    public static final NotificationExceptionStatus INVALID_MAIL_CONFIG = new NotificationExceptionStatus(
            "INVALID_MAIL_CONFIG", 202, "", ResponseCode.BAD_REQUEST
    );
    public static final NotificationExceptionStatus BALE_ERROR = new NotificationExceptionStatus(
            "BALE_ERROR", 201, "", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus ELASTIC_ERROR = new NotificationExceptionStatus(
            "ELASTIC_ERROR", 999, "خطایی رخ داده است.", ResponseCode.INTERNAL_SERVER_ERROR
    );
    public static final NotificationExceptionStatus INTERNAL_ERROR = new NotificationExceptionStatus(
            "INTERNAL_ERROR", 999, "خطای داخلی رخ داده است.", ResponseCode.INTERNAL_SERVER_ERROR
    );
    private static HashMap<String, NotificationExceptionStatus> map = new HashMap<>();
    final String name;
    final int code;
    final String description;
    final int status;

    public NotificationExceptionStatus(String name, int code, String description, int status) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.status = status;
        if (GeneralUtils.isNullOrEmpty(map)) {
            map = new HashMap<>();
        }
        map.put(name, this);
    }

    // replacement for Enum.valueOf
    public static NotificationExceptionStatus valueOf(String name) {
        return map.get(name);
    }

    public static GenericResponse<Object> setErrorMessage(NotificationException e) {
        return getGenericResponse(e.getDeveloperMessage(), e.getCode(), e.getStatus());

    }

    public static GenericResponse<Object> setErrorMessage(NotificationValidationException e) {
        return getGenericResponse(e.getDeveloperMessage(), e.getCode(), e.getStatus());

    }

    public static GenericResponse<Object> setErrorMessage(String description, NotificationExceptionStatus status) {
        Logger logger = LogManager.getLogger(NotificationExceptionStatus.class);
        logger.log(Level.ERROR, "{} {}", description, status.getCode());

        return GenericResponse.builder()
                .errorDescription(description)
                .errorCode(status.getCode())
                .hasError(true)
                .content(null)
                .status(status.status)
                .build();

    }

    public static GenericResponse<Object> setErrorMessage(String description, int code, int status) {
        Logger logger = LogManager.getLogger(NotificationExceptionStatus.class);
        logger.log(Level.ERROR, "{} {}", description, code);

        return GenericResponse.builder()
                .errorDescription(description)
                .errorCode(code)
                .hasError(true)
                .content(null)
                .status(status)
                .build();

    }

    private static GenericResponse<Object> getGenericResponse(String developerMessage, int code, int status) {
        Logger logger = LogManager.getLogger(NotificationExceptionStatus.class);
        logger.log(Level.ERROR, "{} {}", developerMessage, code);

        return GenericResponse.builder()
                .errorDescription(developerMessage)
                .errorCode(code)
                .hasError(true)
                .content(null)
                .status(status == 0 ? 200 : status)
                .build();
    }

    // accessors
    public String name() {
        return name;
    }

}
