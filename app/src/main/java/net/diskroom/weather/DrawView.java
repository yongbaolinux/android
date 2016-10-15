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
    private Paint mPaintLine;
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
        mPaintCircle.setStyle(Paint.Style.FILL); //实心圆还是空心圆
        mPaintCircle.setAntiAlias(true);         //抗锯齿效果
        mPaintCircle.setStrokeWidth(1);         //圆环的宽度
        mPaintCircle.setColor(Color.WHITE);

        mPaintLine = new Paint();                //直线画笔
        mPaintLine.setAntiAlias(true);           //抗锯齿效果
        mPaintLine.setStrokeWidth(1);            //圆环的宽度
        mPaintLine.setColor(Color.WHITE);      //画笔颜色

        mPaintPath = new Paint();
        mPaintPath.setStyle(Paint.Style.FILL);   //填充模式
        mPaintPath.setAntiAlias(true);           //抗锯齿效果
        //mPaintPath.setStrokeWidth(0);            //画笔宽度

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
        circleXPointer = width / 2;                 //圆心 X坐标
        circleYPointer = height / 2 - 30;           //圆心 Y坐标

        /*for(int i=0;i<256;i++) {
            mPaintRect.setARGB(255,255-i,0,i);
            canvas.drawRect(100+i, 100, 101+i, 200, mPaintRect);
        }*/

        //初始化时间数据、潮汐数据和颜色数据
        int[] clocks = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};         //时间
        int[] tide = new int[]{130,150,165,130,130,100,100,200,120,110,135,128};     //潮汐数据
        int[] doughnutColors = new int[3];                              //颜色数据
        doughnutColors[0] = 0xFFFF0000;
        doughnutColors[1] = 0xFF0000FF;
        doughnutColors[2] = 0xFFFF0000;
        mPaintPath.setShader(new SweepGradient(circleXPointer, circleYPointer, doughnutColors, null));

        //添加点
        List<Point> points = new ArrayList<Point>();
        int angle = 0;     //小时数乘上30度
        float xPointer = 0;
        float yPointer = 0;

        //// TODO: 2016/10/13 两个for循环合并
        for(int i=0;i<clocks.length;i++){
            angle = clocks[i] * 30;     //小时数乘上30度
            xPointer = (float) (tide[i] * Math.sin(angle*Math.PI/180) + circleXPointer);
            yPointer = (float) (circleYPointer - tide[i] * Math.cos(angle*Math.PI/180));
            Point point = new Point(xPointer, yPointer);
            points.add(point);
            canvas.drawCircle(xPointer, yPointer, 1, mPaintCircle);
            canvas.drawLine(circleXPointer,circleYPointer,xPointer,yPointer,mPaintLine);
        }

        Path path = new Path();
        path.moveTo(points.get(0).getX(), points.get(0).getY());
        //计算每个点的切线控制点
        float delta = 30;           //控制点到节点的垂直距离
        for(int i = 0;i<points.size()-1;i++){
            angle = clocks[i] * 30;     //小时数乘上30度
            //计算始发端的下行控制点
            float startPointDownControlX = points.get(i).getX() + (float)Math.cos(angle*Math.PI/180)*delta;
            float startPointDownControlY = points.get(i).getY() + (float)Math.sin(angle * Math.PI / 180)*delta;
            //计算终点端的上行控制点
            float endPointUpControlX = points.get(i+1).getX() - (float)Math.cos(angle*Math.PI/180)*delta;
            float endPointUpControlY = points.get(i+1).getY() - (float)Math.sin(angle * Math.PI / 180)*delta;

            //计算始发端的上行控制点
            float startPointUpControlX = points.get(i).getX() - (float)Math.cos(angle*Math.PI/180)*delta;
            float startPointUpControlY = points.get(i).getY() - (float)Math.sin(angle * Math.PI / 180)*delta;
            //绘制控制线
            //canvas.drawLine(startPointUpControlX,startPointUpControlY,startPointDownControlX,startPointDownControlY,mPaintLine);
            //绘制三阶贝塞尔曲线
            path.cubicTo(startPointDownControlX,startPointDownControlY,endPointUpControlX,endPointUpControlY,points.get(i+1).getX(),points.get(i+1).getY());
        }
        path.close();

        //布尔运算去掉中间部分 API19
        //Path circlePath = new Path();
        //circlePath.addCircle(circleXPointer,circleYPointer,50,Path.Direction.CW);
        //path.op(circlePath, Path.Op.DIFFERENCE);
        canvas.drawPath(path,mPaintPath);
        canvas.drawCircle(circleXPointer,circleYPointer,80,mPaintCircle);

        /*for(int i=0;i<clocks.length-1;i++) {
            //计算控制点
            angle = (clocks[i] * 30 + clocks[i+1] * 30)/2;     //小时数乘上30度
            float controlPointerX = (float) (((tide[i] + tide[i+1])/3)* Math.sin(angle*Math.PI/180) + circleXPointer);
            float controlPointerY = (float) (circleYPointer - ((tide[i] + tide[i+1])/3) * Math.cos(angle*Math.PI/180));
            canvas.drawCircle(controlPointerX, controlPointerY, 1, mPaintCircle);
            //绘制二阶贝塞尔曲线
            path.quadTo(controlPointerX, controlPointerY, points.get(i+1).getX(), points.get(i+1).getY());
        }*/

        /*float mFirstMultiplier = 0.3f;
        float mSecondMultiplier = 1 - mFirstMultiplier;
        for (int b = 0; b < length; b += 2) {
            int nextIndex = b + 2 < length ? b + 2 : b;
            int nextNextIndex = b + 4 < length ? b + 4 : nextIndex;
            calc(points, p1, b, nextIndex, mSecondMultiplier);
            p2.setX(points.get(nextIndex));
            p2.setY(points.get(nextIndex + 1));
            calc(points, p3, nextIndex, nextNextIndex, mFirstMultiplier);
            //canvas.drawCircle(p1.getX(), p1.getY(), 2, mPaintPath);
            //canvas.drawCircle(p2.getX(), p2.getY(), 2, mPaintPath);
            //canvas.drawCircle(p3.getX(), p3.getY(), 2, mPaintPath);

            path.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
        }
        path.close();
        canvas.drawPath(path,mPaintPath);*/



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
