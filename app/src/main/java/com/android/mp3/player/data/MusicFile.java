package com.android.mp3.player.data;

import com.java.lib.oil.GlobalMethods;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class MusicFile {
    private String filePath;
    private long position;

    public MusicFile() {
        this(null);
    }

    public MusicFile(String filePath) {
        this(filePath, 0);
    }

    public MusicFile(String filePath, long position) {
        this.filePath = filePath;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MusicFile) {
            MusicFile file = (MusicFile) obj;
            if (!GlobalMethods.getInstance().checkEqual(file.getFilePath(), getFilePath())) {
                return false;
            }
            if (!GlobalMethods.getInstance().checkEqual(file.getPosition(), getPosition())) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
