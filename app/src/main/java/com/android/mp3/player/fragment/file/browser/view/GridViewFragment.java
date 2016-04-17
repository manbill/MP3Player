package com.android.mp3.player.fragment.file.browser.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.perferences.PreferencesUtils;
import com.android.mp3.player.R;
import com.android.mp3.player.data.MusicFile;
import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.manager.NewsManager;
import com.android.mp3.player.process.StartProcess;
import com.android.mp3.player.util.StaticFields;
import com.android.mp3.player.util.StaticMethods;
import com.java.lib.oil.GlobalMethods;
import com.java.lib.oil.json.JSON;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class GridViewFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private static final String KEY_FILE_PATH = "file_path";

    private FileGridAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_view, container, false);

        GridView grid = (GridView) view.findViewById(R.id.file_grid);
        if (grid != null) {
            if (mAdapter == null) {
                mAdapter = new FileGridAdapter(inflater, PreferencesUtils.getInstance().readStringFromPreferences(getActivity(), null, StaticFields.KEY.REMEMBERED_FOLDER, "/"));
            }
            if (savedInstanceState != null && savedInstanceState.getString(KEY_FILE_PATH) != null) {
                mAdapter.setFilePath(savedInstanceState.getString(KEY_FILE_PATH));
            }
            TextView gridHeaderText = (TextView) view.findViewById(R.id.grid_header);
            if (gridHeaderText != null) {
                gridHeaderText.setText(mAdapter.getFilePath());
            }

            mAdapter.setInflater(inflater);

            grid.setOnItemClickListener(this);

            grid.setAdapter(mAdapter);
        }

        return view;
    }

    @Override
    public NewsManager.NewsResult onNewsArrival(String sMsg, Object oMsg) {
        if (getView() != null) {
            if (GlobalMethods.getInstance().checkEqual(sMsg, "browser_back")) {
                if (mAdapter != null) {
                    if (GlobalMethods.getInstance().checkEqual(mAdapter.getFilePath(), "/")) {
                        return NewsManager.NewsResult.ABORT;
                    }
                    mAdapter.setFilePath(new File(mAdapter.getFilePath()).getParent());
                    TextView gridHeaderText = (TextView) getView().findViewById(R.id.grid_header);
                    if (gridHeaderText != null) {
                        gridHeaderText.setText(mAdapter.getFilePath());
                    }
                }
            }
            else if (GlobalMethods.getInstance().checkEqual(sMsg, "browser_confirm")) {
                if (mAdapter != null) {
                    PreferencesUtils.getInstance().writeStringToPreferences(getActivity(), null, StaticFields.KEY.REMEMBERED_FOLDER, mAdapter.getFilePath());
                    MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT);
                }
            }
        }
        return NewsManager.NewsResult.CONTINUE;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null && getView() != null) {
            File file = (File) mAdapter.getItem(position);
            if (file != null) {
                if (file.isDirectory()) {
                    mAdapter.setFilePath(file.getAbsolutePath());

                    TextView gridHeaderText = (TextView) getView().findViewById(R.id.grid_header);
                    if (gridHeaderText != null) {
                        gridHeaderText.setText(mAdapter.getFilePath());
                    }
                }
                else {
                    PreferencesUtils.getInstance().writeStringToPreferences(getActivity(), null, StaticFields.KEY.REMEMBERED_FILE, JSON.toJsonString(new MusicFile(file.getAbsolutePath(), 0), MusicFile.class));
                    MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT);
                }
            }
        }
    }

    private static class FileGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private String filePath;
        private ArrayList<File> listItems;

        public FileGridAdapter(LayoutInflater inflater, String filePath) {
            this.inflater = inflater;
            this.filePath = filePath;
            onNewFilePath();
        }

        public LayoutInflater getInflater() {
            return this.inflater;
        }

        public void setInflater(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
            onNewFilePath();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.listItems != null ? this.listItems.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return this.listItems != null ? this.listItems.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.fragment_grid_view_grid_item, parent, false);
            }

            File file = this.listItems.get(position);
            if (file != null && file.exists()) {
                ImageView typeIconImage = (ImageView) convertView.findViewById(R.id.type_icon);
                if (typeIconImage != null) {
                    if (file.isFile()) {
                        typeIconImage.setBackgroundResource(R.mipmap.fragment_list_view_list_item_file_icon);
                    }
                    else {
                        typeIconImage.setBackgroundResource(R.mipmap.fragment_list_view_list_item_dir_icon);
                    }
                }

                TextView fileNameText = (TextView) convertView.findViewById(R.id.file_name);
                if (fileNameText != null) {
                    fileNameText.setText(file.getName());
                }
            }

            return convertView;
        }

        private void onNewFilePath() {
            if (this.filePath != null) {
                File file = new File(this.filePath);
                if (file.exists()) {
                    this.listItems = StaticMethods.getInstance().listFiles(file);
                }
                else {
                    this.listItems = null;
                }
            }
        }
    }
}
