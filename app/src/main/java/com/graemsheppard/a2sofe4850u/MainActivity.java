package com.graemsheppard.a2sofe4850u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.graemsheppard.a2sofe4850u.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private static final int REQUEST_TIMEZONE = 1;

    private LinearLayout llButtons;

    private Button btnStopwatch;
    private Button btnTimezone;

    private TextClock txtClock;
    private TextView txtStopwatchPreview;

    private StopwatchService stopwatchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llButtons = findViewById(R.id.llButtons);
        btnStopwatch = findViewById(R.id.btnStopwatch);
        btnTimezone = findViewById(R.id.btnTimezone);
        txtClock = findViewById(R.id.txtClock);
        txtStopwatchPreview = findViewById(R.id.txtStopwatchPreview);

        btnStopwatch.setOnClickListener(this::onStopwatchClicked);
        btnTimezone.setOnClickListener(this::onTimezoneClicked);

        stopwatchService = StopwatchService.getInstance();
        stopwatchService.subscribe(this::onStopwatchChanged);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stopwatchService.isRunning()) {
            txtStopwatchPreview.setVisibility(View.VISIBLE);
        } else {
            txtStopwatchPreview.setVisibility(View.GONE);
        }
    }

    private void onStopwatchClicked(View v) {
        Intent intent = new Intent(this, StopwatchActivity.class);
        startActivity(intent);
    }

    private void onTimezoneClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), TimezoneActivity.class);
        startActivityForResult(intent, REQUEST_TIMEZONE);
    }

    private void onStopwatchChanged(long millis) {
        String elapsed = stopwatchService.millisToString(millis);
        runOnUiThread(() -> {
            txtStopwatchPreview.setText(elapsed);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TIMEZONE && resultCode != RESULT_CANCELED) {
            txtClock.setTimeZone(data.getStringExtra("timezone"));
            txtClock.refreshTime();
        }
    }
}