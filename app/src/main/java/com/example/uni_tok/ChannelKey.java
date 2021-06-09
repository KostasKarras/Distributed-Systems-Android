package com.example.uni_tok;
import java.io.Serializable;
import java.util.Date;

/** Channel Key
 * This class is used to represent keys for HashMaps
 * with two values : channel Name and id.
 * Broker needs to know which channel name sent which videos
 * and that would be impossible using only video id as a key
 * to our Hashmap
 * */
public class ChannelKey implements Serializable {

    private final String channelName;
    private final int videoID;
    private Date date;

    ChannelKey(String channelName, int videoID) {
        this.channelName = channelName;
        this.videoID = videoID;
        date = new Date();
    }

    @Override
    public boolean equals(Object newKey) {

        if (this == newKey) {return true;}
        if (!(newKey instanceof ChannelKey)) {return false;}
        ChannelKey key = (ChannelKey) newKey;
        return channelName.equals(key.getChannelName()) && videoID == key.getVideoID();
    }

    @Override
    public int hashCode() {
        return channelName.hashCode() + videoID;
    }

    @Override
    public String toString() {
        return String.format("Channel Name : %s, Video Id : %d", channelName, videoID);
    }

    public String getChannelName() {
        return channelName;
    }

    public int getVideoID() {
        return videoID;
    }

    public Date getDate() {
        return date;
    }
}