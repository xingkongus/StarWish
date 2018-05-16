package us.xingkong.starwishingbottle.base;

import android.Manifest;

public class Constants {

    /**
     * debug 5425615547dbb709dffc6f993f1e9877
     *
     * release a9b0fbb67cff96d2113f9058850a5b3a
     */
    public static final String ApplicationID = "5425615547dbb709dffc6f993f1e9877";

    public static final String[] PERMISSIONS_EXTERNAL_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
//            ,Manifest.permission.READ_PHONE_STATE
    };

    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    /**
     * debug 51357a5c53
     *
     * release 04f8e0c1f5
     */
    public static final String APP_ID = "51357a5c53";
}
