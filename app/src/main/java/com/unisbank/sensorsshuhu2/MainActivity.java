package com.unisbank.sensorsshuhu2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvTemperature;
    private Button btnSave, btnDelete;
    private ListView listView;
    private List<String> temperatureList;
    private TemperatureListAdapter adapter;

    private SensorManager sensorManager;
    private Sensor ambientTemperatureSensor;

    private TemperatureDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private float currentTemperature = 0.0f; // Menyimpan nilai suhu terkini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemperature = findViewById(R.id.tvTemperature);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);

        temperatureList = new ArrayList<>();
        adapter = new TemperatureListAdapter(this, temperatureList);
        listView.setAdapter(adapter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        dbHelper = new TemperatureDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTemperature();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTemperature();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ambientTemperatureSensor != null) {
            sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        loadSavedTemperatures();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ambientTemperatureSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentTemperature = event.values[0];
        tvTemperature.setText("Ambient Temperature: " + currentTemperature);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak digunakan dalam contoh ini
    }

    private void saveTemperature() {
        // Simpan suhu ke database
        String temperature = String.valueOf(currentTemperature);
        dbHelper.saveTemperature(db, temperature);

        // Tampilkan suhu di TextView
        tvTemperature.setText("Ambient Temperature: " + temperature);

        // Tambahkan suhu ke daftar suhu yang tersimpan
        temperatureList.add(temperature);
        adapter.notifyDataSetChanged();
    }

    private void deleteTemperature() {
        // Hapus semua suhu dari database
        dbHelper.deleteAllTemperatures(db);

        // Hapus suhu dari TextView
        tvTemperature.setText("Ambient Temperature: ");

        // Kosongkan daftar suhu yang tersimpan
        temperatureList.clear();
        adapter.notifyDataSetChanged();
    }

    private void loadSavedTemperatures() {
        temperatureList.clear();

        Cursor cursor = dbHelper.getAllTemperatures(db);
        if (cursor.moveToFirst()) {
            do {
                String temperature = cursor.getString(cursor.getColumnIndex(TemperatureDatabaseHelper.COLUMN_TEMPERATURE));
                temperatureList.add(temperature);
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
