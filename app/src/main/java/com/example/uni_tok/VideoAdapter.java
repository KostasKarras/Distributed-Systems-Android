package com.example.uni_tok;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private ArrayList<VideoFile> videoList;
    private Context mContext;
    private int index;

    public VideoAdapter(Context context, ArrayList<VideoFile> videoList){
        this.videoList = videoList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        this.index = position;

        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_item_channel, parent, false);


        TextView videoName = (TextView) convertView.findViewById(R.id.videoTitle);
        TextView hashtags = (TextView) convertView.findViewById(R.id.hashtags);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.Thumbnail);

        Uri videoUri = Uri.parse(videoList.get(position).getFilepath());
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(mContext, videoUri);
        Bitmap thumbnail = mMMR.getFrameAtTime(20000000);

        videoName.setText(videoList.get(position).getVideoName());

        StringBuilder stringBuilder = new StringBuilder();
        for (String hashtag : videoList.get(position).getAssociatedHashtags())
            stringBuilder.append(hashtag + ", ");
        if (stringBuilder.length() != 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);

        hashtags.setText(stringBuilder);

        imageView.setImageBitmap(thumbnail);

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoList.get(position).getFilepath()), "video/mp4");
            mContext.startActivity(intent);
        });

        return convertView;
    }

    @Override
    public int getCount(){
        return videoList.size();
    }

    @Override
    public VideoFile getItem(int position) {
        return videoList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
