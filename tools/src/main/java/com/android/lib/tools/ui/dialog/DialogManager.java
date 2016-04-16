package com.android.lib.tools.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.java.lib.oil.GlobalMethods;
import com.java.lib.oil.json.JSON;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by liutiantian on 2016-03-19.
 */
public class DialogManager {
    private static DialogManager mInstance;

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    private ArrayList<SoftReference<ManagedDialog>> mDialogCache;

    private DialogManager() {
        mDialogCache = new ArrayList<>();
    }

    public boolean registerDialog(ManagedDialog dialog) {
        if (dialog == null) {
            return false;
        }
        if (mDialogCache.isEmpty()) {
            return mDialogCache.add(new SoftReference<>(dialog));
        }
        else {
            synchronized (DialogManager.class) {
                for (int i = 0; i < mDialogCache.size(); ++i) {
                    SoftReference<ManagedDialog> reference = mDialogCache.get(i);
                    if (reference == null) {
                        continue;
                    }
                    ManagedDialog value = reference.get();
                    if (value == null) {
                        continue;
                    }
                    if (GlobalMethods.getInstance().checkEqual(dialog, value)) {
                        return true;
                    }
                }
                return mDialogCache.add(new SoftReference<>(dialog));
            }
        }
    }

    public boolean unregisterDialog(ManagedDialog dialog) {
        if (mDialogCache.isEmpty()) {
            return true;
        }
        synchronized (DialogManager.class) {
            int dcs = mDialogCache.size();
            for (int i = 0; i < dcs; ++i) {
                SoftReference<ManagedDialog> reference = mDialogCache.get(i);
                if (reference == null) {
                    continue;
                }
                ManagedDialog value = reference.get();
                if (value == null) {
                    continue;
                }
                if (GlobalMethods.getInstance().checkEqual(dialog, value)) {
                    return mDialogCache.remove(reference);
                }
            }
            return true;
        }
    }

    public void broadcastOrder(String order) {
        if (mDialogCache.isEmpty() || order == null || order.isEmpty()) {
            return;
        }
        synchronized (DialogManager.class) {
            for (int i = 0; i < mDialogCache.size(); ++i) {
                SoftReference<ManagedDialog> reference = mDialogCache.get(i);
                if (reference == null) {
                    continue;
                }
                ManagedDialog value = reference.get();
                if (value == null) {
                    continue;
                }
                value.onDialogOrder(order);
            }
        }
    }

    public interface OnDialogOrderListener {
        String ORDER_DISMISS = "dismiss";
        String ORDER_CANCEL = "cancel";
        String ORDER_HIDE = "hide";
        boolean onDialogOrder(String order);
    }

    public static class ManagedDialog extends Dialog implements OnDialogOrderListener {
        public ManagedDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            DialogManager.getInstance().registerDialog(this);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            DialogManager.getInstance().unregisterDialog(this);
        }

        @Override
        public boolean onDialogOrder(String order) {
            if (order == null || order.isEmpty()) {
                return false;
            }

            if (GlobalMethods.getInstance().checkEqual(order, ORDER_CANCEL)) {
                cancel();
                return true;
            }
            else if (GlobalMethods.getInstance().checkEqual(order, ORDER_DISMISS)) {
                dismiss();
                return true;
            }
            else if (GlobalMethods.getInstance().checkEqual(order, ORDER_HIDE)) {
                hide();
                return true;
            }
            return false;
        }
    }

    public abstract static class ManagedOrder implements Serializable {
        private String order;

        public ManagedOrder(String order) {
            this.order = order;
        }

        public String getOrder() {
            return this.order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public abstract String getSender();
    }
}
