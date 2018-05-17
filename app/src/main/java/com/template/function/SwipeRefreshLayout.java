package com.template.function;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

import com.template.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * listview 上下刷新加载所需
 * **/
public class SwipeRefreshLayout extends ViewGroup {
    private static final String LOG_TAG = SwipeRefreshLayout.class.getSimpleName();

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final float MAX_SWIPE_DISTANCE_FACTOR = .6f;
    private static final int REFRESH_TRIGGER_DISTANCE = 120;//下拉120个像素刷新
    private static final int INVALID_POINTER = -1;
    private static final int DEFAULT_TYPE = 0x0;
    private static final int PULL_TO_REFRESH = 0x2;
    private static final int RELEASE_TO_REFRESH = 0x3;

    private SwipeProgressBar mProgressBar; // the thing that shows progress is
    // going
    private View mTarget; // the content that gets pulled down
    private int mOriginalOffsetTop;
    private int mOriginalOffsetTopBackup;
    private OnRefreshListener mListener;
    private OnLoadListener mLoadListener;
    private int mFrom;
    private boolean mRefreshing = false;
    private boolean tmpRefreshing = false;
    private boolean mLoading = false;
    private boolean tmpLoading = false;
    private int mTouchSlop;
    private float mDistanceToTriggerSync = -1;
    private int mMediumAnimationDuration;
    private int mProgreesBarWidth;
    private int mCurrentTargetOffsetTop;

    private float mInitialMotionY;
    private float mInitialMotionX;
    // private float mLastMotionY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    // private static final int[] LAYOUT_ATTRS = new int[] {
    // android.R.attr.state_enabled // state_enabled
    // };

    // customs
    private int headerHeight = -1;
    // private int screenWidth;
    public String defaultText = "下拉刷新";
    private String latestRefreshTime = "";
    private String latestLoadTime = "";
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    /**
     * 拖动方向
     */
    private int direction;

    private int lastDiff;

    private boolean up;
    private boolean down;

    private static final float DEFAULT_TIPS_TEXTSIZE = 18;
    private static final int DEFAULT_TEXT_COLOR = 0xFF808080;

    private int startImage, loadingImage, finishedImage;//加载完成立即显示图片
    private boolean pull2refresh;
    private boolean pull2load;
    private float textSize;// 文字大小
    private int textColor;// 文字颜色

    private static final String DEFAULT_PULL_DOWN_LABEL = "下拉刷新内容";
    private static final String DEFAULT_REFRESHING_LABEL = "正在刷新...";
    private static final String DEFAULT_RELEASE_DOWN_LABEL = "释放立即刷新";

    private static final String DEFAULT_PULL_UP_LABEL = "上拉加载更多";
    private static final String DEFAULT_LOADING_LABEL = "正在载入...";
    private static final String DEFAULT_RELEASE_UP_LABEL = "释放立即加载";

    private String pullDownLabel;
    private String refreshingLabel;
    private String releaseDownLabel;

    private String pullUpLabel;
    private String loadingLabel;
    private String releaseUpLabel;

    /**
     * 0:rotate 1:flip
     */
    private int type;

    private int state;

