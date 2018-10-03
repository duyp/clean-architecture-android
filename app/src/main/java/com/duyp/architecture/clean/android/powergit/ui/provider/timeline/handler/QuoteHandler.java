package com.duyp.architecture.clean.android.powergit.ui.provider.timeline.handler;

import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;

import com.style.MarkDownQuoteSpan;

import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

/**
 * Created by Kosh on 23 Apr 2017, 11:30 AM
 */

public class QuoteHandler extends TagNodeHandler {

    @ColorInt private final int color;

    public QuoteHandler(int color) {
        this.color = color;
    }

    public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
        builder.append("\n");
        builder.setSpan(new MarkDownQuoteSpan(color), (start > builder.length() - 1) ? start + 1 : start, builder
                .length() - 1, 33);
        builder.append("\n");
    }
}
