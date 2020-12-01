package com.smona.tool.opendoor.widget;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smona.tool.opendoor.IClickCallback;
import com.smona.tool.opendoor.R;

public class BleStartupContainer extends LinearLayout {

    private View stepOne;
    private View stepTwo;
    private View stepThree;
    private boolean statusOk = false;
    public static final int INIT = 1;
    public static final int PROCESS = 2;
    public static final int SUCCESS = 3;
    public static final int FAILED = 4;

    public BleStartupContainer(Context context) {
        super(context);
    }

    public BleStartupContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BleStartupContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        stepOne = findViewById(R.id.step_one);
        stepTwo = findViewById(R.id.step_two);
        stepThree = findViewById(R.id.step_three);
    }

    private IClickCallback callback;

    public void setOnCallback(IClickCallback callback) {
        this.callback = callback;
    }

    /**
     * 1--init
     * 2--success
     * 3--failed
     *
     * @param step
     */
    public void startStepOne(int step) {
        stepOne.findViewById(R.id.circle).setEnabled(true);
        stepOne.findViewById(R.id.divider_bottom).setEnabled(step > INIT);
        stepOne.findViewById(R.id.divider_top).setVisibility(View.INVISIBLE);
        TextView tv_title = stepOne.findViewById(R.id.tv_title);
        tv_title.setText("开启手机蓝牙");
        TextView tv_desc = stepOne.findViewById(R.id.tv_desc);
        String desc = "如果你已开启蓝牙，" + "<font color=\"#2C80FF\" size=15>" + "点击此处刷新" + "<\\font>";
        tv_desc.setText(Html.fromHtml(desc));
        tv_desc.setVisibility(step == FAILED ? View.VISIBLE : View.GONE);
        tv_desc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRefreshBluetooth();
            }
        });
        TextView tv_action = stepOne.findViewById(R.id.tv_action);
        tv_action.setText("去开启");
        tv_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter.getDefaultAdapter().enable();
            }
        });
        tv_action.setVisibility(step == FAILED ? View.VISIBLE : View.GONE);
        View action_ok = stepOne.findViewById(R.id.action_ok);
        action_ok.setVisibility(step == SUCCESS ? View.VISIBLE : View.GONE);
    }

    public void startStepTwo(int step) {
        stepTwo.findViewById(R.id.circle).setEnabled(step > INIT);
        TextView tv_title = stepTwo.findViewById(R.id.tv_title);
        tv_title.setText("获取蓝牙钥匙");
        tv_title.setEnabled(step > INIT);

        stepTwo.findViewById(R.id.divider_top).setVisibility(View.VISIBLE);
        stepTwo.findViewById(R.id.divider_bottom).setEnabled(step == SUCCESS);
        stepTwo.findViewById(R.id.divider_bottom).setEnabled(step == SUCCESS);

        TextView tv_desc = stepTwo.findViewById(R.id.tv_desc);
        tv_desc.setVisibility(View.GONE);
        TextView tv_action = stepTwo.findViewById(R.id.tv_action);
        tv_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRefreshMobileKeys();
            }
        });
        tv_action.setText("重新获取");
        tv_action.setVisibility(step == FAILED ? View.VISIBLE : View.GONE);
        View action_ok = stepTwo.findViewById(R.id.action_ok);
        action_ok.setVisibility(step == SUCCESS ? View.VISIBLE : View.GONE);
    }

    public void startStepThree(int step) {
        stepThree.findViewById(R.id.circle).setEnabled(step > INIT);
        TextView tv_title = stepThree.findViewById(R.id.tv_title);
        tv_title.setText("本人到店入住认证");
        tv_title.setEnabled(step > INIT);
        TextView tv_action = stepThree.findViewById(R.id.tv_action);
        tv_action.setText("去认证");
        tv_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_action.setVisibility(step == FAILED ? View.VISIBLE : View.GONE);
        View action_ok = stepThree.findViewById(R.id.action_ok);
        action_ok.setVisibility(step == SUCCESS ? View.VISIBLE : View.GONE);
        statusOk = step == SUCCESS;
    }

    public boolean isFinish() {
        return statusOk;
    }
}
