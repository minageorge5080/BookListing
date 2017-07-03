package com.example.minageorge.booklisting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minageorge.booklisting.InfoActivity;
import com.example.minageorge.booklisting.R;
import com.example.minageorge.booklisting.pojos.BooksList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mina george on 27-Jun-17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private List<BooksList> mArrayList = new ArrayList<BooksList>();
    private Context mContext;
    private Intent mIntent;
    private Activity mActivity;

    public RecycleAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, final int position) {
        try {
            Picasso.with(mContext).load(mArrayList.get(position).getThumbnail()).into(holder.img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, InfoActivity.class);
                mIntent.putExtra("name", mArrayList.get(position).getTitle());
                mIntent.putExtra("imag", mArrayList.get(position).getThumbnail());
                mIntent.putExtra("date", mArrayList.get(position).getPublishedDate());
                if (!mArrayList.get(position).getDescription().equals("")) {
                    mIntent.putExtra("desc", mArrayList.get(position).getDescription());
                } else {
                    mIntent.putExtra("desc", "Not Available");
                }
                if (!mArrayList.get(position).getPublisher().equals("")) {
                    mIntent.putExtra("publisher", mArrayList.get(position).getPublisher());
                } else {
                    mIntent.putExtra("publisher", "");
                }
                mIntent.putExtra("rate", Double.toString(mArrayList.get(position).getAverageRating()));
                if (mArrayList.get(position).getAmount() != 0.0) {
                    String price = mArrayList.get(position).getAmount() + " " + mArrayList.get(position).getCurrencyCode();
                    mIntent.putExtra("price", price);
                } else {
                    mIntent.putExtra("price", "");
                }

                ArrayList<String> authors = mArrayList.get(position).getAuthors();
                if (authors != null) {
                    String newAuthors = "";
                    for (int i = 0; i < authors.size(); i++) {
                        newAuthors += authors.get(i) + ",";
                    }
                    mIntent.putExtra("auth", newAuthors);
                }
                ArrayList<String> Categories = mArrayList.get(position).getCategories();
                if (!Categories.equals(null)) {
                    String newCategories = "";
                    for (int i = 0; i < Categories.size(); i++) {
                        newCategories += Categories.get(i) + ",";
                    }
                    mIntent.putExtra("cat", newCategories);
                }
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mActivity.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void swapdata(Collection<BooksList> data) {
        this.mArrayList.clear();
        this.mArrayList.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
