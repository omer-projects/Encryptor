package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageDesigner {


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = (width + height) / 16;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static Bitmap squareImage(Bitmap image) {
        Bitmap squreImage = image;
        int height = squreImage.getHeight();
        int width = squreImage.getWidth();
        if (width != height) {
            int size = 0;
            int startX = 0;
            int startY = 0;
            if (width < height) {
                size = width;
                startY = (height - width) / 2;
            } else {
                size = height;
                startX = (width - height) / 2;
            }
            squreImage = Bitmap.createBitmap(image, startX, startY, size, size);
        }
        return squreImage;
    }

}
