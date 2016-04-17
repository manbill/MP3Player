package com.android.mp3.player.thread;

import android.content.Context;
import android.os.Environment;

import com.android.lib.tools.device.media.audio.AudioHelper;
import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.thread.BaseThread;
import com.android.mp3.player.BuildConfig;
import com.android.mp3.player.R;
import com.android.mp3.player.process.StartProcess;
import com.android.mp3.player.util.StaticFields;
import com.java.lib.oil.cache.SingleTanPool;
import com.java.lib.oil.file.FileUtils;
import com.java.lib.oil.json.JSON;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liutiantian on 2016-04-17.
 */
public class MP3QueryThread extends BaseThread {
    public MP3QueryThread(Context context) {
        super(context);
    }

    @Override
    public void run() {
        SingleTanPool.getInstance().put(StaticFields.ID.SYSTEM_AUDIO_FILES, AudioHelper.getInstance().getAudioFiles(getContext()));

        write:
        if (BuildConfig.DEBUG) {
            if (Environment.getExternalStorageDirectory() != null) {
                ArrayList<AudioHelper.AudioFile> audioFiles = SingleTanPool.getInstance().get(StaticFields.ID.SYSTEM_AUDIO_FILES, null);
                if (audioFiles != null) {
                    MessageManager.getInstance().sendMessage(StartProcess.class, StaticFields.MSG.SHOW_STRING_TOAST, getContext().getString(R.string.mp3_query_thread_result_number, audioFiles.size()));
                    File pathFile = new File(Environment.getExternalStorageDirectory(), "MP3Player");
                    if (!pathFile.exists()) {
                        if (!pathFile.mkdirs()) {
                            break write;
                        }
                    }
                    FileUtils.getInstance().writeStringToFile(new File(pathFile, "audioFiles.log"), JSON.toJsonString(audioFiles, ArrayList.class));
                }
            }
        }
        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_END);
    }
}
