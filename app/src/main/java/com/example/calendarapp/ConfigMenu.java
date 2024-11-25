package com.example.calendarapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private EditText noteEditText;
    private SwitchMaterial alertSwitch;

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

        // Viewの参照取得
        TextView dateTextView = findViewById(R.id.dateTextView);
        startTimeText = findViewById(R.id.startDateTextView);
        endTimeText = findViewById(R.id.endDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        alertSwitch = findViewById(R.id.material_switch);
        ImageView alertIcon = findViewById(R.id.alertIcon);

        // Intentからデータを取得
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("date");
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String note = intent.getStringExtra("note");
        boolean isAlertEnabled = intent.getBooleanExtra("isAlertEnabled", false);

        // 取得したデータをセット
        dateTextView.setText(selectedDate);
        if (startTime != null) {
            startTimeText.setText(startTime);
        }
        if (endTime != null) {
            endTimeText.setText(endTime);
        }
        if (note != null) {
            noteEditText.setText(note);
        }
        alertSwitch.setChecked(isAlertEnabled);
        alertIcon.setImageResource(isAlertEnabled ? R.drawable.ic_alert : R.drawable.ic_alert_close);

        // アラートスイッチの切り替え
        alertSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alertIcon.setImageResource(isChecked ? R.drawable.ic_alert : R.drawable.ic_alert_close);
            alertIcon.setVisibility(View.VISIBLE);
        });

        // 時間選択ボタンの設定
        findViewById(R.id.startTimeButton).setOnClickListener(v -> showTimePicker(startTimeText));
        findViewById(R.id.endTimeButton).setOnClickListener(v -> showTimePicker(endTimeText));

        // 保存ボタン
        findViewById(R.id.saveTimeButton).setOnClickListener(v -> saveAndReturn());

        // 戻るボタン
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }


    private void showTimePicker(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            targetTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, currentHour, currentMinute, true).show();
    }

    private void saveAndReturn() {
        String selectedDate = getIntent().getStringExtra("selectedDate"); // 日付を取得
        String startTime = startTimeText.getText().toString();
        String endTime = endTimeText.getText().toString();
        String note = noteEditText.getText().toString();
        boolean isAlertEnabled = alertSwitch.isChecked();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("date", selectedDate); // 日付を含める
        resultIntent.putExtra("startTime", startTime);
        resultIntent.putExtra("endTime", endTime);
        resultIntent.putExtra("note", note);
        resultIntent.putExtra("isAlertEnabled", isAlertEnabled);

        setResult(RESULT_OK, resultIntent); // 結果をセット
        finish(); // アクティビティを終了
    }

}
