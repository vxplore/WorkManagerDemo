package com.morningstar.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    private EditText editTextInterval;
    private TextView textViewCounter;
    private Switch aSwitch;

    private int interval;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private CountDownTimer countDownTimer;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setupViews();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    startTimer();
                }
                else{
                    stopTimer();
                }
            }
        });
    }

    private void stopTimer() {

    }

    private void startTimer() {
        int delay = 0;
        String period = editTextInterval.getText().toString();
        if (period.isEmpty())
            interval = (10*60000);
        else
            interval = (Integer.parseInt(period) * 60000);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewCounter.setText(count + "");
                        ++count;
                    }
                });
            }
        }, delay, interval);
    }

    private void setupViews() {
        editTextInterval = findViewById(R.id.editText_interval_mins);
        textViewCounter = findViewById(R.id.textView_presence_count);
        aSwitch = findViewById(R.id.presence_switch);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        finish();
    }
}
