package com.example.samsung.p0951_myapp2servicebackpendingintent;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    private String message = "";

    final int TASK1_CODE = 1,
            TASK2_CODE = 2,
            TASK3_CODE = 3;
    public final static int STATUS_START = 100,
                            STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time",
                               PARAM_PINTENT = "pendingIntent",
                               PARAM_RESULT = "result";

    TextView tvTask1, tvTask2, tvTask3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask1.setText(R.string.task1);
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask2.setText(R.string.task2);
        tvTask3 = (TextView) findViewById(R.id.tvTask3);
        tvTask3.setText(R.string.task3);
    }

    public void onClickStart(View view) {

        PendingIntent pendingIntent;
        Intent intent, nullIntent = new Intent();

        //Создание PendingIntent для Task1
        pendingIntent = createPendingResult(TASK1_CODE, nullIntent, 0);
        /**Создание Intent для вызова сервиса,
         * помещение в него параметра времени
         * и созданного pendingIntent
         */
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_PINTENT, pendingIntent);
        //Старт сервиса
        startService(intent);

        //Создание PendingIntent для Task2
        pendingIntent = createPendingResult(TASK2_CODE, nullIntent, 0);
        /**Создание Intent для вызова сервиса,
         * помещение в него параметра времени
         * и созданного pendingIntent
         */
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_PINTENT, pendingIntent);
        //Старт сервиса
        startService(intent);

        //Создание PendingIntent для Task3
        pendingIntent = createPendingResult(TASK3_CODE, nullIntent, 0);
        /**Создание Intent для вызова сервиса,
         * помещение в него параметра времени
         * и созданного pendingIntent
         */
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_PINTENT, pendingIntent);
        //Старт сервиса
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        message = "requestCode = " + requestCode + ", resultCode = " + resultCode;
        Log.d(LOG_TAG, message);
        //Перехват сообщений о старте задач
        if (resultCode == STATUS_START) {

            switch (resultCode) {

                case TASK1_CODE :
                    tvTask1.setText(getString(R.string.task1) + getString(R.string._start));
                    break;
                case TASK2_CODE :
                    tvTask2.setText(getString(R.string.task2) + getString(R.string._start));
                    break;
                case TASK3_CODE :
                    tvTask3.setText(getString(R.string.task3) + getString(R.string._start));
                    break;
                default:
                    break;
            }

        }
        //Перехват сообщений об остановке задач
        if (requestCode == STATUS_FINISH) {

            int result = data.getIntExtra(PARAM_RESULT, 0);

            switch (requestCode) {

                case TASK1_CODE :
                    tvTask1.setText(getString(R.string.task1) + getString(R.string._finish) + result);
                    break;
                case TASK2_CODE :
                    tvTask2.setText(getString(R.string.task2) + getString(R.string._finish) + result);
                    break;
                case TASK3_CODE :
                    tvTask3.setText(getString(R.string.task3) + getString(R.string._finish) + result);
                    break;
                default:
                    break;
            }

        }
    }
}
