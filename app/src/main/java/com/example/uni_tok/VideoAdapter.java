package com.example.uni_tok;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private ArrayList<VideoFile> videoList;
    private Context mContext;

    public VideoAdapter(Context context, ArrayList<VideoFile> videoList){
        this.videoList = videoList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//        VideoFile videoFile = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false);


        TextView videoName = (TextView) convertView.findViewById(R.id.videoTitle);
        TextView hashtags = (TextView) convertView.findViewById(R.id.hashtags);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.Thumbnail);

        Uri videoUri = Uri.parse(videoList.get(position).getFilepath());
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(mContext, videoUri);
        Bitmap thumbnail = mMMR.getFrameAtTime(20000000);

//        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoList.get(position).getFilepath(),
//                MediaStore.Video.Thumbnails.MICRO_KIND);

        videoName.setText(videoList.get(position).getVideoName());
        hashtags.setText(videoList.get(position).getAssociatedHashtags().toString());
        imageView.setImageBitmap(thumbnail);


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
