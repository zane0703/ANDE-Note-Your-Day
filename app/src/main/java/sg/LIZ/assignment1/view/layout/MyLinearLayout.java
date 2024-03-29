/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sg.LIZ.assignment1.R;

public final class MyLinearLayout extends LinearLayout {
    private final GestureDetector GESTURE_DETECTOR;
    private final onSetMonth MAIN_ACTIVITY;
    public  MyLinearLayout(@NonNull Context context) {
        this(context,null,0,0);
    }
    public MyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0,0);
    }

    public MyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }
    public MyLinearLayout(@NonNull Context context,@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        GESTURE_DETECTOR = new GestureDetector(context,  new GestureListener());
        MAIN_ACTIVITY =(onSetMonth) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.main_content);
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
                            MAIN_ACTIVITY.toLast(1);
                        } else {
                            MAIN_ACTIVITY.toNext(1);
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
