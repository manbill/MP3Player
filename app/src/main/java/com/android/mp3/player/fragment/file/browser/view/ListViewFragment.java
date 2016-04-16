package com.android.mp3.player.fragment.file.browser.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.log.MyLog;
import com.android.lib.tools.perferences.PreferencesUtils;
import com.android.mp3.player.R;
import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.manager.NewsManager;
import com.android.mp3.player.process.StartProcess;
import com.android.mp3.player.util.StaticFields;
import com.android.mp3.player.util.StaticMethods;
import com.java.lib.oil.GlobalMethods;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class ListViewFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ListViewFragment";

    private static final String KEY_FILE_PATH = "file_path";

    private FileListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        ListView list = (ListView) view.findViewById(R.id.file_list);
        if (list != null) {
            if (list.getHeaderViewsCount() == 0) {
                list.addHeaderView(inflater.inflate(R.layout.fragment_list_view_list_header, list, false));
            }
            if (mAdapter == null) {
                mAdapter = new FileListAdapter(inflater, PreferencesUtils.getInstance().readStringFromPreferences(getActivity(), null, StaticFields.KEY.REMEMBERED_FOLDER, "/"));
            }
            if (savedInstanceState != null) {
                mAdapter.setFilePath(savedInstanceState.getString(KEY_FILE_PATH, "/"));
            }
            TextView listHeaderText = (TextView) list.findViewById(R.id.list_header);
            if (listHeaderText != null) {
                listHeaderText.setText(mAdapter.getFilePath());
            }
            mAdapter.setInflater(inflater);

            list.setOnItemClickListener(this);

            list.setAdapter(mAdapter);
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
                    ListView list = (ListView) getView().findViewById(R.id.file_list);
                    if (list != null) {
                        TextView listHeaderText = (TextView) list.findViewById(R.id.list_header);
                        if (listHeaderText != null) {
                            listHeaderText.setText(mAdapter.getFilePath());
                        }
                    }
                }
            }
            else if (GlobalMethods.getInstance().checkEqual(sMsg, "browser_confirm")) {
                if (mAdapter != null) {
                    PreferencesUtils.getInstance().writeStringToPreferences(getActivity(), null, StaticFields.KEY.REMEMBERED_FOLDER, mAdapter.getFilePath());
                    if (new File(mAdapter.getFilePath()).isDirectory()) {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PROGRAM_FRAGMENT);
                    }
                    else {
                        MessageManager.getInstance().sendEmptyMessage(StartProcess.class, StaticFields.MSG.PROCESS_EXECUTE_SHOW_PLAY_FRAGMENT);
                    }
                }
            }
        }
        return NewsManager.NewsResult.CONTINUE;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null) {
            view.setSelected(true);
            File file = (File) mAdapter.getItem(position - 1);
            if (file != null && file.isDirectory()) {
                mAdapter.setFilePath(file.getAbsolutePath());

                TextView listHeaderText = (TextView) parent.findViewById(R.id.list_header);
                if (listHeaderText != null) {
                    listHeaderText.setText(mAdapter.getFilePath());
                }
            }
        }
    }

    private static class FileListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private String filePath;
        private ArrayList<File> listItems;

        public FileListAdapter(LayoutInflater inflater, String filePath) {
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
                convertView = this.inflater.inflate(R.layout.fragment_list_view_list_item, parent, false);
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
