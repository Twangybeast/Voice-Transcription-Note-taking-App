package twangybeast.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import twangybeast.myapplication.soundAnalysis.Complex;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class WaveformView extends SurfaceView implements SurfaceHolder.Callback
{
    Queue<float[]> rawHistory;
    public static final int MAX_HISTORY = 20;

    public WaveformView(Context context)
    {
        super(context);
        init();
    }

    public WaveformView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public WaveformView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public void init()
    {
        setZOrderOnTop(true);
        rawHistory = new LinkedList<>();
        getHolder().addCallback(this);
    }

    public float[] updateAudioData(float[] buffer)
    {
        float[] newBuffer;
        synchronized (rawHistory)
        {
            if (rawHistory.size() == MAX_HISTORY)
            {
                newBuffer = rawHistory.poll();
                System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            }
            else
            {
                newBuffer = buffer.clone();
            }

            rawHistory.offer(newBuffer);
            return newBuffer;
        }
    }

    public void updateDisplay()
    {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null)
        {
            drawAll(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void drawAll(Canvas canvas)
    {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        float width = getWidth();
        float height = getHeight() / 2;
        float yCenter = height;
        int pixelX = 0;
        float lastX = -1;
        float lastY = 0;
        synchronized (rawHistory)
        {
            for (float[] buffer : rawHistory)
            {
                for (int x = 0; x < width / MAX_HISTORY; x++)
                {
                    int i = (int) ((x / (width / MAX_HISTORY)) * buffer.length);
                    float sample = buffer[i];
                    float y = (-sample) * height + yCenter;
                    if (lastX != -1)
                    {
                        canvas.drawLine(lastX, lastY, pixelX, y, paint);
                    }
                    lastX = pixelX;
                    lastY = y;
                    pixelX++;
                }
            }
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        updateDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        updateDisplay();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }
}
