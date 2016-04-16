package com.android.mp3.player.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class StaticMethods {
    private static StaticMethods mInstance;

    public static StaticMethods getInstance() {
        if (mInstance == null) {
            synchronized (StaticMethods.class) {
                if (mInstance == null) {
                    mInstance = new StaticMethods();
                }
            }
        }
        return mInstance;
    }

    private StaticMethods() {

    }

    public ArrayList<File> listFiles(File parent) {
        if (parent == null || !parent.exists()) {
            return null;
        }
        if (parent.isFile()) {
            throw new IllegalArgumentException("listFiles method in StaticMethods only receive Directory File as it argument.");
        }
        File[] children = parent.listFiles();
        if (children == null || children.length == 0) {
            return null;
        }
        ArrayList<File> dirs = new ArrayList<>(children.length);
        for (int i = 0; i < children.length; ++i) {
            if (children[i].isDirectory()) {
                dirs.add(children[i]);
            }
        }
        Collections.sort(dirs);

        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < children.length; ++i) {
            if (children[i].isFile()) {
                files.add(children[i]);
            }
        }
        Collections.sort(files);

        dirs.addAll(files);

        return dirs;
    }
}
