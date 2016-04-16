package com.android.lib.tools.log;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.lib.oil.OrderedList;
import com.java.lib.oil.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wb-duanyongyu on 2016/2/16.
 */
public class MyLog {
    protected static final String TAG = "MyLog";

    private static final Object lock = new Object();

    public static final int LOG_TO_CONSOLE = 1;
    public static final int LOG_TO_FILE = 2;
    public static final int LOG_TO_CONSOLE_AND_FILE = LOG_TO_CONSOLE | LOG_TO_FILE;

    private static boolean INFO = true;
    private static boolean ERROR = true;

    private static MyLogThread mThread;

    public static void startLogThread() {
        if (MyLog.mThread == null || MyLog.mThread.isStop) {
            MyLog.mThread = new MyLogThread();
            MyLog.mThread.start();
        }
    }

    public static void stopLogThread() {
        if (MyLog.mThread == null) {
            return;
        }
        MyLog.mThread.setStop();
    }

    public static void i(String TAG, String method, String message) {
        MyLog.i(MyLog.LOG_TO_CONSOLE_AND_FILE, MyLogThread.LogMessage.TYPE_I, TAG, method, message);
    }

    public static void i(int logTo, int type, String TAG, String method, String message) {
        startLogThread();
        MyLog.mThread.queue(logTo, type, TAG, method, message);
    }

    public static void e(String TAG, String method, String message) {
        MyLog.e(MyLog.LOG_TO_CONSOLE_AND_FILE, MyLogThread.LogMessage.TYPE_E, TAG, method, message);
    }

    public static void e(int logTo, int type, String TAG, String method, String message) {
        startLogThread();
        MyLog.mThread.queue(logTo, type, TAG, method, message);
    }

    public static class MyLogThread extends Thread {
        private boolean isStop;
        private FileWriteThread mFileWriteThread;

        public void setStop() {
            this.isStop = true;
            if (mFileWriteThread != null) {
                mFileWriteThread.stopThread();
            }
        }

        public MyLogThread() {

        }

        /**
         * there is no need to care anything about cpu or something about efficiency.
         * but you must be warned, that file log depends on /sdcard directory, and write permission of this directory.
         * if the directory does not exist or has no write permission, the program will crash which will cause the progress to crash.
         */
        public void enableFileLog() {
            mFileWriteThread = new FileWriteThread();
            mFileWriteThread.start();
        }

        @Override
        public void run() {
            while (!this.isStop) {
                if (this.messages == null || this.messages.isEmpty()) {
                    continue;
                }
                synchronized (MyLog.lock) {
                    LogMessage message = this.messages.remove(0);
                    if (message != null) {
                        if (message.type == LogMessage.TYPE_I) {
                            if (MyLog.INFO) {
                                if ((message.logTo & MyLog.LOG_TO_CONSOLE) != 0) {
                                    Log.i(message.TAG, message.method + ", " + message.msg);
                                }
                                if ((message.logTo & MyLog.LOG_TO_FILE) != 0) {
                                    if (mFileWriteThread != null) {
                                        mFileWriteThread.queue(message.toString() + System.getProperty("line.separator"));
                                    }
                                }
                            }
                        }
                        else if (message.type == LogMessage.TYPE_E) {
                            if (MyLog.ERROR) {
                                if ((message.logTo & MyLog.LOG_TO_CONSOLE) != 0) {
                                    Log.e(message.TAG, message.method + ", " + message.msg);
                                }
                                if ((message.logTo & MyLog.LOG_TO_FILE) != 0) {
                                    if (mFileWriteThread != null) {
                                        mFileWriteThread.queue(message.toString() + System.getProperty("line.separator"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private static class FileWriteThread extends Thread {
            private File logFile;
            private ArrayList<String> mArrayList;
            private boolean isStop;

            public FileWriteThread() {
                mArrayList = new  ArrayList<>();
                File sdcard = Environment.getExternalStorageDirectory();
                if (sdcard.exists()) {
                    this.logFile = new File(sdcard, "alimap_thread_" + new SimpleDateFormat("yyyy-MM-dd__HH_mm_ss", Locale.US).format(new Date()) + ".log");
                    try {
                        Log.i(TAG, "MyLogThread.MyLogThread, result of createNewFile: " + this.logFile.createNewFile());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void stopThread() {
                this.isStop = true;
            }

            public void queue(String line) {
                mArrayList.add(line);
            }

            public void run() {
                while (!this.isStop) {
                    while (!mArrayList.isEmpty()) {
                        FileUtils.getInstance().appendStringToFile(this.logFile, mArrayList.remove(0));
                    }
                }
            }
        }

        private OrderedList<LogMessage> messages;
        private void queue(int logTo, int type, String TAG, String method, String msg) {
            if (this.messages == null) {
                this.messages = new OrderedList<>(128, OrderedList.SORT_TYPE_ASC);
            }
            synchronized (MyLog.lock) {
                this.messages.add(new LogMessage(logTo, type, TAG, method, msg, System.currentTimeMillis()));
            }
        }

        public static class LogMessage implements Comparable<LogMessage> {
            public static final int TYPE_I = 0;
            public static final int TYPE_E = 2;

            private int logTo;
            private int type;
            private String TAG;
            private String method;
            private String msg;
            private long time;

            public LogMessage(int logTo, int type, String TAG, String method, String msg, long time) {
                this.logTo = logTo;
                this.type = type;
                this.TAG = TAG;
                this.method = method;
                this.msg = msg;
                this.time = time;
            }

            @Override
            public int compareTo(@NonNull LogMessage another) {
                return (int) (this.time - another.time);
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                if (this.type == TYPE_I) {
                    sb.append("I/");
                }
                else if (this.type == TYPE_E) {
                    sb.append("E/");
                }
                sb.append(TAG);
                sb.append(", ");
                sb.append(this.method);
                sb.append(", ");
                sb.append(msg);
                return sb.toString();
            }
        }
    }
}
