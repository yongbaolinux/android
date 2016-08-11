package net.diskroom.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import net.diskroom.weather.R;

/**
 * TODO: document your custom view class.
 */
public class DrawView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private int width;                  //canvas  画布宽度
    private int height;                 //canvas  画布高度
    private float circleXPointer;         //圆心 x 坐标
    private float circleYPointer;         //圆心 y 坐标

    private Paint mPaintCircle;
    private Paint mPaintNumber;
    private Paint mPaintRect;
    private Paint mPaintPath;
    private Paint mPaintPathTest;

    public DrawView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        mPaintRect = new Paint();
        mPaintRect.setStyle(Paint.Style.FILL);

        mPaintCircle = new Paint();
        mPaintCircle.setStyle(Paint.Style.FILL);  //实心圆还是空心圆
        mPaintCircle.setAntiAlias(true);            //抗锯齿效果
        mPaintCircle.setStrokeWidth(50);            //圆环的宽度

        mPaintPath = new Paint();
        mPaintPath.setStyle(Paint.Style.STROKE);  //
        mPaintPath.setAntiAlias(true);            //抗锯齿效果
        mPaintPath.setStrokeWidth(50);            //

        mPaintNumber = new Paint();
        mPaintNumber.setTextSize(20);
        mPaintNumber.setColor(Color.WHITE);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.DrawView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.DrawView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.DrawView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.DrawView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.DrawView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight();
        width = getWidth();
        circleXPointer = width / 2;
        circleYPointer = height / 2 - 30;

        /*for(int i=0;i<256;i++) {
            mPaintRect.setARGB(255,255-i,0,i);
            canvas.drawRect(100+i, 100, 101+i, 200, mPaintRect);
        }*/

        //绘制数字
        int[] clocks = new int[]{1,4,7,10};         //时间
        int[] tide = new int[]{130,150,165,130};    //潮汐数据

        //绘制贝塞尔曲线
        int angle = clocks[0] * 30;     //小时数乘上30度
        float xPointer = (float) (tide[0] * Math.sin(angle*Math.PI/180)+circleXPointer);
        float yPointer = (float) (circleYPointer - tide[0] * Math.cos(angle*Math.PI/180));

        Path path = new Path();
        path.moveTo(xPointer, yPointer);
        for(int i=1;i<clocks.length;i++){
            angle = clocks[i] * 30;     //小时数乘上30度
            xPointer = (float) (tide[i] * Math.sin(angle*Math.PI/180) + circleXPointer);
            yPointer = (float) (circleYPointer - tide[i] * Math.cos(angle*Math.PI/180));
            path.lineTo(xPointer,yPointer);
        }
        path.close();

        int[] doughnutColors = new int[3];
        doughnutColors[0] = 0xFFFF0000;
        doughnutColors[1] = 0xFF0000FF;
        doughnutColors[2] = 0xFFFF0000;
        mPaintPath.setShader(new SweepGradient(circleXPointer, circleYPointer, doughnutColors, null));
        canvas.drawPath(path,mPaintPath);

        //绘制贝塞尔曲线后再画圆
        canvas.drawCircle(circleXPointer, circleYPointer, 100, mPaintPath);


        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
