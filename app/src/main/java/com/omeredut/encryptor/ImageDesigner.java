package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageDesigner {


    /**
     * This function rounded corners of given bitmap image and by given n, if not given n it will be 25 and the image that you will get will be in a circle
     * @param bitmapImage bitmap image for round corners
     * @param n the number of percent for round
     * @return Bitmap of the rounded image
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmapImage, int n) {
        int height = bitmapImage.getHeight();
        int width = bitmapImage.getWidth();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = (width + height) / n;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapImage, rect, rect, paint);

        return output;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmapImage) {
        return getRoundedCornerBitmap(bitmapImage, 25);
    }


    /**
     * This function crop given image bitmap to square in the middle of the image
     * @param bitmapImage the image bitmap for crop
     * @return bitmap of the square image
     */
    public static Bitmap squareImage(Bitmap bitmapImage) {
        Bitmap squreImage = bitmapImage;
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
            squreImage = Bitmap.createBitmap(bitmapImage, startX, startY, size, size);
        }
        return squreImage;
    }

}
