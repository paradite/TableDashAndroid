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

    public Table(Context context, int durationLeft, int left, int top) {
        if(bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.table);
        }

        this.durationLeft = durationLeft;
        this.paint = new Paint();
        updatePaint();

        rect = new Rect(left, top, left + bitmap.getWidth(), top + bitmap.getHeight());
    }

    public int getDurationLeft() {
        return durationLeft;
    }

    public void setDurationLeft(int durationLeft) {
        this.durationLeft = durationLeft;
    }

    public void updatePaint()
    {
        if(durationLeft >= 30) {
            ColorFilter filter = new LightingColorFilter(Color.RED, 1);
            paint.setColorFilter(filter);
        }
        else if(durationLeft <= 0) {
            ColorFilter filter = new LightingColorFilter(Color.WHITE, 1);
            paint.setColorFilter(filter);
        }
        else if(durationLeft <= 5) {
            ColorFilter filter = new LightingColorFilter(Color.GREEN, 1);
            paint.setColorFilter(filter);
        }
        else if(durationLeft <= 15) {
            ColorFilter filter = new LightingColorFilter(Color.YELLOW, 1);
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
