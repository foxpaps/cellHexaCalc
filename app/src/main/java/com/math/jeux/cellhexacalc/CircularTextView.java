package com.math.jeux.cellhexacalc;

/**
 * Created by fabrice on 26/01/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class CircularTextView extends TextView
{
    protected float strokeWidth;
    protected int strokeColor,solidColor,selectColor;

    public CircularTextView(Context context) {
        super(context);
        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getInt(R.styleable.CircularTextView_StrokeWidth,2);
        strokeColor = a.getColor(R.styleable.CircularTextView_StrokeColor,0);
        solidColor = a.getColor(R.styleable.CircularTextView_SolidColor,0);*/
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getInt(R.styleable.CircularTextView_StrokeWidth,2);
        strokeColor = a.getColor(R.styleable.CircularTextView_StrokeColor,165550);
        solidColor = a.getColor(R.styleable.CircularTextView_SolidColor,-1);
        selectColor = a.getColor(R.styleable.CircularTextView_SelectColor,-1);
        int size = a.getInt(R.styleable.CircularTextView_TextSize,-1);
        a.recycle();
        this.setTextSize(size);
    }

    public CircularTextView(Context context, AttributeSet attrs,String value) {
        super(context, attrs);
        setText(value);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getInt(R.styleable.CircularTextView_StrokeWidth,2);
        strokeColor = a.getColor(R.styleable.CircularTextView_StrokeColor,0);
        solidColor = a.getColor(R.styleable.CircularTextView_SolidColor,0);
        selectColor = a.getColor(R.styleable.CircularTextView_SelectColor,-1);
        int size = a.getInt(R.styleable.CircularTextView_TextSize,-1);
        a.recycle();
        this.setTextSize(size);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        strokeWidth = a.getInt(R.styleable.CircularTextView_StrokeWidth,2);
        strokeColor = a.getColor(R.styleable.CircularTextView_StrokeColor,0);
        solidColor = a.getColor(R.styleable.CircularTextView_SolidColor,0);
        int size = a.getInt(R.styleable.CircularTextView_TextSize,-1);
        a.recycle();
        this.setTextSize(size);

    }
    private void init(Context context, AttributeSet attrs) {

        strokeWidth = R.styleable.CircularTextView_StrokeWidth;
        strokeColor = R.styleable.CircularTextView_StrokeColor;
        solidColor = R.styleable.CircularTextView_SolidColor;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(isSelected() ? selectColor : solidColor);
        //circlePaint.setColor( solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        //strokePaint.setColor(selected ? selectStrokeColor : strokeColor);
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius,strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }

    public void setStrokeColor(String color)
    {

        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {
        solidColor = Color.parseColor(color);

    }


}
