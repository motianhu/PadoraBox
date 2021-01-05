package com.smona.tool.opendoor.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smona.tool.opendoor.R;

public class BleOpenDoorContainer extends RelativeLayout {

    private View bluetoothPhoneView;
    private View lockHandView;
    private View ripperAnimView;
    private TextView tipsTv;
    private TextView countDownTv;

    public static final int LOCKING = 1;
    public static final int LOCK_SUCCESS = 2;
    public static final int LOCK_FAILED = 3;

    public BleOpenDoorContainer(Context context) {
        super(context);
    }

    public BleOpenDoorContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BleOpenDoorContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bluetoothPhoneView = findViewById(R.id.bluetooth_phone);
        lockHandView = findViewById(R.id.lock_hand);
        ripperAnimView = findViewById(R.id.ripper_anim);
        tipsTv = findViewById(R.id.open_door_tips);
        countDownTv = findViewById(R.id.count_down_tv);
    }

    public void executeUnLocking(int lock) {
        bluetoothPhoneView.setVisibility(lock == LOCK_SUCCESS ? View.GONE : View.VISIBLE);
        lockHandView.setRotation(lock != LOCK_SUCCESS ? 0 : 30);
        lockHandView.setPivotX(30);
        lockHandView.setPivotY(0);
        ripperAnimView.setVisibility(lock != LOCKING ? View.GONE : View.VISIBLE);
        int resId = R.string.open_bluetooth_tips;
        if(lock == LOCK_SUCCESS) {
            resId = R.string.open_bluetooth_tips_success;
        } else  if(lock == LOCK_FAILED) {
            resId = R.string.open_bluetooth_tips_failed;
        }
        tipsTv.setText(resId);
        countDownTv.setVisibility(lock != LOCKING ? View.GONE : View.VISIBLE);
        countDownTv.setText(R.string.open_bluetooth_tips_unlocking);
    }
}
