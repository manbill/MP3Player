package com.android.mp3.player.util;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class StaticFields {
    public static final class MSG {
        public static final int SHOW_STRING_TOAST = 1000;
        public static final int SHOW_INT_TOAST = 1001;

        public static final int PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_START = 1010;
        public static final int PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_END = 1011;
        public static final int PROCESS_CHECK_PLAY_FILE = 1012;
        public static final int PROCESS_CHECK_PLAY_LIST = 1013;
        public static final int PROCESS_CHECK_REMEMBERED_FOLDER = 1014;
        public static final int PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT = 1015;
        public static final int PROCESS_EXECUTE_SHOW_FILE_FRAGMENT = 1016;
        public static final int PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT = 1017;
        public static final int PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT = 1018;

        public static String msg2String(int msg) {
            switch (msg) {
                case SHOW_STRING_TOAST:
                    return "SHOW_STRING_TOAST";
                case SHOW_INT_TOAST:
                    return "SHOW_INT_TOAST";
                case PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_START:
                    return "PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_START";
                case PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_END:
                    return "PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_END";
                case PROCESS_CHECK_PLAY_FILE:
                    return "PROCESS_CHECK_PLAY_FILE";
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

    public static final class ID {
         public static final String SYSTEM_AUDIO_FILES = "system_audio_files";
    }

    public static final class KEY {
        public static final String PLAY_LIST = "play_list";
        public static final String REMEMBERED_FOLDER = "remembered_folder";
        public static final String REMEMBERED_FILE = "remembered_file";
        public static final String USER_CHOOSE_FILE_BROWSER = "user_choose_file_browser";
    }
}
