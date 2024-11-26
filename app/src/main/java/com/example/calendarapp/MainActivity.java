package com.example.calendarapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.provider.Settings;

public class
MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1;

    private Map<String, List<DayData>> dayDataMap = new HashMap<>();
    private ActivityResultLauncher<Intent> configMenuLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestScheduleExactAlarmPermission();

        createNotificationChannel(); // 通知チャンネルの作成

        CalendarView calendarView = findViewById(R.id.calendarView);

        // ConfigMenuからの結果を受け取るランチャー
        configMenuLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String date = result.getData().getStringExtra("date");
                String startTime = result.getData().getStringExtra("startTime");
                String endTime = result.getData().getStringExtra("endTime");
                String note = result.getData().getStringExtra("note");
                boolean isAlertEnabled = result.getData().getBooleanExtra("isAlertEnabled", false);

                // 日付に新しい予定を追加
                dayDataMap.putIfAbsent(date, new ArrayList<>());
                dayDataMap.get(date).add(new DayData(startTime, endTime, note, isAlertEnabled));

                // 日付にマークを付ける
                markCalendarDate(calendarView, date);

                // 通知を設定
                if (isAlertEnabled) {
                    scheduleNotification(date, startTime, note);
                }
            }

        });

        // カレンダー日付選択
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            showBottomMenu(selectedDate);
        });
    }

    private void requestScheduleExactAlarmPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "スケジュールされたアラームの許可が与えられました");
            } else {
                Log.d("Permission", "スケジュールされたアラームの許可が拒否されました");
                showPermissionDeniedDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    Log.d("Permission", "スケジュールされたアラームの許可が与えられました");
                } else {
                    Log.d("Permission", "スケジュールされたアラームの許可が拒否されました");
                    showPermissionDeniedDialog();
                }
            }
        }
    }

    /**
     * カレンダーにマークを付ける
     */
    private void markCalendarDate(CalendarView calendarView, String date) {
        // カレンダーに直接マークを付けるAPIがないため、サードパーティライブラリを利用するのが推奨
        // ここでは簡易的にデバッグログで代用
        Log.d("CalendarMark", "予定が追加された日: " + date);
    }

    /**
     * 下部メニューを表示
     */
    private void showBottomMenu(String date) {
        List<DayData> dataList = dayDataMap.get(date);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView dateTextView = bottomSheetView.findViewById(R.id.dateTextView);
        LinearLayout eventListLayout = bottomSheetView.findViewById(R.id.eventListLayout);
        Button addEventButton = bottomSheetView.findViewById(R.id.addEventButton);
        dateTextView.setText(date);

        // イベントをリストとして表示
        eventListLayout.removeAllViews();
        if (dataList != null && !dataList.isEmpty()) {
            for (DayData data : dataList) {
                View eventView = getLayoutInflater().inflate(R.layout.event_item, null);
                TextView eventText = eventView.findViewById(R.id.eventText);
                Button editButton = eventView.findViewById(R.id.editButton);
                Button deleteButton = eventView.findViewById(R.id.deleteButton);

                eventText.setText(String.format("開始: %s, 終了: %s, メモ: %s",
                        data.getStartTime(), data.getEndTime(), data.getNote()));

                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, ConfigMenu.class);
                    intent.putExtra("date", date);
                    intent.putExtra("startTime", data.getStartTime());
                    intent.putExtra("endTime", data.getEndTime());
                    intent.putExtra("note", data.getNote());
                    intent.putExtra("isAlertEnabled", data.isAlertEnabled());
                    configMenuLauncher.launch(intent);
                    bottomSheetDialog.dismiss();
                });

                // 削除ボタン
                deleteButton.setOnClickListener(v -> {
                    dataList.remove(data);
                    if (dataList.isEmpty()) {
                        dayDataMap.remove(date);
                    }
                    bottomSheetDialog.dismiss();
                });

                eventListLayout.addView(eventView);
            }
        } else {
            // 予定がない場合
            TextView noEventText = new TextView(this);
            noEventText.setText("予定がありません");
            eventListLayout.addView(noEventText);
        }

        // 新しい予定を追加
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfigMenu.class);
            intent.putExtra("selectedDate", date); // 日付を渡す
            configMenuLauncher.launch(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /**
     * 通知チャンネルを作成
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Reminder Channel";
            String description = "Channel for event reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("eventReminderChannel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 通知をスケジュール
     */
    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(String date, String startTime, String note) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("note", note);
        intent.putExtra("startTime", startTime); // startTimeを追加
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 開始時間をCalendarに変換
        Calendar calendar = Calendar.getInstance();
        String[] dateParts = date.split("/");
        String[] timeParts = startTime.split(":");
        calendar.set(Calendar.YEAR, Integer.parseInt(dateParts[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("権限が必要です")
                .setMessage("正確なアラームを設定する��めに、アプリに権限を付与してください。")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestScheduleExactAlarmPermission();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
}

