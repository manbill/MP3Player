package com.android.lib.tools.device.screen;

import android.app.KeyguardManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.android.lib.tools.R;

/**
 * Created by wb-liutiantian.h on 2016/3/15.
 */
public class ScreenManager {
    private static ScreenManager mInstance;

    public static ScreenManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ScreenManager.class) {
                if (mInstance == null) {
                    mInstance = new ScreenManager(context);
                }
            }
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private Context mContext;
    private ComponentName mScreenAdmin;
    private DevicePolicyManager mDeviceManager;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;

    private boolean isWakeLocked;
    private boolean isKeyguardLocked;

    private ScreenManager(Context context) {
        mContext = context;
        mScreenAdmin = new ComponentName(mContext, ScreenAdminReceiver.class);
        mDeviceManager = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        mPowerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, ScreenManager.class.getSimpleName());
        mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock(ScreenManager.class.getSimpleName());

        setWakeLocked(false);
        setKeyguardLocked(getKeyguardManager().isKeyguardLocked());

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, getScreenAdmin());
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getContext().getResources().getString(R.string.screen_manager_receiver_explanation));
        getContext().startActivity(intent);
    }

    public void lockNow() {
        if (!isKeyguardLocked()) {
            if (getKeyguardLock() != null) {
                getKeyguardLock().reenableKeyguard();
                setKeyguardLocked(true);
            }
        }

        if (isWakeLocked()) {
            if (getWakeLock() != null) {
                getWakeLock().release();
                setWakeLocked(false);
            }
        }

        if (mDeviceManager.isAdminActive(getScreenAdmin())) {
            getDeviceManager().lockNow();
        }
        else {
            ScreenAdminReceiver.lockRequested = true;
        }
    }

    public void unlockNow() {
        if (isKeyguardLocked()) {
            if (getKeyguardLock() != null) {
                getKeyguardLock().disableKeyguard();
                setKeyguardLocked(false);
            }
        }

        if (!isWakeLocked()) {
            if (getWakeLock() != null) {
                getWakeLock().acquire();
                setWakeLocked(true);
            }
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public ComponentName getScreenAdmin() {
        return mScreenAdmin;
    }

    public DevicePolicyManager getDeviceManager() {
        if (mDeviceManager == null) {
            if (getContext() == null) {
                return  null;
            }
            mDeviceManager = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        return mDeviceManager;
    }

    public PowerManager getPowerManager() {
        if (mPowerManager == null) {
            if (getContext() == null) {
                return  null;
            }
            mPowerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        }
        return mPowerManager;
    }

    public PowerManager.WakeLock getWakeLock() {
        if (mWakeLock == null) {
            if (getPowerManager() == null) {
                return null;
            }
            mWakeLock = getPowerManager().newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, ScreenManager.class.getSimpleName());
        }
        return mWakeLock;
    }

    public boolean isWakeLocked() {
        return this.isWakeLocked;
    }

    public void setWakeLocked(boolean isWakeLocked) {
        this.isWakeLocked = isWakeLocked;
    }

    public KeyguardManager getKeyguardManager() {
        if (mKeyguardManager == null) {
            if (getContext() == null) {
                return null;
            }
            mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        }
        return mKeyguardManager;
    }

    public KeyguardManager.KeyguardLock getKeyguardLock() {
        if (mKeyguardLock == null) {
            if (getKeyguardManager() == null) {
                return null;
            }
            mKeyguardLock = getKeyguardManager().newKeyguardLock(ScreenManager.class.getSimpleName());
        }
        return mKeyguardLock;
    }

    public boolean isKeyguardLocked() {
        return this.isKeyguardLocked;
    }

    public void setKeyguardLocked(boolean isKeyguardLocked) {
        this.isKeyguardLocked = isKeyguardLocked;
    }

    public static class ScreenAdminReceiver extends DeviceAdminReceiver {
        public static boolean lockRequested;

        public void onEnabled(Context paramContext, Intent paramIntent) {
            if (ScreenAdminReceiver.lockRequested) {
                if (ScreenManager.getInstance(null) != null) {
                    ScreenManager.getInstance(null).lockNow();
                }
                ScreenAdminReceiver.lockRequested = false;
            }
        }

        public void onDisabled(Context paramContext, Intent paramIntent) {

        }
    }
}
