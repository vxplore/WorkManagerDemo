package com.morningstar.workmanagerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.morningstar.workmanagerdemo.events.PresenceDetectedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    private EditText editTextInterval;
    private TextView textViewCounter;
    private Switch aSwitch;
    private Button resetButton;

    private int interval;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private Timer timer;

    private int count = 0;
    private boolean isUserPresent;

    private PresenceDetectedEvent event;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setupViews();

        EventBus.getDefault().register(this);

        timer = new Timer();
        isUserPresent = false;
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);

        count = sharedPreferences.getInt("count", 0);

        textViewCounter.setText(count + "");

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isUserPresent = true;
                    event = new PresenceDetectedEvent();
                    event.setPresent(isUserPresent);
                }
                else{
                    isUserPresent = false;
                    event = new PresenceDetectedEvent();
                    event.setPresent(isUserPresent);
                }
                EventBus.getDefault().post(event);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                textViewCounter.setText("0");
            }
        });
    }

    private void stopTimer() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", count);
        editor.apply();
    }

    private void startTimer() {
        count = sharedPreferences.getInt("count", 0);
        int delay = 60000;
        String period = editTextInterval.getText().toString();
        if (period.isEmpty())
            interval = (10*60000);
        else
            interval = (Integer.parseInt(period) * 60000);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count += 1;
                        textViewCounter.setText(count + "");
                    }
                });
            }
        }, delay, interval);
    }

    private void setupViews() {
        editTextInterval = findViewById(R.id.editText_interval_mins);
        textViewCounter = findViewById(R.id.textView_presence_count);
        aSwitch = findViewById(R.id.presence_switch);
        resetButton = findViewById(R.id.button_reset);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopTimer();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void presenceChanged(PresenceDetectedEvent event) {
        if (event.isPresent()) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
