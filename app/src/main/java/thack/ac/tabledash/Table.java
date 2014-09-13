package thack.ac.tabledash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

public class Table {

    public static Bitmap bitmap = null;
    private int durationLeft;
    private Paint paint;
    private Rect rect;

    public Table(Context context, int durationLeft, int locationX, int locationY) {
        if(bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.table);
        }

        this.durationLeft = durationLeft;
        this.paint = new Paint();
        updatePaint();

        rect = new Rect(locationX, locationY,
                locationX + bitmap.getWidth(),
                locationY + bitmap.getHeight());
    }

    public int getDurationLeft() {
        return durationLeft;
    }

    public void setDurationLeft(int durationLeft) {
        this.durationLeft = (durationLeft <= 0) ? 0 : durationLeft;
    }

    public void updatePaint()
    {
        if(durationLeft >= 30) {
            ColorFilter filter = new LightingColorFilter(Color.RED, 1);
            paint.setColorFilter(filter);
        }
        else {
            int redValue = (int)((double)durationLeft/30) * 255;
            int greenValue = (int)((double)(30 - durationLeft)/30) * 255;

            ColorFilter filter = new LightingColorFilter(Color.rgb(redValue, greenValue, 0), 1);
            paint.setColorFilter(filter);
        }
    }

    public Paint getPaint() {
        return paint;
    }

    public Rect getRect() {
        return rect;
    }
}
