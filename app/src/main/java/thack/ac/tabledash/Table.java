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

    private String id;
    private int durationLeft; // Should be in seconds
    private Paint paint;
    private Rect rect;

    public Table(Context context, String id, int durationLeft, int locationX, int locationY) {
        if(bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.table);
        }

        this.id = id;
        this.durationLeft = durationLeft;

        this.paint = new Paint();
        updatePaint();

        rect = new Rect(locationX, locationY,
                locationX + bitmap.getWidth(),
                locationY + bitmap.getHeight());
    }

    public String getID()
    {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public int getDurationLeft() {
        return durationLeft;
    }

    public void setDurationLeft(int durationLeft) {
        this.durationLeft = (durationLeft <= 0) ? 0 : durationLeft;
        updatePaint();
    }

    public void updatePaint()
    {
        if(durationLeft > 30) {
            ColorFilter filter = new LightingColorFilter(Color.RED, 1);
            paint.setColorFilter(filter);
        }
        else {
            int redValue = (int)((durationLeft/30.0) * 255);
            int greenValue = (int)( ((30 - durationLeft)/30.0) * 255);

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