    private double trigger_angle;
    private final Animation mAnimateToStartPosition = new Animation() {

        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop = 0;
            if (mFrom != mOriginalOffsetTop) {
                targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
            }
            int offset = targetTop - mTarget.getTop();
            final int currentTop = mTarget.getTop();
            setTargetOffsetTopAndBottom(offset);
            setProgressBarProperty(currentTop);
        }
    };
    private Animation mShrinkTrigger = new Animation() {

        public void applyTransformation(float interpolatedTime, Transformation t) {
            setProgressBarProperty(mTarget.getTop());
        }
    };
    private final AnimationListener mReturnToStartPositionListener = new BaseAnimationListener() {

        public void onAnimationEnd(Animation animation) {
            if (isRefreshing()) {
                mCurrentTargetOffsetTop = headerHeight;
            } else if (mLoading) {
                mCurrentTargetOffsetTop = -headerHeight;
            } else {
                mCurrentTargetOffsetTop = 0;
            }
            lastDiff = 0;
            if (!tmpRefreshing) {
                mRefreshing = false;
            }
            if (!tmpLoading) {
                mLoading = false;
            }
        }
    };

    private final AnimationListener mShrinkAnimationListener = new BaseAnimationListener() {

        public void onAnimationEnd(Animation animation) {
        }
    };
    private final Runnable mReturnToStartPosition = new Runnable() {

        public void run() {
            mReturningToStart = true;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
        }
    };
    private final Runnable mCancel = new Runnable() {

        public void run() {
            mReturningToStart = true;
            if (mProgressBar != null) {
                mShrinkTrigger.setDuration(mMediumAnimationDuration);
                mShrinkTrigger.setAnimationListener(mShrinkAnimationListener);
                mShrinkTrigger.reset();
                mShrinkTrigger.setInterpolator(mDecelerateInterpolator);
                startAnimation(mShrinkTrigger);
            }
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
        }

    };

    // 构�?�函�?
    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
        setWillNotDraw(false);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        // screenWidth = metrics.widthPixels;
        trigger_angle = Math.atan((double) metrics.widthPixels / metrics.heightPixels);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.justice);
        // setEnabled(a.getBoolean(0, true));
        setEnabled(true);
        startImage = a.getResourceId(R.styleable.justice_startImage, R.mipmap.start);
        loadingImage = a.getResourceId(R.styleable.justice_loadingImage, R.mipmap.default_ptr_rotate);
        finishedImage = a.getResourceId(R.styleable.justice_finishedImage, R.mipmap.finished);
        pull2refresh = a.getBoolean(R.styleable.justice_ptr, true);
        pull2load = a.getBoolean(R.styleable.justice_ptl, true);
        textSize = a.getDimension(R.styleable.justice_textSize, DEFAULT_TIPS_TEXTSIZE * metrics.density);
        textColor = a.getColor(R.styleable.justice_textColor, DEFAULT_TEXT_COLOR);
        type = a.getInt(R.styleable.justice_animationStyle, DEFAULT_TYPE);
        pullDownLabel = a.getString(R.styleable.justice_pullDownLabel);
        refreshingLabel = a.getString(R.styleable.justice_refreshingLabel);
        releaseDownLabel = a.getString(R.styleable.justice_releaseDownLabel);
        pullUpLabel = a.getString(R.styleable.justice_pullUpLabel);
        loadingLabel = a.getString(R.styleable.justice_loadingLabel);
        releaseUpLabel = a.getString(R.styleable.justice_releaseUpLabel);
        if (null == pullDownLabel || pullDownLabel.equals("")) {
            pullDownLabel = DEFAULT_PULL_DOWN_LABEL;
        }
        if (null == refreshingLabel || refreshingLabel.equals("")) {
            refreshingLabel = DEFAULT_REFRESHING_LABEL;
        }
        if (null == releaseDownLabel || releaseDownLabel.equals("")) {
            releaseDownLabel = DEFAULT_RELEASE_DOWN_LABEL;
        }
        if (null == pullUpLabel || pullUpLabel.equals("")) {
            pullUpLabel = DEFAULT_PULL_UP_LABEL;
        }
        if (null == loadingLabel || loadingLabel.equals("")) {
            loadingLabel = DEFAULT_LOADING_LABEL;
        }
        if (null == releaseUpLabel || releaseUpLabel.equals("")) {
            releaseUpLabel = DEFAULT_RELEASE_UP_LABEL;
        }
        mProgressBar = new SwipeProgressBar(this, textSize, textColor, startImage, loadingImage, finishedImage, type);
        a.recycle();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(mCancel);
        removeCallbacks(mReturnToStartPosition);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mReturnToStartPosition);
        removeCallbacks(mCancel);
        mProgressBar.destory();
    }
    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
        mAnimateToStartPosition.setAnimationListener(listener);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mTarget.startAnimation(mAnimateToStartPosition);
    }
    // 提供外部接口,下拉刷新监听
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }
    // 提供外部接口,上啦加载监听
    public void setOnLoadListener(OnLoadListener mLoadListener) {
        this.mLoadListener = mLoadListener;
    }
    private void setProgressBarProperty(float diff) {
        if (direction == 1) {
            mProgressBar.setBounds(0, mTarget.getBottom() + mTarget.getPaddingBottom(), mProgreesBarWidth, mTarget.getBottom()
                    + mTarget.getPaddingBottom() + Math.abs((int) diff));
            mProgressBar.setProperty(defaultText, latestLoadTime, direction, state);

        } else {
            mProgressBar.setBounds(0, 0, mProgreesBarWidth, (int) diff);
            mProgressBar.setProperty(defaultText, latestRefreshTime, direction, state);
        }

        mProgressBar.setWidth(mProgreesBarWidth);
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing
     *            Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            tmpRefreshing = refreshing;
            if (refreshing) {
                mRefreshing = refreshing;
            }
            ensureTarget();
            if (refreshing) {
                mProgressBar.start();
                mReturnToStartPosition.run();
                latestRefreshTime = formatter.format(new Date());
                mProgressBar.setProcessingTips(refreshingLabel);
            } else {
                mOriginalOffsetTop = mOriginalOffsetTopBackup;

                mProgressBar.stop();
                postDelayed(mReturnToStartPosition, 500);
            }
        }
    }
    public void setLoading(boolean loading) {
        if (mLoading != loading) {
            tmpLoading = loading;
            if (loading) {
                mLoading = loading;
            }
            ensureTarget();
            if (loading) {
                mReturnToStartPosition.run();
                mProgressBar.start();
                mProgressBar.setProcessingTips(loadingLabel);
            } else {
                // mReturnToStartPosition.run();
                mOriginalOffsetTop = mOriginalOffsetTopBackup;
                mProgressBar.stop();
                postDelayed(mReturnToStartPosition, 500);
            }
        }
    }
    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     *         progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }
    private void ensureTarget() {
        if (mTarget == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
            }
            mTarget = getChildAt(0);
            mOriginalOffsetTopBackup = mOriginalOffsetTop = mTarget.getTop() + getPaddingTop();
        }
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (headerHeight == -1) {
            headerHeight = (int) (DisplayUtil.getFontHeight(textSize) * 3 + 4* metrics.density * 4);
        }
        if (isRefreshing()) {
            mOriginalOffsetTop = headerHeight + getPaddingTop();
        } else if (mLoading) {
            mOriginalOffsetTop = -headerHeight + getPaddingTop();
        } else {
            mOriginalOffsetTop = mOriginalOffsetTopBackup;
        }
        if (mDistanceToTriggerSync == -1) {
            if (getParent() != null && ((View) getParent()).getHeight() > 0) {
                mDistanceToTriggerSync = (int) Math.min(((View) getParent()).getHeight() * MAX_SWIPE_DISTANCE_FACTOR,
                        REFRESH_TRIGGER_DISTANCE * metrics.density);
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        mProgressBar.draw(canvas);
    }


    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        // TODO 设置header顶部间距
        // mProgressBar.setBounds(0, 0, width, mCurrentTargetOffsetTop);
        mProgreesBarWidth = width;
        if (getChildCount() == 0) {
            return;
        }
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = mCurrentTargetOffsetTop + getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        try {

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
        catch (Exception ex){

        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (getChildCount() > 0) {
            getChildAt(0).measure(
                    MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView
                        .getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }
    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll down. Override this if the child view is a custom view.
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);

                if (lastChild != null) {
                    if ((absListView.getLastVisiblePosition() == (absListView.getCount() - 1))
                            && lastChild.getBottom() >= absListView.getPaddingBottom()) {
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return mTarget.getHeight() - mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {

        try {
            ensureTarget();
            final int action = MotionEventCompat.getActionMasked(ev);
            if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
                mReturningToStart = false;
            }
            if (!isEnabled() || mReturningToStart) {
                return false;
            }
            if (isRefreshing()) {
                return false;
            }
            if (mLoading) {
                return false;
            }
            switch (action) {
                case MotionEvent.ACTION_DOWN :
                    mInitialMotionY = ev.getY();
                    mInitialMotionX = ev.getX();
                    mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    mIsBeingDragged = false;
                    up = canChildScrollUp();
                    down = canChildScrollDown();
                    break;
                case MotionEvent.ACTION_MOVE :
                    if (mActivePointerId == INVALID_POINTER) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                        return false;
                    }
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex < 0) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);
                    final float yDiff = y - mInitialMotionY;
                    final float adx = Math.abs(x - mInitialMotionX);
                    final float ady = Math.abs(y - mInitialMotionY);
                    if (up) { // 不触发下拉刷新
                        if (down) { // 不触发上拉加载
                            return false;
                        } else {
                            if (pull2load) {
                                double angle = Math.atan(adx / ady);
                                if (-yDiff > mTouchSlop && (angle < trigger_angle)) { // 上拉加载
                                    direction = 1;
                                    mIsBeingDragged = true;
                                }
                            }
                        }
                    } else {
                        if (pull2refresh) {
                            double angle = Math.atan(adx / ady);
                            if (yDiff > mTouchSlop && (angle < trigger_angle)) {
                                direction = 0;
                                mIsBeingDragged = true;
                            }
                        }

                    }
                    break;
                case MotionEventCompat.ACTION_POINTER_UP :
                    onSecondaryPointerUp(ev);
                    break;
                case MotionEvent.ACTION_UP :
                case MotionEvent.ACTION_CANCEL :
                    mIsBeingDragged = false;
                    mActivePointerId = INVALID_POINTER;
                    break;
            }
            return mIsBeingDragged;
        } catch (IllegalArgumentException e) {
            Log.e("error", e.getMessage());
            return false;
        }

    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
    }

    public boolean onTouchEvent(MotionEvent ev) {
        try {
            final int action = MotionEventCompat.getActionMasked(ev);
            if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
                mReturningToStart = false;
            }
            if (!isEnabled() || mReturningToStart) {
                return false;
            }
            if (isRefreshing()) {
                return false;
            }
            if (mLoading) {
                return false;
            }
            switch (action) {
                case MotionEvent.ACTION_DOWN :
                    mInitialMotionY = ev.getY();
                    mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    mIsBeingDragged = false;
                    up = canChildScrollUp();
                    down = canChildScrollDown();
                    break;
                case MotionEvent.ACTION_MOVE :
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex < 0) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    final float y = MotionEventCompat.getY(ev, pointerIndex);
                    final float yDiff = y - mInitialMotionY;
                    if (up) { // 不触发下拉刷新
                        if (down) { // 不触发上拉加载
                            return false;
                        } else {
                            if (-yDiff > mTouchSlop) { // 上拉加载
                                direction = 1;
                                mIsBeingDragged = true;
                            }
                        }
                    } else {
                        if (yDiff > mTouchSlop) {
                            direction = 0;
                            mIsBeingDragged = true;
                        }
                    }
                    if (mIsBeingDragged) {
                        if (direction == 0) {
                            if (yDiff > mDistanceToTriggerSync) {
                                state = RELEASE_TO_REFRESH;
                                defaultText = releaseDownLabel;
                            } else {
                                state = PULL_TO_REFRESH;
                                defaultText = pullDownLabel;
                            }
                            if (latestRefreshTime.equals("")) {
                                latestRefreshTime = formatter.format(new Date());
                            }
                            setProgressBarProperty(mTarget.getTop());

                            updateContentOffsetTop((int) yDiff >> 1);
                        } else {
                            if (-yDiff > mDistanceToTriggerSync) {
                                state = RELEASE_TO_REFRESH;
                                defaultText = releaseUpLabel;
                            } else {
                                state = PULL_TO_REFRESH;
                                defaultText = pullUpLabel;
                            }
                            if (latestLoadTime.equals("")) {
                                latestLoadTime = formatter.format(new Date());
                            }
                            setProgressBarProperty(mTarget.getTop());
                            updateContentOffsetBottom((int) yDiff >> 1);
                        }
                    }
                    break;
                case MotionEventCompat.ACTION_POINTER_DOWN : {
                    final int index = MotionEventCompat.getActionIndex(ev);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                    break;
                }
                case MotionEventCompat.ACTION_POINTER_UP :
                    onSecondaryPointerUp(ev);
                    break;
                case MotionEvent.ACTION_UP :
                    final int mPointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (mPointerIndex < 0) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    final float mY = MotionEventCompat.getY(ev, mPointerIndex);
                    final float mYDiff = mY - mInitialMotionY;

                    if (!mIsBeingDragged && mYDiff > mTouchSlop) {
                        mIsBeingDragged = true;
                    }
                    if (direction == 0) {
                        if (mIsBeingDragged) {
                            if (mYDiff > mDistanceToTriggerSync) {
                                startRefresh();
                            } else {
                                mReturnToStartPosition.run();
                            }
                        }
                    } else {
                        if (mIsBeingDragged) {
                            if (-mYDiff > mDistanceToTriggerSync) {
                                startLoad();
                            } else {
                                mReturnToStartPosition.run();
                            }
                        }
                    }
                case MotionEvent.ACTION_CANCEL :
                    mIsBeingDragged = false;
                    mActivePointerId = INVALID_POINTER;
                    return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            Log.e("error", e.getMessage());
            return false;
        }

    }
    private void startRefresh() {
        removeCallbacks(mCancel);
        setRefreshing(true);
        mListener.onRefresh();
    }
    private void startLoad() {
        setLoading(true);
        mLoadListener.onLoad();
    }
    private void updateContentOffsetBottom(int targetBottom) {
        if (targetBottom > 0) {
            targetBottom = 0;
        }
        int offset = 0;
        if (lastDiff != 0) {
            offset = targetBottom - lastDiff;
        } else {
            offset = targetBottom;
        }
        lastDiff = targetBottom;
        setTargetOffsetTopAndBottom(offset);
    }

    private void updateContentOffsetTop(int targetTop) {
        final int currentTop = mTarget.getTop();
        if (targetTop < 0) {
            targetTop = 0;
        }
        setTargetOffsetTopAndBottom(targetTop - currentTop);
    }

    private void setTargetOffsetTopAndBottom(int offset) {
        mTarget.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mTarget.getTop();
    }
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }
    public interface OnLoadListener {
        public void onLoad();
    }

    /**
     * Simple AnimationListener to avoid having to implement unneeded methods in
     * AnimationListeners.
     */
    private class BaseAnimationListener implements AnimationListener {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
}

