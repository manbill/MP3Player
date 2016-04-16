package com.android.mp3.player.util;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class StaticFields {
    public static final class MSG {
        public static final int SHOW_STRING_TOAST = 1000;
        public static final int SHOW_INT_TOAST = 1001;

        public static final int PROCESS_CHECK_PLAY_LIST = 1010;
        public static final int PROCESS_CHECK_REMEMBERED_FOLDER = 1011;
        public static final int PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT = 1012;
        public static final int PROCESS_EXECUTE_SHOW_FILE_FRAGMENT = 1013;
        public static final int PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT = 1014;
        public static final int PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT = 1015;

        public static String msg2String(int msg) {
            switch (msg) {
                case SHOW_STRING_TOAST:
                    return "SHOW_STRING_TOAST";
                case SHOW_INT_TOAST:
                    return "SHOW_INT_TOAST";
                case PROCESS_CHECK_PLAY_LIST:
                    return "PROCESS_CHECK_PLAY_LIST";
                case PROCESS_CHECK_REMEMBERED_FOLDER:
                    return "PROCESS_CHECK_REMEMBERED_FOLDER";
                case PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT:
                    return "PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT";
                case PROCESS_EXECUTE_SHOW_FILE_FRAGMENT:
                    return "PROCESS_EXECUTE_SHOW_FILE_FRAGMENT";
                case PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT:
                    return "PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT";
                case PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT:
                    return "PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT";
                default:
                    return "UNKNOWN: " + msg;
            }
        }
    }

    public static final class KEY {
        public static final String PLAY_LIST = "play_list";
        public static final String REMEMBERED_FOLDER = "remembered_folder";
        public static final String USER_CHOOSE_FILE_BROWSER = "user_choose_file_browser";
    }
}
