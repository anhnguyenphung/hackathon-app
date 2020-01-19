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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    /*
     * Class that represents an entry for view
     */
    protected class Entry
    {
        protected Place place;
        protected int image;
        public Entry(Place p, int i)
        {
            place = p;
            image = i;
        }
    }
    ArrayList<Entry> entries = new ArrayList<Entry>();

    /* Only examples */
    //Copy images to res/drawable then call R.drawable."name",
    int[] images = {R.drawable.toilet_room,
            R.drawable.bathroom_download,
            R.drawable.toilet_room1,
            R.drawable.bathroom_fancy,
            R.drawable.bathroom_download,
            R.drawable.toilet_room1
    };
    //Copy brief description of building names, distance, and rate
    String[] Names = {"Koffler", "Chem", "GS", "Palace", "Atmospheric", "Psych"};
    String[] Distance = {"1.2ft", "12ft", "3.4ft", "4.6ft", "1.1ft","5.5ft"};
    String[] Rate = {"4.7", "4.8", "3.6", "5.0", "3.9","3.6"};
    /* End examples */

    protected void addExamples()
    {
        for(int i = 0; i < images.length; i++)
        {
            List<Double> meanScores = new ArrayList<Double>();
            for(int j = 0; j < 4; j++)
            {
                meanScores.add(Double.parseDouble(Rate[i]));
            }
            List<Review> reviews = new ArrayList<Review>();
            Review r = Review.getEmpty();
            r.ratingCategories = meanScores;
            reviews.add(r);
            entries.add(new Entry(new Place("Koffler", "Student Union",
                    new double[]{0.0,0.0},
                    meanScores,reviews),images[i]));
        }
    }
   ListView mlistView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        addExamples();

        mlistView=(ListView)findViewById(R.id.listView);

        CustomAdaptor customAdaptor = new CustomAdaptor();
        mlistView.setAdapter(customAdaptor);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, RatingActivity.class);
                MapsActivity.targetIndex = position;
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
            //return entries.size();
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
