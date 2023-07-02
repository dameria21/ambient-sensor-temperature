package com.unisbank.sensorsshuhu2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TemperatureListAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> temperatureList;

    public TemperatureListAdapter(Context context, List<String> temperatureList) {
        super(context, 0, temperatureList);
        this.context = context;
        this.temperatureList = temperatureList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_temperature, parent, false);
        }

        String temperature = temperatureList.get(position);

        TextView tvTemperature = convertView.findViewById(R.id.tvTemperature);
        tvTemperature.setText(temperature);

        return convertView;
    }
}

