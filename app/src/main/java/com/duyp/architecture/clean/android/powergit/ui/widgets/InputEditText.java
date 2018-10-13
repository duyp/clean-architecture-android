package com.duyp.architecture.clean.android.powergit.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.duyp.architecture.clean.android.powergit.R;
import com.duyp.architecture.clean.android.powergit.ui.utils.UiUtils;

public class InputEditText extends AppCompatEditText {

    protected final static int DRAWABLE_RIGHT = 2;

    protected boolean mIsEditable;

    protected boolean mIsClearable;

    protected Drawable mClearIcon;

    @ColorRes protected int mTintColorId;

    @Nullable private OnClearListener mOnClearListener;

    public InputEditText(final Context context) {
        super(context);
        init(context, null);
    }

    public InputEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public boolean isEditable() {
        return mIsEditable;
    }

    public boolean isClearable() {
        return mIsClearable;
    }

    public void setClearable(final boolean clearable) {
        mIsClearable = clearable;
        invalidateIcon(getText());
    }

    public void setOnClearListener(@NonNull final OnClearListener onClearListener) {
        mOnClearListener = onClearListener;
    }

    @Override protected void finalize() throws Throwable {
        mClearIcon = null;
        super.finalize();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override public boolean onTouchEvent(final MotionEvent event) {
        if(mIsClearable && event.getAction() == MotionEvent.ACTION_UP) {

            final Drawable rightDrawable = getCompoundDrawables()[DRAWABLE_RIGHT];

            final boolean isClearIcon = mClearIcon.equals(rightDrawable);

            if(isClearIcon) {
                final int[] screenLocation = new int[2];
                getLocationInWindow(screenLocation);
                final int right = screenLocation[0] + getWidth();
                final int leftEdgeOfRightDrawable = right - rightDrawable.getBounds().width() - getCompoundDrawablePadding();
                if(event.getRawX() >= leftEdgeOfRightDrawable) {
                    // touched on clear icon
                    setText(null);
                    if (mIsEditable) {
                        requestFocus();
                    }
                    event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
                    if(mOnClearListener != null) {
                        mOnClearListener.onClear(this);
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @CallSuper protected void init(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputEditText);
        try {
            mIsEditable = a.getBoolean(R.styleable.InputEditText_editable, defaultEditable());
            mIsClearable = a.getBoolean(R.styleable.InputEditText_clearable, defaultClearable());
            mTintColorId = a.getResourceId(R.styleable.InputEditText_iconTint, R.color.dark_grey);
        } finally {
            a.recycle();
        }

        mClearIcon = UiUtils.getTintedVectorDrawable(getContext(), R.drawable.ic_clear, mTintColorId);

        if(!mIsEditable) {
            setFocusable(false);
            setInputType(InputType.TYPE_NULL);
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {}

            @Override public void afterTextChanged(final Editable s) {
                invalidateIcon(s);
            }
        });

        invalidateIcon(getText());
    }

    protected boolean defaultClearable() {
        return false;
    }

    protected boolean defaultEditable() {
        return true;
    }

    protected void invalidateIcon(final Editable txt) {
        if (mIsClearable) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, isEmpty(txt) ? null : mClearIcon, null);
        }
    }

    protected boolean isEmpty(final Editable txt) {
        return txt.length() == 0;
    }

    public interface OnClearListener {

        void onClear(InputEditText edt);
    }
}
