package com.duyp.architecture.clean.android.powergit.ui.provider.timeline.handler;

import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.duyp.architecture.clean.android.powergit.ui.helper.InputHelper;
import com.duyp.architecture.clean.android.powergit.ui.provider.timeline.handler.drawable.DrawableGetter;

import net.nightwhistler.htmlspanner.TagNodeHandler;
import net.nightwhistler.htmlspanner.spans.CenterSpan;

import org.htmlcleaner.TagNode;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by Kosh on 22 Apr 2017, 1:09 PM
 */

public class DrawableHandler extends TagNodeHandler {

    private final TextView textView;
    private final int width;

    public DrawableHandler(TextView textView, int width) {
        this.textView = textView;
        this.width = width;
    }

    @SuppressWarnings("ConstantConditions") private boolean isNull() {
        return textView == null;
    }

    @Override public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
        String src = node.getAttributeByName("src");
        if (!InputHelper.isEmpty(src)) {
            builder.append("￼");
            if (isNull()) return;
            DrawableGetter imageGetter = new DrawableGetter(textView, width);
            builder.setSpan(new ImageSpan(imageGetter.getDrawable(src)), start, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new CenterSpan(), start, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            appendNewLine(builder);
        }
    }
}
