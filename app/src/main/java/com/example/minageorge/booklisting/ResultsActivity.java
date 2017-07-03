package com.example.minageorge.booklisting;

import android.app.ProgressDialog;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.minageorge.booklisting.Adapters.RecycleAdapter;
import com.example.minageorge.booklisting.Tasks.BookLoader;
import com.example.minageorge.booklisting.pojos.BooksList;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    LinearLayout empty_view;

    private BookLoader mBookLoader;
    private String url;
    private RecycleAdapter mRecycleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle bundle;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");
        url = getIntent().getStringExtra("url");

        dialog = new ProgressDialog(ResultsActivity.this);
        dialog.setMessage("loading..");
        dialog.setCancelable(false);
        mRecycleAdapter = new RecycleAdapter(getApplicationContext(), ResultsActivity.this);
        WindowManager wm = getWindowManager();

        Display d = wm.getDefaultDisplay();
        if (d.getWidth() > d.getHeight())
            mLayoutManager = new GridLayoutManager(this, 3);
        else
            mLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        bundle = new Bundle();
        bundle.putString("url", url);
        mBookLoader = (BookLoader) getSupportLoaderManager().initLoader(88, bundle, mListLoaderCallbacks);
        mRecyclerView.setAdapter(mRecycleAdapter);
        empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().restartLoader(88, bundle, mListLoaderCallbacks);
            }
        });
    }

    private LoaderManager.LoaderCallbacks<List<BooksList>> mListLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<BooksList>>() {
        @Override
        public Loader<List<BooksList>> onCreateLoader(int id, Bundle args) {
            dialog.show();
            empty_view.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return new BookLoader(getApplicationContext(), args);
        }


        @Override
        public void onLoadFinished(Loader<List<BooksList>> loader, List<BooksList> data) {
            dialog.cancel();
            mRecycleAdapter.swapdata(data);
            if (data.isEmpty()) {
                empty_view.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<BooksList>> loader) {
            mRecycleAdapter.swapdata(Collections.EMPTY_LIST);
            mBookLoader = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


}
