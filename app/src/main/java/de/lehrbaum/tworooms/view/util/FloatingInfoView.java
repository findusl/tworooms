package de.lehrbaum.tworooms.view.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sebastian on 20.10.2015.
 */
public final class FloatingInfoView extends View {

    private Paint mPaintShape;
    private Paint mPaintText;

    private int mPaddingRight;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mMinTextWidth;
    private int mMinTextHeight;
    private int mCircleDia;
    private String mText;

    public FloatingInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
        //check attributes you need, for example all paddings
        int [] attributes = new int [] {android.R.attr.paddingLeft, android.R.attr.paddingTop,
                android.R.attr.paddingBottom, android.R.attr.paddingRight};

        //then obtain typed array
        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);

        //and get values you need by indexes from your array attributes defined above
        mPaddingLeft = arr.getDimensionPixelOffset(0, 5);
        mPaddingTop = arr.getDimensionPixelOffset(1, 5);
        mPaddingBottom = arr.getDimensionPixelOffset(2, 5);
        mPaddingRight = arr.getDimensionPixelOffset(3, 5);
    }

    private void setupPaint() {
        mPaintShape = new Paint();
        mPaintShape.setStyle(Paint.Style.FILL);
        mPaintShape.setColor(Color.RED);
        mPaintShape.setTextSize(30);
        mPaintShape.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
    }

    public void setText(String s) {
        Rect rect = new Rect();
        mPaintText.getTextBounds(s, 0, s.length(), rect);
        mMinTextWidth = rect.width() + mPaddingLeft + mPaddingRight;
        mMinTextHeight = rect.height() + mPaddingTop + mPaddingBottom;
        mCircleDia = (int) Math.sqrt((mMinTextHeight * mMinTextHeight) - (mMinTextWidth * mMinTextWidth));
        mText = s;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mCircleDia, mCircleDia);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mText, 0, 0, mPaintText);
        canvas.drawOval(new RectF(0, 0, getWidth(), getHeight()), mPaintShape);
    }
}
