package com.github.swipeback.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.swipeback.R;
import com.github.swipeback.utils.ActivityStack;
import com.github.swipeback.utils.ActivityUtils;

public class SwipeBackLayout extends FrameLayout {
    private ViewDragHelper mDragHelper;
    //注入的Activity
    private Activity mActivity;
    //DecorView的子View
    private View mContentView;
    //内容滑动时左偏移量
    private int mContentLeft;
    //滑动比例
    private float mScrollPercent;
    //不透明度
    private float mOpacity;
    //内容左边阴影
    private Drawable mShadow;
    //阴影可见区域
    private Rect mShadowRect;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShadow = ContextCompat.getDrawable(getContext(), R.drawable.bg_shadow_left);
        mShadowRect = new Rect();
        mDragHelper = ViewDragHelper.create(this, 1F, new Callback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mContentView != null) {
            mContentView.layout(mContentLeft, top, mContentLeft + mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawChild = child == mContentView;
        child.getHitRect(mShadowRect);
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (drawChild && mOpacity > 0F && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            mShadow.setBounds(mShadowRect.left - mShadow.getIntrinsicWidth(), mShadowRect.top, mShadowRect.left, mShadowRect.bottom);
            mShadow.setAlpha((int) (mOpacity * 255));
            mShadow.draw(canvas);
        }
        return ret;
    }

    /**
     * 添加到Activity中
     *
     * @param activity 注册Activity的对象
     */
    public void injectToActivity(Activity activity) {
        mActivity = activity;
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        //偷梁换柱，将DecorView中的子View移出
        decorView.removeView(decorChild);
        //子View设置背景为白色
        decorChild.setBackgroundColor(Color.WHITE);
        //把子视图添加到FrameLayout中
        addView(decorChild);
        setContentView(decorChild);
        //把当前视图添加到DecorView中去
        decorView.addView(this);
    }

    /**
     * 设置内容View
     *
     * @param view 从DecorView中获取的View
     */
    public void setContentView(View view) {
        mContentView = view;
    }

    /**
     * 获取上一个Activity布局
     *
     * @return 上一个Activity布局
     */
    public SwipeBackLayout getPreLayout() {
        return ((ISwipeBack) mActivity).getPreActivity().getPreLayout();
    }

    /**
     * 联动上一个Activity的布局，实现背景联动
     *
     * @param layout 上一个Activity的布局
     */
    private void linkagePreLayout(SwipeBackLayout layout) {
        if (layout != null) {
            float translationX = (float) (0.4 / 0.9 * (mScrollPercent - 0.9) * layout.getWidth());
            if (translationX > 0) {
                translationX = 0;
            }
            layout.setTranslationX(translationX);
        }
    }

    /**
     * 当Layout滑动距离不足时恢复Layout布局位置
     *
     * @param layout 上一个Activity的布局
     */
    private void recoveryPreLayout(SwipeBackLayout layout) {
        if (layout != null) {
            layout.setTranslationX(0);
        }
    }

    private class Callback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View view, int pointerId) {
            boolean edgeTouched = mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, pointerId);
            //当前Activity栈大于1的时候才可以滑动
            boolean canSwipe = ActivityStack.getInstance().getStackSize() > 1;
            if (edgeTouched && canSwipe) {
                ActivityUtils.convertToTranslucent(mActivity);
            }
            return canSwipe;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mScrollPercent >= 1F) {
                    mActivity.finish();
                } else {
                    recoveryPreLayout(getPreLayout());
                }
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mContentView) {
                mContentLeft = left;
                mScrollPercent = Math.abs((float) left / mContentView.getWidth());
                linkagePreLayout(getPreLayout());
                invalidate();
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int childWidth = releasedChild.getWidth();
            int left;
            if (mContentLeft >= (childWidth * 0.25)) {
                left = childWidth;
            } else {
                left = 0;
                recoveryPreLayout(getPreLayout());
            }
            mDragHelper.settleCapturedViewAt(left, 0);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mOpacity = 1 - mScrollPercent;
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    public interface ISwipeBack {
        /**
         * 获取上一个Activity布局
         *
         * @return 布局
         */
        SwipeBackLayout getPreLayout();

        /**
         * 获取上一个Activity布局
         *
         * @return ISwipeBack
         */
        ISwipeBack getPreActivity();
    }
}
