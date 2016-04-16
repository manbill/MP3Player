package com.java.lib.oil.lang;

import com.java.lib.oil.ResultHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static StringManager mInstance;

    public static StringManager getInstance() {
        if(mInstance == null) {
            synchronized(StringManager.class) {
                mInstance = new StringManager();
            }
        }
        return mInstance;
    }

    private StringManager() {

    }

    public Vector<String> search(String sou, String regex) {
        if(sou == null || sou.equals("")) {
            return null;
        }

        Vector<String> res = new Vector<>();

        if(regex == null || regex.equals("")) {
            res.add(sou);
            return res;
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sou);
        while(m.find()) {
            res.add(m.group());
        }

        if(res.size() == 0) {
            return null;
        }
        return res;
    }

    public Vector<String> search(String sou, String regex, int num) {
        if(sou == null || sou.equals("")) {
            return null;
        }

        Vector<String> res = new Vector<>();

        if(regex == null || regex.equals("")) {
            res.add(sou);
            return res;
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sou);
        for(int i = 0; i < num && m.find(); i++) {
            res.add(m.group());
        }

        return res;
    }

    public String searchSingleItem(String sou, String regex) {
        Vector<String> res = new Vector<>();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sou);
        while(m.find()) {
            res.add(m.group());
        }

        if(res.size() == 0) {
            return null;
        }
        return res.elementAt(0);
    }

    public boolean contains(String sou, String regex) {
        if (sou == null || sou.equals("")) {
            return false;
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sou);

        return m.find();
    }

    public String findClosedSubString(String content, String start, String end) {
        int start_index = content.indexOf(start);
        if(start_index == -1) {
            return null;
        }
        int end_index = content.indexOf(end);
        if(end_index < start_index) {
            throw new IllegalArgumentException("content is not propery closed.");
        }
        if(end_index == -1) {
            throw new IllegalArgumentException("can not find end string: " + end + " in your content: " + content);
        }
        int start_index_detect = content.indexOf(start, start_index + start.length());
        if(start_index_detect == -1 || start_index_detect > end_index + end.length()) {
            return content.substring(start_index, end_index + end.length());
        }
        int level = 1;
        while(level > 0) {
            if(start_index_detect > -1 && start_index_detect < end_index) {
                start_index_detect = content.indexOf(start, start_index_detect + start.length());
                if(start_index_detect > -1 && start_index_detect < end_index) {
                    ++level;
                    continue;
                }
            }
            else if(end_index > -1 && end_index < start_index_detect) {
                end_index = content.indexOf(end, end_index + end.length());
                if(end_index == -1 && level > 0) {
                    throw new IllegalArgumentException("content is not propery closed.");
                }
                if(end_index < start_index_detect) {
                    --level;
                }
                continue;
            }

            end_index = content.indexOf(end, end_index + end.length());
            if(end_index > -1 && (end_index < start_index_detect || start_index_detect == -1)) {
                --level;
                continue;
            }

            if(end_index == -1 && level > 0) {
                throw new IllegalArgumentException("content is not propery closed.");
            }
        }

        return content.substring(start_index, end_index + end.length());
    }

    public byte[] md5(byte[] source, ResultHandler callback) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] res = md.digest(source);
            if(callback != null) {
                callback.onSuccess("md5", res);
            }
            return res;
        }
        catch (NoSuchAlgorithmException exception) {
            if(callback != null) {
                callback.onFail("md5", exception);
            }
        }
        return null;
    }

    public String md5Hex(byte[] source) {
        if (source != null && source.length > 0) {
            StringBuffer sb = new StringBuffer(source.length * 2);
            for (int i = 0; i < source.length; ++i) {
                sb.append(DIGITS[(source[i] & 0xF0) >> 4]);
                sb.append(DIGITS[(source[i] & 0x0F)]);
            }
            return sb.toString();
        }
        return null;
    }
}
