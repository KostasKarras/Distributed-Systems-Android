package com.example.uni_tok;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchVideoAdapter extends BaseAdapter {

    private ArrayList<VideoInformation> videoList;
    private Context mContext;

    public SearchVideoAdapter(Context context, ArrayList<VideoInformation> videoList){
        this.videoList = videoList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_view, parent, false);

        TextView videoName = (TextView) convertView.findViewById(R.id.searchVideoTitle);
        TextView channelName = (TextView) convertView.findViewById(R.id.searchChannelName);
        TextView hashtags = (TextView) convertView.findViewById(R.id.searchHashtags);

        videoName.setText(videoList.get(position).getTitle());
        channelName.setText(videoList.get(position).getChannelName());

        StringBuilder stringBuilder = new StringBuilder();
        for (String hashtag : videoList.get(position).getHashtags())
            stringBuilder.append(hashtag + ", ");
        if (stringBuilder.length() != 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);

        hashtags.setText(stringBuilder);

        convertView.setOnClickListener(v -> {
            //Intent intent = new Intent(mContext, sfsdf.class);
            //mContext.startActivity(intent);
        });

        return convertView;
    }

    @Override
    public int getCount(){
        return videoList.size();
    }

    @Override
    public VideoInformation getItem(int position) {
        return videoList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
