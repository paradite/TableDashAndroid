package thack.ac.tabledash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GraphicalLayout extends SurfaceView implements SurfaceHolder.Callback {

    private GraphicsThread _thread;

    public GraphicalLayout(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Clears the canvas
        canvas.drawColor(Color.BLACK);

        // Redraw the tables
        for(Table table : StatusActivity.tables) {
            canvas.drawBitmap(Table.bitmap, table.getRect().left, table.getRect().top, table.getPaint());
        }
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
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {

                    //Insert methods to modify positions of items in onDraw()
                    postInvalidate();
                    }
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
