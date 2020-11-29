package sg.LIZ.assignment1.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import sg.LIZ.assignment1.view.activity.MainActivity;

import androidx.annotation.Nullable;

public final class MyLinearLayout extends LinearLayout {
    private final GestureDetector GESTURE_DETECTOR;
    private final MainActivity MAIN_ACTIVITY;
    public  MyLinearLayout( Context context) {
        this(context,null,0,0);
    }
    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0,0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }
    public MyLinearLayout(Context context,@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        GESTURE_DETECTOR = new GestureDetector(context,  new GestureListener());
        MAIN_ACTIVITY = (MainActivity) context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return GESTURE_DETECTOR.onTouchEvent(event);
    }
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            MAIN_ACTIVITY.toLastMonth(1);
                        } else {
                            MAIN_ACTIVITY.toNextMonth(1);
                        }
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}
