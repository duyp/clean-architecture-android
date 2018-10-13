package com.duyp.architecture.clean.android.powergit.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

public class UiUtils {

    public static Drawable getTintedVectorDrawable(final Context context, @DrawableRes final int vectorDrawable,
                                                   @ColorRes final int colorRes) {
        final Drawable wrapDrawable = createDrawable(context, vectorDrawable);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, colorRes));
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }

    public static Drawable getTintedVectorDrawableFromColor(final Context context,
                                                            @DrawableRes final int vectorDrawable,
                                                            @ColorInt final int color) {
        final Drawable wrapDrawable = createDrawable(context, vectorDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }

    public static Drawable getTintedDrawable(final Context context, final Drawable inputDrawable,
                                             @ColorRes final int colorRes) {
        final int color = ContextCompat.getColor(context, colorRes);
        return getTintedDrawableFromColor(inputDrawable, color);
    }

    public static Drawable getTintedDrawableFromColor(final Drawable inputDrawable, @ColorInt final int color) {
        if(inputDrawable == null)
            return null;

        final Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable).mutate();
        DrawableCompat.setTint(wrapDrawable, color);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }

    public static Bitmap getBitmapFromVectorDrawable(final Context context, @DrawableRes final int vectorDrawable) {
        final Drawable wrapDrawable = createDrawable(context, vectorDrawable);

        final Bitmap bitmap = Bitmap.createBitmap(wrapDrawable.getIntrinsicWidth(),
                wrapDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        wrapDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        wrapDrawable.draw(canvas);

        return bitmap;
    }

    @NonNull private static Drawable createDrawable(final Context context, @DrawableRes final int vectorDrawable) {
        final Drawable drawable = AppCompatResources.getDrawable(context, vectorDrawable);
        return DrawableCompat.wrap(drawable).mutate();
    }
}
