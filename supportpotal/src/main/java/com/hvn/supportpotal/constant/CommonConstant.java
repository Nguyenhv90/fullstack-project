package com.hvn.supportpotal.constant;

public class CommonConstant {

    /* ----->>>  Constant for UserService START <<<-----*/
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USER_NOT_FOUND_BY_USERNAME = "User not found by username: ";
    public static String FOUND_USER_BY_USERNAME = "Returning found user by username: ";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email: ";
    //public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/temp";
    /* ----->>>  Constant for UserService END <<<-----*/

    /* ----->>>  Constant for Email START <<<-----*/
    public static final String SIMPLE_EMAIL_TRANSFER_PROTOCOL = "smtps";
    public static final String USERNAME = "zzhnvipzz@gmail.com";
    public static final String PASSWORD = "tfzabxbwjkjphsky";
    public static final String FROM_EMAIL = "nguyenhv236@gmail.com";
    public static final String CC_EMAIL = "";
    public static final String EMAIL_SUBJECT = "HN server - New Password";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 465;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    /* ----->>>  Constant for Email END <<<-----*/

    /* ----->>>  Content for mail START <<<-----*/
    public static final String CONTENT_NEW_PASSWORD = "Hello %s \n \n Your new account password is: %s \n \n The Support Team";
    public static final String CONTENT_RESET_PASSWORD = "Hello %s \n \n Your password has been reset. Please don't share it: %s \n \n The Support Team";
    /* ----->>>  Content for mail END <<<-----*/

    /* ----->>>  File constant START <<<-----*/
    public static final String USER_IMAGE_PATH = "/use/image/";
    public static final String JPG_EXTENSION = ".jpg";
    public static final String USER_FOLDER = System.getProperty("user.home") + "/supportportal/user/";
    public static final String DIRECTORY_CREATED = "Created directory for: ";
    public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/";
    public static final String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name: ";
    public static final String FORWARD_SLASH = "/";
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
    public static final String TEMP_PROFILE_IMAGE_CAT_STYLE = "?set=set4";
    /* ----->>>  File constant END <<<-----*/


}
