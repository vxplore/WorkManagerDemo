package com.morningstar.workmanagerdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.morningstar.workmanagerdemo.events.WorkRecurringEvent;

import org.greenrobot.eventbus.EventBus;

public class RecurringWorkerClass extends Worker {
    private Context context;

    public RecurringWorkerClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        EventBus.getDefault().post(new WorkRecurringEvent());
        return Result.success();
    }
}
