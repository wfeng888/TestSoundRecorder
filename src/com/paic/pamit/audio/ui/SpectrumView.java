package com.paic.pamit.audio.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.testsoundrecorder.R;
import com.paic.pamit.audio.ui.MyLinkedList.MyIterator;

public class SpectrumView extends SurfaceView
    implements android.view.SurfaceHolder.Callback
{
    private class SamplePoint extends Point
    {

        int amplitude;
        SamplePoint(int i, int j, int k)
        {
            super(j, k);
            amplitude = i;
        }
    }


    public SpectrumView(Context context)
    {
        this(context, null);
    }

    public SpectrumView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public SpectrumView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mAmpPoints = new MyLinkedList(new LinkedList());
        mIsDoingAnimation = false;
        mTmpPath = new Path();
        mStartPoint = new Point();
        mEndPoint = new Point();
        mTmpPoint = new Point();
        mTmpPoint1 = new Point();
        mTmpPoint2 = new Point();
        mPaint = new Paint();
        mIsFirstPoint = true;
        mLocation = new int[2];
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(1);
    }

    private void addToPoints(int i)
    {
        mAmpPoints.addFirst(new SamplePoint(i, 0, getHeight() / 2));
    }

    private int computePointX(int i, int j, int k)
    {
        return (int)((double)i + -Math.sin(((2D * (double)i) / (double)k) * 3.1415926535897931D) * (double)j);
    }

    private int computePointY(int i, int j, int k)
    {
        return (int)((double)i - (((double)i - (double)k / 2D) / 15.600000000000001D) * (double)j);
    }

    private void computeSamplePoints(int i)
    {
        int k = getWidth();
        double d = (double)k / 2D;
        double d1 = (double)getHeight() / 2D;
        double d2 = ((float)(SystemClock.elapsedRealtime() - mLastPointAddTime) / (float)INTERVAL_ADD_POINT) * (float)(mCurrentAmplitude - mLastAmplitude) + (float)mLastAmplitude;
        MyIterator iterator = mAmpPoints.iterator();
        while(iterator.hasNext()) 
        {
            SamplePoint samplepoint = (SamplePoint)iterator.next();
            int j;
            if(samplepoint.amplitude < 0)
                j = -1;
            else
                j = 1;
            samplepoint.x = samplepoint.x + i;
            samplepoint.y = (int)((((double)j * d2) / 50000D) * d1 * Math.sin(((1.0D - Math.abs(d - (double)samplepoint.x) / d) * 3.1415926535897931D) / 2D) + d1);
        }
        for(; mAmpPoints.size() > 0 && ((SamplePoint)mAmpPoints.getLast()).x >= k; mAmpPoints.removeLast())
            mIsFirstPoint = false;

    }

    private void drawAnimation()
    {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null)
        {
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            drawBG(canvas);
            int l = getWidth();
            int i1 = getHeight();
            mPaint.setAlpha(255);
            long l1 = (int)(SystemClock.elapsedRealtime() - mLastDrawTime);
            mLastDrawTime = SystemClock.elapsedRealtime();
            computeSamplePoints((int)((float)l1 * MOTION_SPEED));
            mPaint.setColor(-9079173);
            canvas.drawLine(0.0F, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
            int i = 0;
            int j = 0;
            MyIterator iterator;
            SamplePoint samplepoint2;
            while(j < 26) 
            {
                boolean flag = false;
                int k = i;
                SamplePoint samplepoint;
                SamplePoint samplepoint1;
                if(isGhostNewStep(j))
                {
                    
                    
                    if(j != 0)
                    {
                        i += 50 / j;
                        mPaint.setColor(-9079173);
                        mPaint.setStrokeWidth(1.0F);
                    } else
                    {
                        mPaint.setColor(-4933434);
                        mPaint.setStrokeWidth(2.0F);
                    }
                    mPaint.setAlpha(((26 - j) * 255) / 26);
                    mTmpPath.reset();
                    k = i;
                }
                samplepoint = null;
                samplepoint1 = null;
                if(mAmpPoints.size() > 0)
                {
                    int j1 = (j + 1) * 100;
                    iterator = mAmpPoints.iterator();
                    i = ((flag) ? 1 : 0);
                    Object obj;
                    do
                    {
                        obj = samplepoint;
                        if(!iterator.hasNext())
                            break;
                        samplepoint2 = (SamplePoint)iterator.next();
                        samplepoint = samplepoint2;
                        if(i == 0)
                        {
                            samplepoint1 = samplepoint2;
                        } else
                        {
                            mTmpPoint1.set(computePointX(((SamplePoint) (obj)).x, k, l), computePointY(((SamplePoint) (obj)).y, j, i1));
                            mTmpPoint2.set(computePointX(samplepoint.x, k, l), computePointY(samplepoint.y, j, i1));
                            drawBezier(mTmpPoint1, mTmpPoint2, mTmpPath, j1);
                        }
                        i++;
                    } while(true);
                    mTmpPoint1.set(computePointX(samplepoint1.x, k, l), computePointY(samplepoint1.y, j, i1));
                    drawBezier(mStartPoint, mTmpPoint1, mTmpPath, j1);
                    mTmpPoint1.set(computePointX(((SamplePoint) (obj)).x, k, l), computePointY(((SamplePoint) (obj)).y, j, i1));
                    if(!mIsFirstPoint)
                    {
                        drawBezier(mTmpPoint1, mEndPoint, mTmpPath, j1);
                    } else
                    {
                        mTmpPoint.set(((SamplePoint) (obj)).x + 150, getHeight() / 2);
                        drawBezier(mTmpPoint1, mTmpPoint, mTmpPath, j1);
                    }
                }
                if(isGhostNewStep(j + 1))
                    canvas.drawPath(mTmpPath, mPaint);
                j++;
                i = k;
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBG(Canvas canvas)
    {
        if(mBackground == null)
        {
            mBackground = getResources().getDrawable(R.drawable.shape_soundrecorder_bg);
            int i = mLocation[1];
            int j = getHeight();
            mBackground.setBounds(0, -2*i, getWidth(), i + j + i);
        }
        mBackground.draw(canvas);
    }

    private void drawBezier(Point point, Point point1, Path path, int i)
    {
        if(point == null || point1 == null || path == null)
            return;
        path.moveTo(point.x, point.y);
        int j = i;
        if(Math.abs(point1.x - point.x) < i * 2)
            j = Math.abs(point1.x - point.x) / 2;
        path.cubicTo(point.x + j, point.y, point1.x - j, point1.y, point1.x, point1.y);
    }

    private boolean isGhostNewStep(int i)
    {
        if(i != 0 && i < 26)
            if((i - 1) % 1 == 0)
            {
                if((i + 1) - 1 > 26)
                    return false;
            } else
            {
                return false;
            }
        return true;
    }

    public void setMaxAmplitude(int i)
    {
        boolean flag = true;
        if(i > mMaxAmplitude)
            mMaxAmplitude = i;
        if(SystemClock.elapsedRealtime() - mLastPointAddTime > (long)INTERVAL_ADD_POINT)
        {
            mLastPointAddTime = SystemClock.elapsedRealtime();
            if(mIsDoingAnimation)
            {
                int j = mMaxAmplitude;
                if(mLastBoolean)
                    i = -1;
                else
                    i = 1;
                addToPoints(i * j);
                mLastAmplitude = mCurrentAmplitude;
                mCurrentAmplitude = mMaxAmplitude;
                mMaxAmplitude = 0;
                if(mLastBoolean)
                    flag = false;
                mLastBoolean = flag;
                drawAnimation();
            }
        }
    }

    public void startAnimation()
    {
        if(!mIsDoingAnimation)
        {
            mIsDoingAnimation = true;
            mIsFirstPoint = true;
            mHandler.sendEmptyMessage(1);
            mLastDrawTime = SystemClock.elapsedRealtime();
        }
    }

    public void stopAnimation()
    {
        if(mIsDoingAnimation)
        {
            mIsDoingAnimation = false;
            mAmpPoints.clear();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k)
    {
        mStartPoint.x = 0;
        mStartPoint.y = getHeight() / 2;
        mEndPoint.x = getWidth();
        mEndPoint.y = getHeight() / 2;
        MOTION_SPEED = (float)getWidth() / 1000F;
        INTERVAL_ADD_POINT = 200;
        mPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        drawAnimation();
    }

    public void surfaceCreated(SurfaceHolder surfaceholder)
    {
        getLocationOnScreen(mLocation);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder)
    {
        stopAnimation();
    }

    private static float MOTION_SPEED = 0.8F;
    private int INTERVAL_ADD_POINT;
    private MyLinkedList mAmpPoints;
    private Drawable mBackground;
    private int mCurrentAmplitude;
    private final Point mEndPoint;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message message)
        {
            switch(message.what)
            {
            case 1: 
            	 if(mIsDoingAnimation)
                     sendEmptyMessageDelayed(1, 16L);
                 drawAnimation();
                break;
            default:
                return;
            }
        }
    };
    private boolean mIsDoingAnimation;
    private boolean mIsFirstPoint;
    private int mLastAmplitude;
    private boolean mLastBoolean;
    private long mLastDrawTime;
    private long mLastPointAddTime;
    private int mLocation[];
    private int mMaxAmplitude;
    private final Paint mPaint;
    private final Point mStartPoint;
    private final Path mTmpPath;
    private final Point mTmpPoint;
    private final Point mTmpPoint1;
    private final Point mTmpPoint2;
    private SurfaceHolder surfaceHolder;



}
