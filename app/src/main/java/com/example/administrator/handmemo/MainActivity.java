package com.example.administrator.handmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;
    RadioGroup radioGroup;
    SeekBar seekBar;
    DrawingBoard board;
//    Path path;

    int progress;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        layout = (FrameLayout) findViewById(R.id.layout);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.blue:
                        color = Color.BLUE;
//                        MainActivity.this.makePath();
                        MainActivity.this.board.invalidate();
                        break;
                    case R.id.red:
                        color = Color.RED;
                        MainActivity.this.board.invalidate();
                        break;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        // 1. 보드를 새로 생성한다.
        board = new DrawingBoard(this);
        // 2. 붓을 만들어서 보드에 담는다.
//        Paint paint = new Paint();
//        paint.setColor(Color.MAGENTA);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(progress);
//        board.setPaint(paint);
        // 3. 생성된 보드를 화면에 세팅
//        setContentView(board);
        layout.addView(board);

    }

    public void setStroke(int progress){
        this.progress = progress;
    }


//    public void makePath(){
//        path = new Path();
//    }

//    CompoundButton.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.blue:
//                        Toast.makeText(getBaseContext(), "확인", Toast.LENGTH_SHORT).show();
//                        break;
//
//                }
//        }
//    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            Toast.makeText(getBaseContext(), "확인", Toast.LENGTH_SHORT).show();
            MainActivity.this.setStroke(progress);
            MainActivity.this.board.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            MainActivity.this.board.invalidate();
        }
    };

    class DrawingBoard extends View {

        Paint paint;
        Path path;

        public DrawingBoard(Context context){
            super(context);
//            path = new Path();
        }

        // 붓을 만드는
//        public void setPaint(Paint paint){
//            this.paint = paint;
//        }

        // 일단 그리는 일은 여기서 한다. 다른 일 하지 말 것!!
        @Override
        public void onDraw(Canvas canvas){
            path = new Path();
            paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(progress);
            paint.setAntiAlias(true);
            canvas.drawPath(path, paint);
        }

        // 움직일 때마다 계속 onTouchEvent 메소드가 호출되고, UP 까지 값을 계속 갱신해 주는 것이로군.
        // 로그값으로 어떻게 onTouchEvent 작동하는지 잘 살펴보자
        @Override
        public boolean onTouchEvent(MotionEvent event){
            // 내가 터치한 좌표를 꺼낸다.
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                // DOWN 에서 만들어 진 것이 MOVE 에도 쓰여야 하기 때문에 전역 변수로 선언한다.
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
