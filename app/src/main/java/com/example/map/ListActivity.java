package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    //Copy images to res/drawable then call R.drawable."name",
    int[] images = {R.drawable.toilet_room,
            R.drawable.toilet_room,
            R.drawable.toilet_room1,
            R.drawable.toilet_room1};
    //Copy brief description of building names, distance, and rate
    String[] Names = {"Koffler", "Chem", "GS", "Palace"};
    String[] Distance = {"1.2ft", "12ft", "3.4ft", "4.6ft"};
    String[] Rate = {"4.7", "4.8", "5.0", "3,6"};

   ListView mlistView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mlistView=(ListView)findViewById(R.id.listView);

        CustomAdaptor customAdaptor = new CustomAdaptor();
        mlistView.setAdapter(customAdaptor);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, MapsActivity.class);
                startActivity(intent);
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
