package com.mcmo.mcmo3d.gl.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by ZhangWei on 2016/11/17.
 */

public class MultiGestureDetector {
    private int mTouchSlopSquare;

    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();

    private float mLastFocusX;
    private float mLastFocusY;
    private float mDownFocusX;
    private float mDownFocusY;

    private boolean mInProgress = true;
    private float mCurrSpan;
    private float mPrevSpan;
    private float mInitialSpan;
    private float mCurrSpanX;
    private float mCurrSpanY;
    private float mPrevSpanX;
    private float mPrevSpanY;

    private float mCurrK;
    private float mPrevK;
    private float mInitialK;

    private MotionEvent mCurrentDownEvent;

    private boolean mStillDown;
    private boolean mAlwaysInTapRegion;

    private boolean mScaleRotateUseful = true;


    private final int WHAT_TAP = 1;
    private Handler mHandler;

    private MultiGestureListener mListener;

    public MultiGestureDetector(Context context, MultiGestureListener listener) {
        mHandler = new GestureListener();
        this.mListener = listener;
        init(context);
    }

    public void useScaleRotate(boolean useful) {
        this.mScaleRotateUseful = useful;
    }

    public boolean isScaleRotateUseful() {
        return mScaleRotateUseful;
    }

    private void init(Context context) {
        //TO-DO 没有监听报异常
        int touchSlop;
        if (context == null) {
            touchSlop = ViewConfiguration.getTouchSlop();
        } else {
            final ViewConfiguration configuration = ViewConfiguration.get(context);
            touchSlop = configuration.getScaledTouchSlop();
        }
        mTouchSlopSquare = touchSlop * touchSlop;
    }

    /**
     * 获取两次触摸事件之间的缩放比例
     * @return
     */
    public float getScaleFactor() {
        return mPrevSpan > 0 ? mCurrSpan / mPrevSpan : 1;
    }

    /**
     * 获取从缩放开始到本次触摸事件的缩放比例
     * @return
     */
    public float getScaleFactorAll() {
        return mInitialSpan > 0 ? mCurrSpan / mInitialSpan : 1;
    }

    /**
     * 获取两次触摸事件之间的旋转角度(degree)
     * @return
     */
    public float getRotateFactor() {
        boolean curNaN = Float.isNaN(mCurrK);
        boolean preNaN = Float.isNaN(mPrevK);
        if (curNaN && preNaN) {
            return 0;
        }
        if (curNaN || preNaN) {
            if (curNaN) {
                curNaN = mPrevK > 0;
            }
            if (preNaN) {
                preNaN = mCurrK > 0;
            }
            float currAngle = Float.isNaN(mCurrK) ? curNaN ? 90 : -90 : (float) Math.toDegrees(Math.atan(mCurrK));
            float prevAngle = Float.isNaN(mPrevK) ? preNaN ? 90 : -90 : (float) Math.toDegrees(Math.atan(mPrevK));
            return currAngle - prevAngle;
        } else {
            float o = (mCurrK - mPrevK) / (1 + mCurrK * mPrevK);
            return (float) Math.toDegrees(Math.atan(o));
        }
    }

    /**
     * 获取从缩放开始到本次触摸事件的旋转角度(degree)
     * @return
     */
    public float getRotateFactorAll() {
        float currAngle = Float.isNaN(mCurrK) ? 90 : (float) Math.toDegrees(Math.atan(mCurrK));
        float initAngle = Float.isNaN(mInitialK) ? 90 : (float) Math.toDegrees(Math.atan(mInitialK));
        return currAngle - initAngle;
    }

    /**
     * 获取两指间距离
     * @return
     */
    public float getCurrSpan() {
        return mCurrSpan;
    }

    /**
     * 获取上一次移动是两指间距离
     * @return
     */
    public float getPrevSpan() {
        return mPrevSpan;
    }

    /**
     * 获取两指操作开始时两指间距离
     * @return
     */
    public float getInitialSpan() {
        return mInitialSpan;
    }

    /**
     * 获取两指间水平方向距离
     * @return
     */
    public float getCurrSpanX() {
        return mCurrSpanX;
    }

    /**
     * 获取两指间垂直方向距离
     * @return
     */
    public float getCurrSpanY() {
        return mCurrSpanY;
    }

    /**
     * 获取上一次触摸事件两指间水平距离
     * @return
     */
    public float getPrevSpanX() {
        return mPrevSpanX;
    }

    /**
     * 获取上一次触摸事件两指间垂直距离
     * @return
     */
    public float getPrevSpanY() {
        return mPrevSpanY;
    }

    /**
     * 获取两指间连线的斜率
     * @return
     */
    public float getCurrK() {
        return mCurrK;
    }

    /**
     * 获取上一次两指间连线的斜率
     * @return
     */
    public float getPrevK() {
        return mPrevK;
    }

    /**
     * 获取两指事件触发是两指连线的斜率
     * @return
     */
    public float getInitialK() {
        return mInitialK;
    }

    private void clearTap() {
        mHandler.removeMessages(WHAT_TAP);
        mAlwaysInTapRegion = false;
    }

