package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button btnStart, btnPause, btnResume, btnStop;

    private Handler handler = new Handler();
    private long startTime = 0L, timeInMillis = 0L, timeSwapBuff = 0L, updateTime = 0L;
    private boolean isRunning = false;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMillis;

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updateTime % 1000);

            timerTextView.setText(String.format("%02d:%02d:%03d", mins, secs, milliseconds));
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timer);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        btnStop = findViewById(R.id.btn_stop);

        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnStop.setEnabled(false);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                isRunning = true;
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnResume.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwapBuff += timeInMillis;
                handler.removeCallbacks(updateTimerThread);
                isRunning = false;
                btnPause.setEnabled(false);
                btnResume.setEnabled(true);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                isRunning = true;
                btnPause.setEnabled(true);
                btnResume.setEnabled(false);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(updateTimerThread);
                startTime = 0L;
                timeInMillis = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                timerTextView.setText("00:00:000");
                isRunning = false;
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnStop.setEnabled(false);
            }
        });
    }
}