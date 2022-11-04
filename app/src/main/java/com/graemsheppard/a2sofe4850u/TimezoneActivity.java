package com.graemsheppard.a2sofe4850u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TimezoneActivity extends Activity {

    private ListView lsTimezones;

    private ArrayAdapter<String> timezoneAdapter;
    private String[] timezones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timezone);

        lsTimezones = findViewById(R.id.lsTimezones);

        timezones = getResources().getStringArray(R.array.timezones);
        timezoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timezones);
        lsTimezones.setAdapter(timezoneAdapter);

        lsTimezones.setOnItemClickListener(this::onTimezoneClicked);
    }

    public void onTimezoneClicked(AdapterView<?> listView, View item, int i, long l) {
        Intent result = new Intent();
        result.putExtra("timezone", "Canada/" + timezones[i]);
        setResult(RESULT_OK, result);
        finish();
    }
}