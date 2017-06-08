package com.example.administrator.handmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class DrawBoard1 extends AppCompatActivity {

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
                    case R.id.erase:
                        color = Color.BLUE;
//                        DrawBoard1.this.makePath();
                        DrawBoard1.this.board.invalidate();
                        break;
                    case R.id.red:
                        color = Color.RED;
                        DrawBoard1.this.board.invalidate();
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
            DrawBoard1.this.setStroke(progress);
            DrawBoard1.this.board.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            DrawBoard1.this.board.invalidate();
        }
    };

    // TODO brush 와 path 세트로 만들어 주기

    class DrawingBoard extends View {  // 핵심은 일련의 과정을 매우 상세하게 파악해서 어디서 선언하고, 어디서 호출하고, 어디서 저장하고, 어디서 불러올지 섬세하게 다뤄주는 것!!

        Paint paint;
        Path path;
        List<Path> pathes;
        List<Paint> paints;

        public DrawingBoard(Context context){
            super(context);
            Log.i("Main", "생성자");
             pathes = new ArrayList<>();
            paints = new ArrayList<>();
            path = new Path(); // 초기화 -- 처음 쓰이는 path
            paint = new Paint(); // 초기화
        }

        // 일단 그리는 일은 여기서 한다. 다른 일 하지 말 것!!
        @Override
        public void onDraw(Canvas canvas){ // 이 메소드는 가장 마지막에 호출되어 그림을 그려주는 역할을 한다. draw 가 여기 없으면 그려지지 않음.
            path = new Path();  // 이해1 : 미리 만들어진 Path 객체가 없으면 터치 이벤트는 발생하지만 그림이 그려지지 않는다. 즉, 이미 만들어진 path 에 값을 넣어주는 형식이군
            paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(progress);
            paint.setAntiAlias(true);

            // 찌그러지는 것 처리하기
            paint.setStrokeJoin(Paint.Join.ROUND);  // 각 path 가 서로 만날 때 둥글게 연결함.
            paint.setStrokeCap(Paint.Cap.ROUND); //
            paint.setDither(true);  // 보정

            for(int i=0; i<pathes.size(); i++){
                canvas.drawPath(pathes.get(i), paints.get(i));  // 결국 그려지는 것은 이곳이기 때문에 배열에 저장해 둔 path, paint 객체를 여기서 그려준다.
            }
            Log.i("Main", "onDraw");
        }

        private void setBrush(){
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(progress);
            paint.setAntiAlias(true);

            // 찌그러지는 것 처리하기
            paint.setStrokeJoin(Paint.Join.ROUND);  // 각 path 가 서로 만날 때 둥글게 연결함.
            paint.setStrokeCap(Paint.Cap.ROUND); //
            paint.setDither(true);  // 보정
        }

        // 움직일 때마다 계속 onTouchEvent 메소드가 호출되고, UP 까지 값을 계속 갱신해 주는 것이로군.
        // 로그값으로 어떻게 onTouchEvent 작동하는지 잘 살펴보자
        @Override
        public boolean onTouchEvent(MotionEvent event){
            // 내가 터치한 좌표를 꺼낸다.
            float x = event.getX();
            float y = event.getY();
            Log.i("Main", "Touch Event");
//            path = new Path();  // 안됨
            switch (event.getAction()){
                // TODO 이곳에 새로운 path 와 새로운 paint 를 생성해 줘야 하는 것이었다
                // DOWN 에서 만들어 진 것이 MOVE 에도 쓰여야 하기 때문에 전역 변수로 선언한다.
                case MotionEvent.ACTION_DOWN:
//                    canvas.drawPath(path, paint); // 안됨
                    path.moveTo(x,y);  // 그리지 않고 이동한다
                    Log.i("Main", "ACTION_DOWN");
                    return true;  // 이거는 뭐지
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    Log.i("Main", "ACTION_MOVE");
                    return true;
//                case MotionEvent.ACTION_POINTER_UP:
//                    // 궁금하면 토스트로 띄워보면 되는군!!
//                    break;
                case MotionEvent.ACTION_UP:
                    path.lineTo(x,y);
                    pathes.add(path); // 결국 마지막까지 그려진 path 와 paint 를 저장해 준다.
                    paints.add(paint);
                    Log.i("Main", "ACTION_UP");
                    break;
            }

//            for(int i=0; i< pathes.size(); i++){   // 이곳은 그리는 곳이 아니다.
//                canvas.drawPath(pathes.get(i), paint);
//            }

            // 화면을 그린 후 화면을 갱신해서 반영해 준다.
            invalidate();

            // true 값을 리턴하면 연속해서 계속 받겠다는 의미이다.
            // false 값은 한개만 리턴하겠다는 뜻이다.
            // onTouchEvent 가 호출되지 않는다.
            return true;
        }

    }

}
