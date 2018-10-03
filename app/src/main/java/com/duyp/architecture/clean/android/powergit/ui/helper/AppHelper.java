package com.duyp.architecture.clean.android.powergit.ui.helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.duyp.androidutils.AlertUtils;
import com.duyp.architecture.clean.android.powergit.R;

public class AppHelper {

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), uri);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            AlertUtils.showToastShortMessage(context, context.getString(R.string.success_copied));
        }
    }

    public static boolean isNightMode(@NonNull Resources resources) {
        @PrefGetter.ThemeType int themeType = PrefGetter.getThemeType(resources);
        return themeType != PrefGetter.LIGHT;
    }
}
