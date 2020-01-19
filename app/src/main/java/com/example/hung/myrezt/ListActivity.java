package com.example.hung.myrezt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    //Copy images to res/drawable then call R.drawable."name",
    int[] images = {R.drawable.toilet_room,
                    R.drawable.toilet_room};
    //Copy brief description of building names, distance, and rate
    String[] Names = {"Koffler", "Chem"};
    String[] Distance = {"1.2ft", "12ft"};
    String[] Rate = {"4.7", "4.8"};

    ListView mlistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mlistView=(ListView)findViewById(R.id.listView);

        CustomAdaptor customAdaptor = new CustomAdaptor();
        mlistView.setAdapter(customAdaptor);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When click on the list
                //To-do
            }
        });
    }

    class CustomAdaptor extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.customlayout, null);

            ImageView mImageView = view.findViewById(R.id.imageView3);
            TextView nTextView = view.findViewById(R.id.textView2);
            TextView dTextView = view.findViewById(R.id.textView3);
            TextView rTextView = view.findViewById(R.id.textView4);


            mImageView.setImageResource(images[position]);
            nTextView.setText(Names[position]);
            dTextView.setText(Distance[position]);
            rTextView.setText(Rate[position]);

            return view;
        }
    }
}