    private void clearDoublePointer() {
        mInProgress = false;
        mCurrSpan = 0;
        mPrevSpan = 0;
        mInitialSpan = 0;
        mCurrSpanX = 0;
        mCurrSpanY = 0;
        mPrevSpanX = 0;
        mPrevSpanY = 0;
        mInitialK = 0;
        mCurrK = 0;
        mPrevK = 0;

    }

    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final boolean pointerUp = (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? ev.getActionIndex() : -1;
        float sumX = 0, sumY = 0;
        final int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;
            sumX += ev.getX(i);
            sumY += ev.getY(i);
        }
        final int div = pointerUp ? count - 1 : count;
        final boolean isDoublePointer = div == 2;//是否是双指
        final float focusX = sumX / div;
        final float focusY = sumY / div;

        boolean handled = false;

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                boolean hadTapMessage = mHandler.hasMessages(WHAT_TAP);
                if (hadTapMessage) mHandler.removeMessages(WHAT_TAP);
                if (count == 1) {
                    mHandler.sendEmptyMessageDelayed(WHAT_TAP, TAP_TIMEOUT);
                }
                clearDoublePointer();
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(ev);
                mAlwaysInTapRegion = true;
                mStillDown = true;
                handled |= mListener.onDown(ev);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                clearTap();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mInProgress) {
                    mListener.onScaleRotateEnd(this);
                }
                clearDoublePointer();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDoublePointer && mScaleRotateUseful) {
                    float devSumX = 0, devSumY = 0;
                    for (int i = 0; i < count; i++) {
                        if (skipIndex == i) continue;
                        devSumX += Math.abs(ev.getX(i) - focusX);
                        devSumY += Math.abs(ev.getY(i) - focusY);
                    }
                    final float devX = devSumX / div;
                    final float devY = devSumY / div;
                    final float spanX = devX * 2;
                    final float spanY = devY * 2;
                    final float span = (float) Math.hypot(spanX, spanY);

                    final float deltaX = ev.getX(0) - ev.getX(1);
                    final float deltaY = ev.getY(0) - ev.getY(1);
                    float k = deltaX == 0 ? Float.NaN : deltaY / deltaX;

                    if (!mInProgress) {
                        mInProgress = true;
                        mInitialSpan = span;
                        mPrevSpanX = mCurrSpanX = spanX;
                        mPrevSpanY = mCurrSpanY = spanY;
                        mPrevSpan = mCurrSpan = span;
                        mInitialK = k;
                        mCurrK = k;
                        mPrevK = k;
                        mListener.onScaleRotateBegin(this);
                    } else {
                        mCurrSpanX = spanX;
                        mCurrSpanY = spanY;
                        mCurrSpan = span;
                        mCurrK = k;

                        boolean updatePrev = true;

                        if (mInProgress) {
                            updatePrev = mListener.onScaleRotate(this);
                        }

                        if (updatePrev) {
                            mPrevSpanX = mCurrSpanX;
                            mPrevSpanY = mCurrSpanY;
                            mPrevSpan = mCurrSpan;
                            mPrevK = k;
                        }
                    }
                } else {
                    final float scrollX = focusX - mLastFocusX;
                    final float scrollY = focusY - mLastFocusY;
                    if (mAlwaysInTapRegion) {
                        final int deltaX = (int) (focusX - mDownFocusX);
                        final int deltaY = (int) (focusY - mDownFocusY);
                        int distance = deltaX * deltaX + deltaY * deltaY;
                        if (distance > mTouchSlopSquare) {
                            mAlwaysInTapRegion = false;
                            mHandler.removeMessages(WHAT_TAP);
                            mLastFocusX=focusX;
                            mLastFocusY=focusY;
                        }
                    } else {
                        handled = mListener.onMove(mCurrentDownEvent, ev, scrollX, scrollY);
                        mLastFocusX = focusX;
                        mLastFocusY = focusY;
                    }
                }
                handled |= true;
                break;
            case MotionEvent.ACTION_UP:
                mStillDown = false;
                if (mAlwaysInTapRegion) {
                    handled |= mListener.onSingleTapUp(ev);
                } else {
                    handled |= mListener.onUp(ev);
                }
                clearDoublePointer();
                break;
            case MotionEvent.ACTION_CANCEL:
                clearTap();
                clearDoublePointer();
                break;
        }
        return handled;
    }

    private class GestureListener extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_TAP:
                    mAlwaysInTapRegion = false;
                    break;
            }
        }
    }

    public static class MultiGestureListener {
        public boolean onDown(MotionEvent ev) {
            return false;
        }

        public boolean onMove(MotionEvent down, MotionEvent ev, float scrollX, float scrollY) {
            return false;
        }

        public boolean onUp(MotionEvent ev) {
            return false;
        }

        public boolean onSingleTapUp(MotionEvent ev) {
            return false;
        }

        public boolean onScaleRotateBegin(MultiGestureDetector detector) {
            return false;
        }

        public boolean onScaleRotate(MultiGestureDetector detector) {
            return false;
        }

        public boolean onScaleRotateEnd(MultiGestureDetector detector) {
            return false;
        }
    }
}
