package thack.ac.tabledash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GraphicalLayout extends SurfaceView implements SurfaceHolder.Callback {

    private GraphicsThread _thread;

    private Paint paint;
    private Rect rect;

    private Paint paintBlack;

    public GraphicalLayout(Context context) {
        super(context);
        getHolder().addCallback(this);

        paintBlack = new Paint(Color.BLACK);
        paintBlack.setTextSize(50);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Clears the canvas
        canvas.drawColor(Color.WHITE);

        if(StatusActivity.tables != null) {
            // Redraw the tables every second
            // Update the duration left for each table
            for (Table table : StatusActivity.tables) {
                canvas.drawBitmap(Table.bitmap, table.getRect().left, table.getRect().top, table.getPaint());
                table.setDurationLeft(table.getDurationLeft() - 1);
            }
        }

        if(rect == null)
        {
            rect = new Rect(60, canvas.getHeight() - 200, canvas.getWidth() - 60, canvas.getHeight() - 60);
            Shader shader = new LinearGradient(rect.left, 0, rect.right, 0, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
            paint = new Paint();
            paint.setShader(shader);
        }
        canvas.drawRect(rect, paint);

        canvas.drawText("Occupied", 40, canvas.getHeight() - 240, paintBlack);
        canvas.drawText("Vacant", canvas.getWidth() - 200, canvas.getHeight() - 240, paintBlack);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false); // Allows us to use invalidate() to call onDraw()

        // Start the thread that will make calls to onDraw()
        _thread = new GraphicsThread(getHolder(), this);
        _thread.setRunning(true);
        _thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            _thread.setRunning(false);                //Tells thread to stop
            _thread.join();                           //Removes thread from mem.
        } catch (InterruptedException e) {}
    }

    class GraphicsThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private GraphicalLayout _graphicalLayout;
        private boolean _run = false;

        public GraphicsThread(SurfaceHolder surfaceHolder, GraphicalLayout graphicalLayout) {
            _surfaceHolder = surfaceHolder;
            _graphicalLayout = graphicalLayout;
        }

        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is
                c = null;      //set to false and loop ends, stopping thread

                try {
                    // Limit to one frame per second
                    Thread.sleep(1000);

                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {

                    //Insert methods to modify positions of items in onDraw()
                    postInvalidate();
                    }
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
