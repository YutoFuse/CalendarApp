package com.example.calendarapp;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, List<DayData>> dayDataMap = new HashMap<>();
    private ActivityResultLauncher<Intent> configMenuLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            }
        });

        // カレンダー日付選択
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            showBottomMenu(selectedDate);
        });
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
}

