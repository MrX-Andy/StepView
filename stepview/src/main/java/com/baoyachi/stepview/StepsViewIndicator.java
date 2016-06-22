package com.baoyachi.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期：16/6/22 14:15
 * <p>
 * 描述：StepsViewIndicator指示器
 */
public class StepsViewIndicator extends View
{

    //定义默认的高度
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

    private float mCompletedLineHeight;//完成线的高度
    private float mCircleRadius;//圆的半径

    private Drawable mCompleteIcon;//完成的默认图片
    private Drawable mAttentionIcon;//正在进行的默认图片
    private Drawable mDefaultIcon;//默认的背景图
    private float mCenterY;//该view的中间位置
    private float mLeftY;//左上方的Y位置
    private float mRightY;//右下方的位置

    private int mStepNum = 0;//当前有几部流程
    private float mLinePadding;

    private List<Float> mComplectedXPosition;//定义完成时当前view在左边的位置
    private Paint mUnCompletedPaint;//未完成Paint
    private Paint mCompletedPaint;//完成paint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);//定义默认未完成线的颜色
    private int mCompletedLineColor = Color.WHITE;//定义默认完成线的颜色
    private PathEffect mEffects;

    private int mComplectingPosition;//正在进行position
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener)
    {
        mOnDrawListener = onDrawListener;
    }

    /**
     * get小圆的半径
     *
     * @return
     */
    public float getCircleRadius()
    {
        return mCircleRadius;
    }

    public StepsViewIndicator(Context context)
    {
        this(context, null);
    }

    public StepsViewIndicator(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public StepsViewIndicator(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * init
     */
    private void init()
    {
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mComplectedXPosition = new ArrayList<>();//初始化

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距
        mLinePadding = 0.85f * defaultStepIndicatorNum;

        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//未完成的icon
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec))
        {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec))
        {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取中间的高度
        mCenterY = 0.5f * getHeight();
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        for(int i = 0; i < mStepNum; i++)
        {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离）*2）
            float paddingLeft = (getWidth() - mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding) / 2;
            mComplectedXPosition.add(paddingLeft + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }

        mOnDrawListener.ondrawIndicator();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //-----------------------画线---------------------------------------------------------------
        for(int i = 0; i < mComplectedXPosition.size() - 1; i++)
        {
            //前一个ComplectedXPosition
            final float preComplectedXPosition = mComplectedXPosition.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mComplectedXPosition.get(i + 1);

            if(i < mComplectingPosition)//判断在完成之前的所有点
            {
                //判断在完成之前的所有点，画完成的线，这里是矩形
                canvas.drawRect(preComplectedXPosition + mCircleRadius - 10, mLeftY, afterComplectedXPosition - mCircleRadius + 10, mRightY, mCompletedPaint);
            } else
            {
                mPath.moveTo(preComplectedXPosition + mCircleRadius, mCenterY);
                mPath.lineTo(afterComplectedXPosition - mCircleRadius, mCenterY);
                canvas.drawPath(mPath, mUnCompletedPaint);
            }
        }
        //-----------------------画线---------------------------------------------------------------


        //-----------------------画图标--------------------------------------------------------------
        for(int i = 0; i < mComplectedXPosition.size(); i++)
        {
            final float currentComplectedXPosition = mComplectedXPosition.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterY - mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius), (int) (mCenterY + mCircleRadius));
            if(i < mComplectingPosition)
            {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            } else if(i == mComplectingPosition && mComplectedXPosition.size() != 1)
            {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(currentComplectedXPosition, mCenterY, mCircleRadius * 1.1f, mCompletedPaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            } else
            {
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }
        }
        //-----------------------画图标--------------------------------------------------------------
    }

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getComplectedXPosition()
    {
        return mComplectedXPosition;
    }

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum)
    {
        this.mStepNum = stepNum;
        invalidate();
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    public void setComplectingPosition(int complectingPosition)
    {
        this.mComplectingPosition = complectingPosition;
        invalidate();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }


    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener
    {
        void ondrawIndicator();
    }
}
