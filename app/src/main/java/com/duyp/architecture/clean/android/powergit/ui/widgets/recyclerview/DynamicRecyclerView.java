package com.duyp.architecture.clean.android.powergit.ui.widgets.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.duyp.architecture.clean.android.powergit.R;
import com.duyp.architecture.clean.android.powergit.ui.utils.ViewHelper;


/**
 * Created by Kosh on 9/24/2015. copyrights are reserved
 * <p>
 * recyclerview which will showParentOrSelf/showParentOrSelf itself base on adapter
 */
public class DynamicRecyclerView extends RecyclerView {

    private BottomPaddingDecoration bottomPaddingDecoration;

    public DynamicRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRecyclerView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicRecyclerView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void removeBottomDecoration() {
        if (bottomPaddingDecoration != null) {
            removeItemDecoration(bottomPaddingDecoration);
            bottomPaddingDecoration = null;
        }
    }

    public void addDecoration() {
        bottomPaddingDecoration = BottomPaddingDecoration.with(getContext());
        addItemDecoration(bottomPaddingDecoration);
    }

    public void addKeyLineDivider() {
        if (canAddDivider()) {
            Resources resources = getResources();
            addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height),
                    resources.getDimensionPixelSize(R.dimen.xxx_large), ViewHelper.getListDivider(getContext())));
        }
    }

    public void addDivider() {
        if (canAddDivider()) {
            Resources resources = getResources();
            addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
                    ViewHelper.getListDivider(getContext())));
        }
    }

    public void addNormalSpacingDivider() {
        addDivider();
    }

    public void addDivider(@NonNull Class toDivide) {
        if (canAddDivider()) {
            Resources resources = getResources();
            addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
                    ViewHelper.getListDivider(getContext()), toDivide));
        }
    }

    private boolean canAddDivider() {
        if (getLayoutManager() != null) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                return true;
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                return ((GridLayoutManager) getLayoutManager()).getSpanCount() == 1;
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) getLayoutManager()).getSpanCount() == 1;
            }
        }
        return false;
    }
}
