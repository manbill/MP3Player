package com.android.lib.tools.log;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wb-liutiantian.h on 2016/3/23.
 */
public class LogcatWriter {
    private static final String TAG = "LogcatWriter";
    private LogcatWriterThread writerThread;
    private File logFile;

    public LogcatWriter() {
        this(null);
    }

    public LogcatWriter(File logFile) {
        this.logFile = logFile;
    }

    public File getLogFile() {
        if (this.logFile == null) {
            File sdcard = Environment.getExternalStorageDirectory();
            if (sdcard != null) {
                File logDir = new File(sdcard, "log_files");
                if (!logDir.exists()) {
                    if (!logDir.mkdirs()) {
                        return null;
                    }
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.CHINA);
                this.logFile = new File(logDir, dateFormat.format(new Date()) + ".log");
            }
        }
        return this.logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public void startLog() {
        MyLog.i(TAG, "startLog", "this.writerThread != null && this.writerThread.isAlive(): " + (this.writerThread != null && this.writerThread.isAlive()));
        if (this.writerThread != null && this.writerThread.isAlive()) {
            return;
        }
        this.writerThread = new LogcatWriterThread(this);
        this.writerThread.start();
    }

    public void stopLog() {
        if (this.writerThread != null && this.writerThread.isAlive()) {
            this.writerThread.finish();
        }
    }

    private static class LogcatWriterThread extends Thread {
        private LogcatWriter logcatWriter;
        private boolean running;

        public LogcatWriterThread(LogcatWriter logcatWriter) {
            this.logcatWriter = logcatWriter;
        }

        @Override
        public synchronized void start() {
            this.running = true;
            super.start();
        }

        public synchronized void finish() {
            this.running = false;
        }

        @Override
        public void run() {
            MyLog.i(TAG, "LogcatWriterThread.run", "this.logcatWriter != null && this.logcatWriter.getLogFile() != null: " + (this.logcatWriter != null && this.logcatWriter.getLogFile() != null) + "; this.running: " + this.running);
            if (this.logcatWriter != null && this.logcatWriter.getLogFile() != null) {
                try {
                    try {
                        Runtime.getRuntime().exec("logcat -c").waitFor();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Process process = Runtime.getRuntime().exec("logcat -v time");

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.logcatWriter.getLogFile())));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    for (String line; this.running && (line = reader.readLine()) != null; ) {
                        writer.write(line + System.getProperty("line.separator"));
                    }
                    writer.flush();
                    writer.close();
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
