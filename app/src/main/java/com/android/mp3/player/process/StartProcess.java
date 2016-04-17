package com.android.mp3.player.process;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.log.MyLog;
import com.android.lib.tools.perferences.PreferencesUtils;
import com.android.mp3.player.data.MusicFile;
import com.android.mp3.player.fragment.file.FileFragment;
import com.android.mp3.player.fragment.load.LoadingFragment;
import com.android.mp3.player.manager.PageManager;
import com.android.mp3.player.thread.MP3QueryThread;
import com.android.mp3.player.util.StaticFields;
import com.java.lib.oil.json.JSON;

import java.util.List;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class StartProcess {
    private static final String TAG = "StartProcess";

    private static StartProcess mInstance;

    @NonNull
    public static StartProcess getInstance(Context context) {
        if (mInstance == null) {
            synchronized (StartProcess.class) {
                if (mInstance == null) {
                    mInstance = new StartProcess();
                }
            }
        }
        mInstance.setContext(context);
        if (!MessageManager.getInstance().containsHandler(StartProcess.class)) {
            MessageManager.getInstance().addHandler(StartProcess.class, new StartProcessHandler(mInstance));
        }
        return mInstance;
    }

    private StartProcess() {

    }

    private Context context;

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        if (context != null) {
            this.context = context;
        }
    }

    public void start() {
        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT);
        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_START);
    }

    private static class StartProcessHandler extends MessageManager.BaseHandler<StartProcess> {

        public StartProcessHandler(StartProcess process) {
            super(process);
        }

        @Override
        protected boolean validHost(StartProcess process) {
            return process != null && process.getContext() != null;
        }

        @Override
        public void handleMessage(Message msg) {
            MyLog.i(TAG, "handleMessage", "receive new msg, what: " + StaticFields.MSG.msg2String(msg.what));
            StartProcess process = getHost();
            if (process != null) {
                if (msg.what == StaticFields.MSG.SHOW_STRING_TOAST) {
                    Toast.makeText(process.getContext(), String.valueOf(msg.obj), msg.arg1 == 0 ? Toast.LENGTH_LONG : msg.arg1).show();
                }
                else if (msg.what == StaticFields.MSG.SHOW_INT_TOAST) {
                    Toast.makeText(process.getContext(), (Integer) msg.obj, msg.arg1 == 0 ? Toast.LENGTH_LONG : msg.arg1).show();
                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_START) {
                    new MP3QueryThread(process.getContext()).start();
                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_QUERY_SYSTEM_AUDIO_FILE_END) {
                    MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_CHECK_PLAY_FILE);
                }
                else if (msg.what == StaticFields.MSG.PROCESS_CHECK_PLAY_FILE) {
                    String sPlayFile = PreferencesUtils.getInstance().readStringFromPreferences(process.getContext(), null, StaticFields.KEY.REMEMBERED_FILE, null);
                    if (sPlayFile == null) {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_CHECK_PLAY_LIST);
                    }
                    else {
                        MusicFile playFile = JSON.parseObject(sPlayFile, MusicFile.class);
                        if (playFile == null) {
                            MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_CHECK_PLAY_LIST);
                        }
                        else {
                            MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT);
                        }
                    }
                }
                else if (msg.what == StaticFields.MSG.PROCESS_CHECK_PLAY_LIST) {
                    String sPlayList = PreferencesUtils.getInstance().readStringFromPreferences(process.getContext(), null, StaticFields.KEY.PLAY_LIST, null);
                    if (sPlayList == null) {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_CHECK_REMEMBERED_FOLDER);
                    }
                    else {
                        List<MusicFile> playList = JSON.parseArray(sPlayList, MusicFile.class);
                        if (playList == null || playList.isEmpty()) {
                            MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_CHECK_REMEMBERED_FOLDER);
                        }
                        else {
                            MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT);
                        }
                    }
                }
                else if (msg.what == StaticFields.MSG.PROCESS_CHECK_REMEMBERED_FOLDER) {
                    String rememberedFolder = PreferencesUtils.getInstance().readStringFromPreferences(process.getContext(), null, StaticFields.KEY.REMEMBERED_FOLDER, null);
                    if (rememberedFolder == null) {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_FILE_FRAGMENT);
                    }
                    else {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT);
                    }
                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_SHOW_LOADING_FRAGMENT) {
                    PageManager.getInstance().showPage(LoadingFragment.class, false);
                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_SHOW_FILE_FRAGMENT) {
                    PageManager.getInstance().showPage(FileFragment.class, false);
                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT) {

                }
                else if (msg.what == StaticFields.MSG.PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT) {

                }
            }
        }
    }
}
