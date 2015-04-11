package com.qjy.accountnote.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qjy on 15-4-8.
 */
public class ArcChartView extends View {
    public ArcChartView(Context context) {
        this(context, null);
    }

    public ArcChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private Paint arcPaint;

    /**
     * 弧线的尺寸
     */
    private RectF arcRect;

    private float[] content;
    private int[] colors;


    /**
     * 通用的初始化方法
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        arcPaint = new Paint();
        arcPaint.setColor(Color.BLACK);
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(true);
        arcRect = new RectF(10, 10, 110, 110);

        content = new float[]{0.7f,0.2f,0.07f,0.08f};
        colors = new int[]{Color.GREEN,Color.RED,Color.YELLOW,Color.BLUE,Color.BLACK};

    }

    public void loadData(float[] content){
        this.content = content;
        invalidate();
    }

    public void loadData(double[] money,String str){
        double sum = 0;
        float[] currentcontent = new float[money.length];
        for (int i = 0; i < money.length; i++) {
            sum += money[i];
        }
        for (int i = 0; i < money.length; i++) {
            currentcontent[i] = (float)(money[i]/sum);
        }
        loadData(currentcontent);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //清除内容
        canvas.drawColor(Color.WHITE);


        //画一个弧线
        //第一个参数 弧线尺寸范围
        //第二个参数 起始角度
        //第三个参数 弧线角度
        //第四个参数是否连接中心点
        //第五个参数paint

        float startDegree = 0;
        for (int i = 0; i < content.length; i++) {
            arcPaint.setColor(colors[i]);
            float endDegree =  (content[i] * 360);
            canvas.drawArc(arcRect,startDegree, endDegree, true, arcPaint);
            startDegree = startDegree+endDegree;
        }
        arcRect = new RectF(40, 40, 80, 80);
        arcPaint.setColor(Color.WHITE);
        canvas.drawCircle(60, 60, 20, arcPaint);


    }
}
