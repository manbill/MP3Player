package com.java.lib.oil.file;

import com.java.lib.oil.ResultHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by wb-duanyongyu on 2016/2/3.
 */
public class FileUtils {
    private static FileUtils mInstance;

    public static FileUtils getInstance() {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils();
                }
            }
        }
        return mInstance;
    }

    private static final String TAG = "FileUtils";

    private FileUtils() {

    }

    public byte[] readFileToRawByteArray(String filePath) {
        return readFileToRawByteArray(filePath, 8192, null);
    }

    public byte[] readFileToRawByteArray(String filePath, int len, ResultHandler callback) {
        return readFileToRawByteArray(new File(filePath), len, callback);
    }

    public byte[] readFileToRawByteArray(File file, int len, ResultHandler callback) {
        if (file != null && file.exists()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                InputStream in = new FileInputStream(file);
                byte[] buffer = new byte[len];
                int bytesRead;
                while((bytesRead = in.read(buffer, 0, len)) != -1) {
                    if(bytesRead > 0) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
                in.close();
                bos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                if (callback != null) {
                    callback.onFail("FileUtils.readFileToRawByteArray", e, file, callback);
                }
            }
        }
        return null;
    }

    public String readFileToString(String filePath) {
        return readFileToString(filePath, System.getProperty("file.encoding"), 8192, null);
    }

    public String readFileToString(String filePath, String charset, int len, ResultHandler callback) {
        return readFileToString(filePath != null ? new File(filePath) : null, charset, len, callback);
    }

    public String readFileToString(File file) {
        return readFileToString(file, System.getProperty("file.encoding"), 8192, null);
    }

    public String readFileToString(File file, String charset, int len, ResultHandler callback) {
        if (file != null && file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                if(sb.length() > 0) {
                    return sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onFail("FileUtils.readFileToString", e, file, charset, len, callback);
                }
            }
        }
        return null;
    }

    public boolean writeStringToFile(String filePath, String text) {
        return writeStringToFile(new File(filePath), text);
    }

    public boolean writeStringToFile(String filePath, String text, String charset) {
        return writeStringToFile(new File(filePath), text, charset, false);
    }

    public boolean writeStringToFile(File file, String text) {
        return writeStringToFile(file, text, System.getProperty("file.encoding"), false);
    }

    public boolean writeStringToFile(File file, String text, String charset, boolean append) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
            writer.write(text);
            writer.flush();
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean appendStringToFile(File file, String text) {
        return appendStringToFile(file, text, System.getProperty("file.encoding"));
    }

    public boolean appendStringToFile(File file, String text, String charset) {
        return writeStringToFile(file, text, charset, true);
    }
}
