package com.example.calendarapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.Calendar;

public class ConfigMenu extends AppCompatActivity {

    private TextView startTimeText;
    private TextView endTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config_menu);

        // システムバーの余白を反映
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 日付情報を表示
        TextView dateTextView = findViewById(R.id.dateTextView);
        String selectedDate = getIntent().getStringExtra("selectedDate");
        dateTextView.setText(selectedDate);

        SwitchMaterial switchMaterial = findViewById(R.id.material_switch);
        ImageView alertIcon = findViewById(R.id.alertIcon);

        // アラートスイッチの切り替え
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alertIcon.setImageResource(isChecked ? R.drawable.ic_alert : R.drawable.ic_alert_close);
            alertIcon.setVisibility(View.VISIBLE);
        });

        // 時間表示用のTextViewを取得
        startTimeText = findViewById(R.id.startDateTextView);
        endTimeText = findViewById(R.id.endDateTextView);

        // 現在の時間を取得
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 開始時間の初期値を設定
        startTimeText.setText(String.format("%02d:%02d", currentHour, currentMinute));

        // 終了時間の初期値を設定（現在の時間から1時間後）
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);
        endTimeText.setText(String.format("%02d:%02d", endHour, endMinute));

        // 開始時間設定
        findViewById(R.id.startTimeButton).setOnClickListener(v -> showTimePicker(startTimeText));

        // 終了時間設定
        findViewById(R.id.endTimeButton).setOnClickListener(v -> showTimePicker(endTimeText));

        // 時間を保存するボタンのクリックリスナー
        findViewById(R.id.saveTimeButton).setOnClickListener(v -> saveTime());

        // 戻るボタン
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    /**
     * 時間選択ダイアログを表示する共通メソッド
     */
    private void showTimePicker(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            targetTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, currentHour, currentMinute, true).show();
    }

    /**
     * 入力された時間を保存する処理
     */
    private void saveTime() {
        String startTime = startTimeText.getText().toString();
        String endTime = endTimeText.getText().toString();

        // 未入力チェック
        if (startTime.equals("--:--") || endTime.equals("--:--")) {
            if (startTime.equals("--:--")) {
                startTimeText.setError("開始時間を入力してください");
            }
            if (endTime.equals("--:--")) {
                endTimeText.setError("終了時間を入力してください");
            }
            return;
        }

        // ログに保存（または他の保存処理）
        Log.d("ConfigMenu", "開始時間: " + startTime + ", 終了時間: " + endTime);

        // ユーザーフィードバック（簡易メッセージ）
        showToast("時間を保存しました！");
    }

    /**
     * トーストメッセージを表示
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
