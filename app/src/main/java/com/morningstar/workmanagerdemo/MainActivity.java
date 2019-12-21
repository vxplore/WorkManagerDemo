package com.morningstar.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.morningstar.workmanagerdemo.events.OneTimeWorkEvent;
import com.morningstar.workmanagerdemo.events.WorkRecurringEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDeskHeight;
    private EditText editTextDeskPos;
    private CheckBox checkBoxYes;
    private LinearLayout parentLayout;
    private RecyclerView recyclerView;
    private Button nextButton;

    private CustomRecyclerAdapter adapter;

    private int count;
    private ArrayList<String> suggestionsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;
        suggestionsArrayList = new ArrayList<>();

        editTextDeskHeight = findViewById(R.id.editText_desk_height);
        editTextDeskPos = findViewById(R.id.editText_desk_pos);
        parentLayout = findViewById(R.id.parentlayout);
        checkBoxYes = findViewById(R.id.checkbox_yes);
        recyclerView = findViewById(R.id.recyclerView);
        nextButton = findViewById(R.id.next_activity_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EventBus.getDefault().register(this);

//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(RecurringWorkerClass.class, 15, TimeUnit.MINUTES)
//                .setInitialDelay(1, TimeUnit.MINUTES)
//                .addTag("Periodic Work")
//                .build();
//
//        WorkManager.getInstance(MainActivity.this).enqueue(periodicWorkRequest);

        checkBoxYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxYes.setChecked(true);
//                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(OneTimeWorkerClass.class)
//                            .addTag("One Time Work")
//                            .build();
//
//                    WorkManager.getInstance(MainActivity.this).enqueue(workRequest);
                }
                else
                    checkBoxYes.setChecked(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        // uncomment this line
        WorkManager.getInstance(MainActivity.this).cancelAllWork();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void workCompleted(WorkRecurringEvent workRecurringEvent){
        count += 1;
        suggestionsArrayList.add("Suggestion"+count);
        recyclerView.setAdapter(null);
        adapter = null;
        adapter = new CustomRecyclerAdapter(MainActivity.this, suggestionsArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void workCompleted(OneTimeWorkEvent oneTimeWorkEvent){
        suggestionsArrayList.add("User Away from desk");
        recyclerView.setAdapter(null);
        adapter = null;
        adapter = new CustomRecyclerAdapter(MainActivity.this, suggestionsArrayList);
        recyclerView.setAdapter(adapter);
    }
}