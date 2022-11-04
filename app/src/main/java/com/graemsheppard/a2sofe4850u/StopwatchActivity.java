package com.graemsheppard.a2sofe4850u;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends Activity {

    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private TextView txtStopwatch;

    private StopwatchService stopwatchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        stopwatchService = StopwatchService.getInstance();

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
        txtStopwatch = findViewById(R.id.txtStopwatch);

        btnStart.setOnClickListener(this::onStartClicked);
        btnStop.setOnClickListener(this::onStopClicked);
        btnReset.setOnClickListener(this::onResetClicked);

        stopwatchService.subscribe(this::onStopwatchChanged);
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtStopwatch.setText(stopwatchService.millisToString(stopwatchService.getMillis()));
        if (stopwatchService.isRunning()) {
            showPause();
        } else {
            showStart();
        }
    }

    private void onStartClicked(View v) {
        stopwatchService.start();
        showPause();
    }

    private void onStopClicked(View v) {
        stopwatchService.stop();
        showStart();
    }

    private void onResetClicked(View v) {
        stopwatchService.reset();
        showStart();
    }

    private void showPause() {
        btnStart.setVisibility(View.GONE);
        btnStart.setEnabled(false);
        btnStop.setVisibility(View.VISIBLE);
        btnStop.setEnabled(true);
    }

    private void showStart() {
        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(true);
        btnStop.setVisibility(View.GONE);
        btnStop.setEnabled(false);
    }

    private void onStopwatchChanged(long millis) {
        String elapsed = stopwatchService.millisToString(millis);
        runOnUiThread(() -> {
            txtStopwatch.setText(elapsed);
        });
    }
}