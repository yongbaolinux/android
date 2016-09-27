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

import java.util.ArrayList;
import java.util.List;

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

        //初始化时间数据、潮汐数据和颜色数据
        int[] clocks = new int[]{1,4,7,10,1};         //时间
        int[] tide = new int[]{130,150,165,130,120};    //潮汐数据
        int[] doughnutColors = new int[3];          //颜色数据
        doughnutColors[0] = 0xFFc8d383;
        doughnutColors[1] = 0xFF62da7e;
        doughnutColors[2] = 0xFFc8d383;
        mPaintPath.setShader(new SweepGradient(circleXPointer, circleYPointer, doughnutColors, null));

        //添加点
        List<Float> points = new ArrayList<Float>();
        int angle = 0;     //小时数乘上30度
        float xPointer = 0;
        float yPointer = 0;
        for(int i=0;i<clocks.length;i++){
            angle = clocks[i] * 30;     //小时数乘上30度
            xPointer = (float) (tide[i] * Math.sin(angle*Math.PI/180) + circleXPointer);
            yPointer = (float) (circleYPointer - tide[i] * Math.cos(angle*Math.PI/180));
            points.add(xPointer);
            points.add(yPointer);
        }
        //绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(points.get(0), points.get(1));
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        int length = points.size();
        float mFirstMultiplier = 0.3f;
        // 设置第二个控制点为66%的距离
        float mSecondMultiplier = 1 - mFirstMultiplier;

        for (int b = 0; b < length; b += 2) {
            int nextIndex = b + 2 < length ? b + 2 : b;
            int nextNextIndex = b + 4 < length ? b + 4 : nextIndex;
            // 设置第一个控制点
            calc(points, p1, b, nextIndex, mSecondMultiplier);
            // 设置第二个控制点
            p2.setX(points.get(nextIndex));
            p2.setY(points.get(nextIndex + 1));
            // 设置第三个控制点
            calc(points, p3, nextIndex, nextNextIndex, mFirstMultiplier);
            // 最后一个点就是赛贝尔曲线上的点
            path.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
        }

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

    /**
     * 计算控制点
     * @param points
     * @param result
     * @param index1
     * @param index2
     * @param multiplier
     */
    private void calc(List<Float> points, Point result, int index1, int index2, final float multiplier) {
        float p1x = points.get(index1);
        float p1y = points.get(index1 + 1);
        float p2x = points.get(index2);
        float p2y = points.get(index2 + 1);

        float diffX = p2x - p1x;
        float diffY = p2y - p1y;
        result.setX(p1x + (diffX * multiplier));
        result.setY(p1y + (diffY * multiplier));
    }
}
