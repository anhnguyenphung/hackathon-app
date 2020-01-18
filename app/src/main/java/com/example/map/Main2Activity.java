package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView =(ListView) findViewById(R.id.listview);

        ArrayList<String> arrayList = new ArrayList<>();
        // Adding item list
        arrayList.add("rating number 1");
        arrayList.add("rating number 2");
        arrayList.add("rating number 3");
        arrayList.add("rating number 4");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(Main2Activity.this, MapsActivity.class);
                Main2Activity.this.startActivity(myIntent);
            }
        });
    }

}
