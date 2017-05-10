package com.math.jeux.cellhexacalc;

/**
 * Created by fabrice on 26/01/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;


public class HexaTextView extends View
{

    private static int nbPoints =6;

    private String text;
    protected float strokeWidth;
    protected int strokeColor,solidColor,selectColor;


    private Point pointO ;
    private Point tabHexaPoint[] = new Point[nbPoints];

    public HexaTextView(Context context) {
        super(context);
    }

    public HexaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexaTextView(Context context, AttributeSet attrs, String value) {
        super(context, attrs);
        text = value;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getInt(R.styleable.CircularTextView_StrokeWidth,2);
        strokeColor = a.getColor(R.styleable.CircularTextView_StrokeColor,0);
        solidColor = a.getColor(R.styleable.CircularTextView_SolidColor,0);
        selectColor = a.getColor(R.styleable.CircularTextView_SelectColor,-1);

        a.recycle();
    }

    public HexaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    private void computePoint() {
        int  h = this.getHeight();
        int  w = this.getWidth();

        int adjust = 2;

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        pointO = new Point((int)(w/2),(int)(h/2));
        int cpt = 0;
        for (int i = 0; i<360 ; i+=360/ nbPoints) {
            tabHexaPoint[cpt] = new Point();
            tabHexaPoint[cpt].x = pointO.x + (int)(radius * (float) Math.cos(Math.toRadians(i)));
            tabHexaPoint[cpt].y = pointO.y + (int)(radius * (float) Math.sin(Math.toRadians(i)));

            if (i==60*1) {
                tabHexaPoint[cpt].x -=adjust;
                tabHexaPoint[cpt].y +=adjust;
            }
            if (i==60*2) {
                tabHexaPoint[cpt].x +=adjust;
                tabHexaPoint[cpt].y +=adjust;
            }
            if (i==60*4) {
                tabHexaPoint[cpt].x +=adjust;
                tabHexaPoint[cpt].y -=adjust;
            }
            if (i==60*5) {
                tabHexaPoint[cpt].x -=adjust;
                tabHexaPoint[cpt].y -=adjust;
            }


            cpt++;
        }
    }

    public void draw(Canvas canvas) {

        if (text.isEmpty()) { return; }

        Paint hexaPaint = new Paint();
        hexaPaint.setColor(isSelected() ? selectColor : solidColor);
        hexaPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        //strokePaint.setColor(selected ? selectStrokeColor : strokeColor);
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        computePoint();
        drawPolygone(canvas,tabHexaPoint,hexaPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(android.graphics.Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        int width = (int) textPaint.measureText(text);

        canvas.drawText(text,pointO.x-width/2,pointO.y+8,textPaint);
    }

    private void drawPolygone(Canvas canvas, Point tabHexaPoint[], Paint paint) {

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(tabHexaPoint[0].x,tabHexaPoint[0].y);
        for (int i = 1 ; i< tabHexaPoint.length; i++) {
            path.lineTo(tabHexaPoint[i].x, tabHexaPoint[i].y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

}
