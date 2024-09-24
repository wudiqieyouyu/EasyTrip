package com.easytrip.entity.Constants;

public class Constants {
    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer LENGTH_5 = 5;
    public static final Integer LENGTH_8 = 8;
    public static final Integer LENGTH_20 = 20;
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_15 = 15;
    public static final Integer LENGTH_30 = 30;
    public static final Integer LENGTH_200 = 200;
    public static final Integer LENGTH_190 = 190;
    public static final Long MINUTE_15 = 900000l;
    public static final Long MINUTE_5 = 300000l;
    public static final String SESSION_KEY = "session_key";

    public static final String JWT_KEY_LOGIN_TOKEN = "jwt_login_token_key";

    public static final Integer JWT_TOKEN_EXPIRES_DAYS = 7;
    //管理后台的sessionkey
    public static final String CHECK_CODE_KEY = "check_code_key";

    public static final String REDIS_KEY_CHECKCODE_PHONE = "easytrip:check:phone:";
    public static final String REDIS_KEY_CHECKCODE = "easytrip:check:";
    public static final Integer CHECK_CODE_TYPE_REGISTER = 0;
    public static final Integer CHECK_CODE_TYPE_LOGIN = 1;
    public static final Integer CHECK_CODE_TYPE_RESET = 2;
    public static final String FILE_FOLDER_FILE = "/file/";
    public static final String FILE_FOLDER_TEMP = "/temp/";
    public static final String FILE_FOLDER_IMAGE = "/image/";

    public static final String FILE_FOLDER_AVATAR_NAME = "avatar/";
    public static final String AVATAR_SUFFIX = ".jpg";
    public static final String AVATAR_DEFAULT = "default_avatar.jpg";

    public static final Integer DEFUALT_ROOT_MENU = 0;
    public static final Integer FEEDBACK_TYPE = 5;

    public static final String NO_ADDRESS="未知";

    public static final String[] IMAGE_SUFFIX = {".jpg",".JPG",".jpeg",".JPEG",".png",".PNG",".gif",".GIF",".bmp",".BMP"};

    public static final String READ_IMAGE_PATH="/api/file/getImage/";
}
