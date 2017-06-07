package com.example.administrator.handmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);

//        layout = new FrameLayout(this);

        // 1. 보드를 새로 생성한다.
        DrawingBoard board = new DrawingBoard(this);
        // 2. 붓을 만들어서 보드에 담는다.
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        board.setPaint(paint);
        // 3. 생성된 보드를 화면에 세팅
        setContentView(board);
//        layout.addView(board);
    }

    class DrawingBoard extends View {

        Paint paint;
        Path path;

        public DrawingBoard(Context context){
            super(context);
            path = new Path();
        }

        // 붓을 만드는
        public void setPaint(Paint paint){
            this.paint = paint;
        }

        // 일단 그리는 일은 여기서 한다. 다른 일 하지 말 것!!
        @Override
        public void onDraw(Canvas canvas){
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event){
            // 내가 터치한 좌표를 꺼낸다.
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x,y);  // 그리지 않고 이동한다
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    // 궁금하면 토스트로 띄워보면 되는군!!
                    break;
                case MotionEvent.ACTION_UP:
                    path.lineTo(x,y);
                    break;
            }

            // 화면을 그린 후 화면을 갱신해서 반영해 준다.
            invalidate();

            // true 값을 리턴하면 연속해서 계속 받겠다는 의미이다.
            // false 값은 한개만 리턴하겠다는 뜻이다.
            // onTouchEvent 가 호출되지 않는다.
            return true;
        }

    }

}
