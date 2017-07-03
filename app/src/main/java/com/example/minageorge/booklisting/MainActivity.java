package com.example.minageorge.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.minageorge.booklisting.network.CheckConnection;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.free_box)
    CheckBox mCheckBox;

    @BindView(R.id.edit_title)
    EditText mEditText;

    private String url;
    private boolean isfree = false;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    isfree = true;
                } else {
                    isfree = false;
                }
            }
        });
    }

    public void search(View view) {
        String title = mEditText.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please Type Book Title", Toast.LENGTH_SHORT).show();
        } else {
            if (new CheckConnection(this).isconnected()) {
                if (isfree) {
                    url = "https://www.googleapis.com/books/v1/volumes?q=" + title +
                            "&maxResults=40&filter=free-ebooks";
                } else {
                    url = "https://www.googleapis.com/books/v1/volumes?q=" + title +
                            "&maxResults=40";
                }
                mIntent = new Intent(MainActivity.this, ResultsActivity.class);
                mIntent.putExtra("url", url);
                startActivity(mIntent);
            } else {
                Toast.makeText(this, "Please Check Device Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
