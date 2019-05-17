package com.example.jh.memoproject;

import android.content.Context;
import android.os.Handler;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class OnSwipeTouchListener implements OnTouchListener {

    private GestureDetector mGestureDetector = null;
    private GestureListener mGestureListener = null;
    int Selectionstart, Selectionend;
    AbsoluteSizeSpan[] FontSizeSpans;
    private EditText editor;
    private DiscreteSeekBar seekbar;

    public OnSwipeTouchListener(final Context context, EditText editor, DiscreteSeekBar seekBar) {
        mGestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(context, mGestureListener);
        this.editor = editor;
        this.seekbar = seekBar;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        int size = 14;
        final boolean result = mGestureDetector.onTouchEvent(motionEvent);
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
             FontSizeSpans = editor.getText().getSpans(0, editor.getText().length(), AbsoluteSizeSpan.class);

            if (FontSizeSpans.length>0){
                int SelectionStart = editor.getSelectionStart();

                for (AbsoluteSizeSpan span : FontSizeSpans) {
                    if (span instanceof AbsoluteSizeSpan) {

                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);

                        Log.i("Initialize Progress", "Found Row Size at: " +
                                ", Start: " + start +
                                ", Size: " + size +
                                ", End(s): " +end +
                                ", Selection Start" + SelectionStart);

                        if(start <= SelectionStart && SelectionStart<= end){
                            Log.i("Initialize Progress", "Found Row Size at: " +
                                    ", Start: " + start +
                                    ", Size: " + size +
                                    ", End(s): " +end +
                                    ", Selection Start" + SelectionStart);
                            size = span.getSize();

                            break;
                        }
                    }
                }
            }
            seekbar.setProgress(size);
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            Log.i("Initialize Progress", "Found Row Size at: " +
                    ", Size: " + size);
            seekbar.setProgress(size);
            seekbar.setPressed(true);
            seekbar.refreshDrawableState();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    seekbar.setPressed(false);
                    seekbar.refreshDrawableState();
                }
            },700);
        }
        return result;
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
            boolean result = false;
            try {
                final float diffY = e2.getY() - e1.getY();
                final float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                            result = true;
                        } else {
                            onSwipeLeft();
                            result = true;
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                            result = true;
                        } else {
                            onSwipeTop();
                            result = true;
                        }
                    }
                }
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Selectionstart = editor.getSelectionStart();
            Selectionend = editor.getSelectionEnd();

            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Selectionstart = editor.getSelectionStart();
            Selectionend = editor.getSelectionEnd();

            editor.setSelection(Selectionstart, Selectionend);

            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }


}