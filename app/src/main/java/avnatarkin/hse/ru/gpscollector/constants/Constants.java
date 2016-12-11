package avnatarkin.hse.ru.gpscollector.constants;

public class Constants {

    public Constants() {
        // Required empty body constructor
    }

    public static final String USER_NAME = "userName";
    public static final String FIRST_TIME = "first_time";
    public static final String URL = "http://192.168.42.201:8080/SpringRest/rest/emp/create";
    public static final String URL_CREATE_USER = "http://192.168.42.201:8080/SpringRest/rest/user/create";
    public static final String TOKEN = "token";
    public static final String DATASOURCE_VARIABLE = "location";
    public static final String VARIABLE_ID = "variable";
    public static final String PUSH_TIME = "push_time";
    public static final String SYNC_TIME = "sync_time";
    public static final String SERVICE_RUNNING = "service_running";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String FILE_NAME = ".json";
    public static final String DERICTORY = "avnatarkin.hse.ru.gpscollector";
    public static final String LOCTABLE_PATH = "/data/avnatarkin.hse.ru.gpscollector/databases/loctable.db";

    public static final int NOTIFICATION_ID = 1337;


    public static class VARIABLE_DATABASE {
        public static final String ID_ROAD = "id_road";
        public static final String ROAD = "road";
        public static final String TIME = "time";
    }
}
