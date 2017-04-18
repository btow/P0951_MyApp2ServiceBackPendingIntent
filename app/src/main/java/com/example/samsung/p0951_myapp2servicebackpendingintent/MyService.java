package com.example.samsung.p0951_myapp2servicebackpendingintent;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private final String LOG_TAG = "myLogs";
    private String message = "";

    private ExecutorService executorService;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        message = "MyService onCreate()";
        Log.d(LOG_TAG, message);
        //Выделение двух потоков под выполнение задач
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        message = "MyService onStartCommand()";
        Log.d(LOG_TAG, message);

        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        PendingIntent pendingIntent = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        MyRun myRun = new MyRun(time, startId, pendingIntent);
        executorService.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        message = "MyService onDestroy()";
        Log.d(LOG_TAG, message);
    }

    private class MyRun implements Runnable {

        int time, startId;
        PendingIntent pendingIntent;

        public MyRun(int time, int startId, PendingIntent pendingIntent) {

            this.time = time;
            this.startId = startId;
            this.pendingIntent = pendingIntent;
            message = getString(R.string.my_run) + startId + getString(R.string._create);
            Log.d(LOG_TAG, message);
        }

        @Override
        public void run() {

            message = getString(R.string.my_run) + startId + getString(R.string._start_time) + time;
            Log.d(LOG_TAG, message);
            try {
                //Сообщение о старте задачи
                pendingIntent.send(MainActivity.STATUS_START);
                //Эмуляция начала выполнения задачи
                TimeUnit.SECONDS.sleep(time);
                //Сообщение об окончании выполнения задачи
                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, time * 100);
                pendingIntent.send(MyService.this, MainActivity.STATUS_FINISH, intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }

        private void stop() {
            message = getString(R.string.my_run) + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId);
            Log.d(LOG_TAG, message);
        }
    }
}
