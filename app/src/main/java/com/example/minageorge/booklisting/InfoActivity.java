package com.example.minageorge.booklisting;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.imagee)
    ImageView mImageView;

    @BindView(R.id.namee)
    TextView title_text;

    @BindView(R.id.author)
    TextView auth_text;

    @BindView(R.id.dpc)
    TextView pdc_text;

    @BindView(R.id.desc)
    TextView desc_text;

    @BindView(R.id.price)
    TextView price_text;

    @BindView(R.id.rating)
    RatingBar mRatingBar;

    private String title;
    private String date;
    private String imag;
    private String authors;
    private String publisher;
    private String catog;
    private String desc;
    private String rate;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book Info");

        title = getIntent().getStringExtra("name");
        imag = getIntent().getStringExtra("imag");
        authors = getIntent().getStringExtra("auth");
        date = getIntent().getStringExtra("date");
        publisher = getIntent().getStringExtra("publisher");
        catog = getIntent().getStringExtra("cat");
        desc = getIntent().getStringExtra("desc");
        rate = getIntent().getStringExtra("rate");
        price = getIntent().getStringExtra("price");

        mRatingBar.setRating(Float.parseFloat(rate));
        desc_text.setText(desc);
        price_text.setText(price);
        pdc_text.setText(publisher + "," + date + "-" + catog);
        title_text.setText(title);
        auth_text.setText(authors);
        try {
            Picasso.with(this).load(imag).into(mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            startActivity(new Intent(InfoActivity.this, ResultsActivity.class));
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(InfoActivity.this, ResultsActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
