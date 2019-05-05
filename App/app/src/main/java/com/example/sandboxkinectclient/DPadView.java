package com.example.sandboxkinectclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DPadView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float sizeX;
    private float sizeY;

    private void setupDimensions(float centerx, float centery, float sizex, float sizey) {
        centerX = centerx;
        centerY = centery;
        sizeX = sizex;
        sizeY = sizey;
    }

    public boolean onTouch(View v, MotionEvent e){
        if(v.equals(this)){
            float x = e.getX();
            float y = e.getY();
            if(e.getAction()!=MotionEvent.ACTION_UP){
                float relX = centerX-x;
                float relY = centerY-y;
                if (relY > relX && relY > -relX){
                    drawPad(true, false, false, false);
                } else if (relY > relX && relY < -relX) {
                    drawPad(false, false, true, false);
                } else if (relY < relX && relY > -relX) {
                    drawPad(false, false, false, true);
                } else if (relY < relX && relY < -relX) {
                    drawPad(false, true, false, false);
                }
            }
            else{
                drawPad(false, false, false, false);
            }
        }
        return true;
    }

    public DPadView (Context context){
        super(context);
        setOnTouchListener(this);
    }

    public DPadView (Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setOnTouchListener(this);
    }

    public DPadView (Context context, AttributeSet attributes) {
        super(context, attributes);
        setOnTouchListener(this);
    }

    private Paint getColour(boolean press){
        Paint color = new Paint();
        if(press){
            color.setARGB(255, 66,66,66);
        } else{
            color.setARGB(255,33,33,33);
        }
        color.setStyle(Paint.Style.FILL);
        return color;
    }

    public void drawPad (boolean up, boolean down, boolean left, boolean right) {
        if(getHolder().getSurface().isValid()){
            Canvas c = this.getHolder().lockCanvas();
            Paint colors;
            Path path = new Path();

            colors = getColour(up);
            RectF rect = new RectF(centerX+sizeX/2, centerY-sizeX/2-sizeY, centerX-sizeX/2, centerX+sizeX/2);
            c.drawRect(rect, colors);
            path.moveTo(centerX, centerY);
            path.lineTo(rect.left, rect.bottom);
            path.moveTo(rect.left, rect.bottom);
            path.lineTo(rect.right, rect.bottom);
            path.moveTo(rect.right, rect.bottom);
            path.lineTo(centerX, centerY);
            c.drawPath(path, colors);

            colors = getColour(down);
            rect = new RectF(centerX+sizeX/2, centerY+sizeX/2+sizeY, centerX-sizeX/2, centerY+sizeX/2);
            c.drawRect(rect, colors);
            path.moveTo(centerX, centerY);
            path.lineTo(rect.left, rect.top);
            path.moveTo(rect.left, rect.top);
            path.lineTo(rect.right, rect.top);
            path.moveTo(rect.right, rect.top);
            path.lineTo(centerX, centerY);
            c.drawPath(path, colors);

            colors = getColour(left);
            rect = new RectF(centerX+sizeX/2+sizeY, centerY-sizeX/2, centerX+sizeX/2, centerY+sizeX/2);
            c.drawRect(rect, colors);
            path.moveTo(centerX, centerY);
            path.lineTo(rect.right, rect.top);
            path.moveTo(rect.right, rect.top);
            path.lineTo(rect.right, rect.bottom);
            path.moveTo(rect.right, rect.bottom);
            path.lineTo(centerX, centerY);
            c.drawPath(path, colors);

            colors = getColour(right);
            rect = new RectF(centerX+sizeX/2, centerY-sizeX/2, centerX+sizeX/2+sizeY, centerY+sizeX/2);
            c.drawRect(rect, colors);
            path.moveTo(centerX, centerY);
            path.lineTo(rect.left, rect.top);
            path.moveTo(rect.left, rect.top);
            path.lineTo(rect.left, rect.bottom);
            path.moveTo(rect.left, rect.bottom);
            path.lineTo(centerX, centerY);
            c.drawPath(path, colors);

            getHolder().unlockCanvasAndPost(c);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawPad(false, false, false, false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
