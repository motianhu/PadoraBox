package com.smona.app.tools.module;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by motianhu on 5/13/17.
 */

public class BaseView extends View {
    protected float mCalibr;
    protected int mHeight;
    protected DisplayMetrics mMetrics = null;
    protected float mPixelPerMillimeter;
    protected int mWidth;

    public BaseView(Context paramContext, DisplayMetrics paramDisplayMetrics) {
        super(paramContext);
        mMetrics = paramDisplayMetrics;
        mCalibr = 1.0F;
    }

    public BaseView(Context paramContext, DisplayMetrics paramDisplayMetrics, float paramFloat) {
        super(paramContext);
        mMetrics = paramDisplayMetrics;
        mCalibr = paramFloat;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mPixelPerMillimeter = (mMetrics.ydpi * mCalibr / 25.4F);
    }
}
