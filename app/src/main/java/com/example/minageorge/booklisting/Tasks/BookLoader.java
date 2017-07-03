package com.example.minageorge.booklisting.Tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.minageorge.booklisting.network.CheckConnection;
import com.example.minageorge.booklisting.pojos.BooksList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mina george on 27-Jun-17.
 */

public class BookLoader extends AsyncTaskLoader<List<BooksList>> {
    private List<BooksList> cachedData;
    public static final String ACTION = "com.loaders.FORCE";
    private Bundle mBundle;
    private int status;
    private String Data;
    private List<BooksList> datalist = new ArrayList<>();
    private BooksList bl;
    private Context mContext;


    public BookLoader(Context context, Bundle bundle) {
        super(context);
        this.mBundle = bundle;
        this.mContext = context;
    }

    @Override
    protected void onStartLoading() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(ACTION);
        manager.registerReceiver(mBroadcastReceiver, filter);

        if (cachedData == null) {
            forceLoad();
        } else {
            super.deliverResult(cachedData);
        }
    }

    @Override
    public List<BooksList> loadInBackground() {
        if (new CheckConnection(mContext).isconnected()) {
            try {
                URL url = new URL(mBundle.getString("url"));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();
                status = httpURLConnection.getResponseCode();
                if (status == 200) {
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    Data = Stream2String(in);
                    httpURLConnection.disconnect();
                    JSONObject jsonObject = new JSONObject(Data);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        bl = new BooksList();
                        if (obj.getJSONObject("volumeInfo").has("imageLinks")) {
                            if (obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").has("thumbnail")) {
                                bl.setThumbnail(obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
                            }

                            bl.setTitle(obj.getJSONObject("volumeInfo").getString("title"));

                            if (obj.getJSONObject("volumeInfo").has("publishedDate")) {
                                bl.setPublishedDate(obj.getJSONObject("volumeInfo").getString("publishedDate"));
                            }
                            if (obj.getJSONObject("volumeInfo").has("publisher")) {
                                bl.setPublisher(obj.getJSONObject("volumeInfo").getString("publisher"));
                            }
                            if (obj.getJSONObject("volumeInfo").has("description")) {
                                bl.setDescription(obj.getJSONObject("volumeInfo").getString("description"));
                            }

                            if (obj.getJSONObject("volumeInfo").has("categories")) {
                                JSONArray jsonArray2 = obj.getJSONObject("volumeInfo").getJSONArray("categories");
                                ArrayList<String> Categories = new ArrayList<String>();
                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    Categories.add(jsonArray2.getString(j));
                                }
                                bl.setCategories(Categories);
                            }

                            if (obj.getJSONObject("volumeInfo").has("authors")) {
                                JSONArray jsonArray2 = obj.getJSONObject("volumeInfo").getJSONArray("authors");
                                ArrayList<String> authors = new ArrayList<String>();
                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    authors.add(jsonArray2.getString(j));
                                }
                                bl.setAuthors(authors);
                            }
                            if (obj.getJSONObject("volumeInfo").has("averageRating")) {
                                bl.setAverageRating(obj.getJSONObject("volumeInfo").getDouble("averageRating"));
                                Log.d("loader>>>>>", String.valueOf(bl.getAverageRating()));

                            }

                            if (obj.getJSONObject("saleInfo").has("listPrice")) {
                                bl.setAmount(obj.getJSONObject("saleInfo").getJSONObject("listPrice").getDouble("amount"));
                                bl.setCurrencyCode(obj.getJSONObject("saleInfo").getJSONObject("listPrice").getString("currencyCode"));
                            }
                        }
                        datalist.add(bl);
                    }
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            datalist.clear();
        }
        return datalist;
    }

    @Override
    public void deliverResult(List<BooksList> data) {
        cachedData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };

    public String Stream2String(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String Text = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                Text += line;
            }
            inputStream.close();
        } catch (Exception e) {
        }
        return Text;
    }
}