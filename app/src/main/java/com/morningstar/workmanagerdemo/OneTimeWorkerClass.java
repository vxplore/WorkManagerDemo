package com.morningstar.workmanagerdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.morningstar.workmanagerdemo.events.OneTimeWorkEvent;

import org.greenrobot.eventbus.EventBus;

public class OneTimeWorkerClass extends Worker {

    public OneTimeWorkerClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        EventBus.getDefault().post(new OneTimeWorkEvent());
        return Result.success();
    }
}
