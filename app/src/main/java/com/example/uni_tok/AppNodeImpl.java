package com.example.uni_tok;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class AppNodeImpl {

    private static Socket requestSocket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    private static Channel channel;

    private static TreeMap<Integer, SocketAddress> brokerHashes = new TreeMap<>();
    private static SocketAddress channelBroker;

    private static ServerSocket serverSocket;

    private static SocketAddress hear_address;

    private static ArrayList<String> subscribedToChannels = new ArrayList<>();
    private static ArrayList<String> subscribedToHashtags = new ArrayList<>();

    private static ArrayList<VideoInformation> searchVideoList = new ArrayList<>();
    private static ArrayList<VideoInformation> homePageVideoList  = new ArrayList<>();

    public static void init(int port) {
        try {
            serverSocket = new ServerSocket(port, 60, InetAddress.getByName("10.0.2.15"));

            File uploadedDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Uploaded Videos/");
            uploadedDir.mkdirs();
            File fetchedDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Fetched Videos/");
            fetchedDir.mkdirs();

        } catch (IOException io) {
            Log.d("Initialization Error", "Couldn't initialise App Node!");
        }
    }

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        AppNodeImpl.channel = channel;
    }

    public static void getBrokers() throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject(2);
        objectOutputStream.flush();

        brokerHashes = (TreeMap<Integer, SocketAddress>) objectInputStream.readObject();
    }

    public static boolean setChannelBroker(String name) throws IOException, ClassNotFoundException {
        boolean unique;

        //CHANNEL NAME
        channel = new Channel(name);

        //CONNECT TO APPROPRIATE BROKER
        channelBroker = hashTopic(channel.getChannelName());
        connect(channelBroker);

        //SEND OPTION 4 FOR INITIALIZATION
        objectOutputStream.writeObject(4);
        objectOutputStream.flush();

        //SEND CHANNEL NAME
        objectOutputStream.writeObject(channel.getChannelName());
        objectOutputStream.flush();

        //SEND SOCKET ADDRESS FOR CONNECTIONS
        init(4960);//4960
        String string_socket = serverSocket.getLocalSocketAddress().toString().split("/")[1];
        String[] array = string_socket.split(":");
        InetAddress hear_ip = InetAddress.getByName("127.0.0.1");
        int hear_port = 5529;//5529
        Log.d("HEAR IP", hear_ip.toString());
        Log.d("HEAR PORT", Integer.toString(hear_port));
        hear_address = new InetSocketAddress(hear_ip, hear_port);
        objectOutputStream.writeObject(hear_address);
        objectOutputStream.flush();

        //GET RESPONSE IF CHANNEL NAME IS UNIQUE
        unique = (boolean) objectInputStream.readObject();

        return unique;

    }

    public static boolean setSearchTopicVideoList(String topic) {

        searchVideoList.clear();

        boolean fetched_successfully = false;
        //Get right broker
        SocketAddress socketAddress = hashTopic(topic);

        //Connect to that broker
        connect(socketAddress);

        try {
            //Write option
            objectOutputStream.writeObject(2);
            objectOutputStream.flush();

            //Write channel name or hashtag
            objectOutputStream.writeObject(topic);
            objectOutputStream.flush();

            //Write this user's channel name
            objectOutputStream.writeObject(channel.getChannelName());
            objectOutputStream.flush();

            //Read videoList
            searchVideoList = (ArrayList<VideoInformation>) objectInputStream.readObject();

            fetched_successfully = true;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        return fetched_successfully;
    }

    /**
     * This page uploads to Home Page a video every time a
     * channel or hashtag we are subscribed to uploads a video
     */
    public synchronized static void refreshHomePage(VideoInformation vi) {

        ArrayList<ChannelKey> keys = new ArrayList<>();
        for (VideoInformation info : homePageVideoList) {
            keys.add(info.getChannelKey());
        }
        if (!keys.contains(vi.getChannelKey())) {
            homePageVideoList.add(0, vi);
            if (homePageVideoList.size() > 10) {
                homePageVideoList.remove(homePageVideoList.size() - 1);
            }
        }
    }

    public synchronized static void refreshHomePage(ArrayList<VideoInformation> vi_list) {

        ArrayList<ChannelKey> keys = new ArrayList<>();
        for (VideoInformation info : homePageVideoList) {
            keys.add(info.getChannelKey());
        }

        for (VideoInformation vi : vi_list) {
            if (!keys.contains(vi.getChannelKey())) {
                homePageVideoList.add(vi);
            }
        }

        Collections.sort(homePageVideoList);
        while (homePageVideoList.size() > 10) {
            homePageVideoList.remove(homePageVideoList.size() - 1);
        }
    }

    public synchronized static void refreshHomePage(String topic) {
        if (topic.charAt(0) == '#') {
            for (VideoInformation item : homePageVideoList) {
                if (item.getHashtags().contains(topic)) {
                    homePageVideoList.remove(item);
                }
            }
        }
        else {
            for (VideoInformation item : homePageVideoList) {
                if (item.getChannelName().equals(topic)) {
                    homePageVideoList.remove(item);
                }
            }
        }
    }

    public static ArrayList<VideoInformation> getHomePageVideoList() {
        return homePageVideoList;
    }

    public static ArrayList<VideoInformation> getSearchTopicVideoList() {
        return searchVideoList;
    }

    public static ArrayList<String> getSubscribedToChannels() {
        return subscribedToChannels;
    }

    public static ArrayList<String> getSubscribedToHashtags() {
        return subscribedToHashtags;
    }

    public static boolean Upload(String path, ArrayList<String> associatedHashtags, String videoName,
                                 byte[] thumbnail){
        VideoFile videoFile = new VideoFile(path, associatedHashtags, videoName, thumbnail);

        ChannelKey channelKey = new ChannelKey((AppNodeImpl.getChannel()).getChannelName(),
                AppNodeImpl.getChannel().getCounterVideoID()).setDate(videoFile.getDate());
        HashMap<String, String> notificationHashtags = (AppNodeImpl.getChannel()).addVideoFile(videoFile,
                channelKey);

        if (!notificationHashtags.isEmpty()) {
            for (Map.Entry<String, String> item : notificationHashtags.entrySet())
                AppNodeImpl.notifyBrokersForHashTags(item.getKey(), item.getValue());
        }

        AppNodeImpl.notifyBrokersForChanges(channelKey, associatedHashtags, videoName, associatedHashtags,
                true, thumbnail);

        return true;
    }

    public static void handleRequest() {
        try {
            while(true) {
                Log.d("WAITING FOR CONNECTIONS", "START");
                Socket connectionSocket = serverSocket.accept();
                new ServeRequest(connectionSocket).start();
            }
        } catch(IOException e) {
            /* Crash the server if IO fails. Something bad has happened. */
            Log.d("SERVER SOCKET", "CONNECTION ACCEPT FAIL");
            throw new RuntimeException("Could not create ServerSocket ", e);
        } finally {
            try {
                Log.d("Socket State", "Closed");
                serverSocket.close();
            } catch (IOException | NullPointerException ioException) {
                Log.d("Exception : ", ioException.getMessage());
            }
        }
    }

    public static boolean addHashTag(VideoFile video, ArrayList<String> hashtagsAdded) {

        ArrayList<String> hashtags = new ArrayList<>();
        for (String hashtag : hashtagsAdded) {
            if (!video.getAssociatedHashtags().contains(hashtag))
                hashtags.add(hashtag);
        }

        if (hashtags.isEmpty()) {
            Log.d("ADD HASHTAGS", "No hashtags found to add.");
        } else {

            HashMap<String, String> notificationHashtags = channel.updateVideoFile(video, hashtags, "ADD");
            if (!notificationHashtags.isEmpty()) {
                for (Map.Entry<String, String> item : notificationHashtags.entrySet())
                    notifyBrokersForHashTags(item.getKey(), item.getValue());
            }

            ChannelKey channelKey = new ChannelKey(channel.getChannelName(), video.getVideoID());
            channelKey.setDate(video.getDate());

            notifyBrokersForChanges(channelKey, hashtags, video.getVideoName(), video.getAssociatedHashtags(),
                    false, video.getThumbnail());
        }
        return true;
    }

    public static boolean removeHashTag(VideoFile video, ArrayList<String> hashtagsRemoved) {

        ArrayList<String> hashtags = new ArrayList<>();
        for (String hashtag : hashtagsRemoved) {
            if (video.getAssociatedHashtags().contains(hashtag)) {
                hashtags.add(hashtag);
            }
        }

        if (hashtags.isEmpty()) {
            Log.d("REMOVE HASHTAGS", "No hashtags found to remove.");
        } else {

            HashMap<String, String> notificationHashtags = channel.updateVideoFile(video, hashtags, "REMOVE");
            if (!notificationHashtags.isEmpty()) {
                for (Map.Entry<String, String> item : notificationHashtags.entrySet())
                    notifyBrokersForHashTags(item.getKey(), item.getValue());
            }
        }
        return true;
    }

    public static SocketAddress hashTopic(String hashtopic) {

        int digest;
        SocketAddress brokerAddress = brokerHashes.get(brokerHashes.firstKey());
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] bb = sha256.digest(hashtopic.getBytes(Charset.forName("UTF-8")));
            BigInteger bigInteger = new BigInteger(1, bb);
            digest = bigInteger.intValue();

            //Fit to the right broker
            for (int hash : brokerHashes.keySet()) {
                if (digest <= hash) {
                    brokerAddress = brokerHashes.get(hash);
                    break;
                }
            }
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } finally {
            return brokerAddress;
        }
    }

    public static void push(int id, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream)
            throws NoSuchElementException {

        ArrayList<byte[]> chunks = generateChunks(channel.getVideoFile_byID(id));

        try {
            objectOutputStream.writeObject(true);
            objectOutputStream.flush();

            objectOutputStream.writeObject(chunks.size());
            objectOutputStream.flush();

            while (!chunks.isEmpty()) {
                byte[] clientToServer = chunks.remove(0);
                objectOutputStream.write(clientToServer);
                objectOutputStream.flush();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void notifyBrokersForHashTags(String hashtag, String action) {
        SocketAddress socketAddress = hashTopic(hashtag);
        connect(socketAddress);
        try {
            objectOutputStream.writeObject(7);
            objectOutputStream.flush();

            objectOutputStream.writeObject(hashtag);
            objectOutputStream.flush();

            objectOutputStream.writeObject(action);
            objectOutputStream.flush();

            objectOutputStream.writeObject(hear_address);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public static void notifyBrokersForChanges(ChannelKey channelKey, ArrayList<String> hashtags, String title,
                                               ArrayList<String> associatedHashtags, boolean action,
                                               byte[] thumbnail) {

        if (!hashtags.isEmpty()) {
            for (String hashtag : hashtags) {
                SocketAddress socketAddress = hashTopic(hashtag);
                connect(socketAddress);
                try {
                    objectOutputStream.writeObject(8);
                    objectOutputStream.flush();

                    objectOutputStream.writeObject("hashtag");
                    objectOutputStream.flush();

                    objectOutputStream.writeObject(hashtag);
                    objectOutputStream.flush();

                    objectOutputStream.writeObject(channelKey);
                    objectOutputStream.flush();

                    objectOutputStream.writeObject(title);
                    objectOutputStream.flush();

                    objectOutputStream.writeObject(associatedHashtags);
                    objectOutputStream.flush();

                    objectOutputStream.write(thumbnail);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            }
        }

        if (action) {
            SocketAddress socketAddress = hashTopic(channelKey.getChannelName());
            connect(socketAddress);
            try {
                objectOutputStream.writeObject(8);
                objectOutputStream.flush();

                objectOutputStream.writeObject("channel");
                objectOutputStream.flush();

                objectOutputStream.writeObject(channelKey);
                objectOutputStream.flush();

                objectOutputStream.writeObject(title);
                objectOutputStream.flush();

                objectOutputStream.writeObject(associatedHashtags);
                objectOutputStream.flush();

                objectOutputStream.write(thumbnail);
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }
    }

    public static ArrayList<byte[]> generateChunks(VideoFile video) {

        ArrayList<byte[]> my_arraylist = new ArrayList<>();

        int i = 0;
        byte[] inputBuffer = video.getVideoFileChunk();

        while (i < inputBuffer.length) {
            byte[] buffer = new byte[4096];
            for (int j = 0;j < buffer.length;j++) {
                if (i < inputBuffer.length)
                    buffer[j] = inputBuffer[i++];
            }
            my_arraylist.add(buffer);
        }
        return my_arraylist;
    }

    public static void connect(SocketAddress socketAddress) {

        try {
            Log.d("Enter connection", "Enter connection");
            requestSocket = new Socket();
            requestSocket.connect(socketAddress);
            requestSocket.setSoTimeout(0);
            Log.d("Connected!", "Connected!");
            objectOutputStream = new ObjectOutputStream(requestSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(requestSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            objectInputStream.close();
            objectOutputStream.close();
            requestSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean register(SocketAddress socketAddress, String topic) {

        connect(socketAddress);
        boolean successful_subscription = false;

        try {
            objectOutputStream.writeObject(1);
            objectOutputStream.flush();

            objectOutputStream.writeObject(channel.getChannelName());
            objectOutputStream.flush();

            objectOutputStream.writeObject(topic);
            objectOutputStream.flush();

            objectOutputStream.writeObject(hear_address);
            objectOutputStream.flush();

            String response = (String) objectInputStream.readObject();
            Log.d("RESPONSE", response);

            if (response.contains("successfully")) {
                if (topic.charAt(0) == '#') {
                    subscribedToHashtags.add(topic);
                } else {
                    subscribedToChannels.add(topic);
                }
                successful_subscription = true;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        return successful_subscription;
    }

    public static boolean unregister(SocketAddress socketAddress, String topic) {

        boolean successful_unsubscription = false;

        try {
            connect(socketAddress);

            objectOutputStream.writeObject(9);
            objectOutputStream.flush();

            objectOutputStream.writeObject(topic);
            objectOutputStream.flush();

            objectOutputStream.writeObject(hear_address);
            objectOutputStream.flush();

            if (topic.charAt(0) == '#'){
                subscribedToHashtags.remove(topic);
            } else {
                subscribedToChannels.remove(topic);
            }
            successful_unsubscription = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        return successful_unsubscription;
    }

    public static boolean playData(ChannelKey ck) {

        File nf = null;
        FileOutputStream fw = null;
        String channelName = ck.getChannelName();
        int videoID = ck.getVideoID();
        boolean successfullPull = true;

        try {
            //CONNECTING TO BROKER RESPONSIBLE FOR CHANNEL, THAT HAS THE VIDEO WE ASKED FOR
            SocketAddress brokerAddress = hashTopic(channelName);
            connect(brokerAddress);

            objectOutputStream.writeObject(3);
            objectOutputStream.flush();

            objectOutputStream.writeObject(ck);
            objectOutputStream.flush();

            //RECEIVE VIDEO FILE CHUNKS
            byte[] chunk;
            ArrayList<byte[]> chunks = new ArrayList<>();
            int size = (int) objectInputStream.readObject();

            if (size == 0) {
                System.out.println("CHANNEL HAS NO VIDEO WITH THIS ID...");
            }
            //REBUILD CHUNKS FOR TESTING
            else {
                for (int i = 0; i < size; i++) {
                    chunk = new byte[4096];
                    objectInputStream.readFully(chunk);
                    //chunk = objectInputStream.readAllBytes();
                    chunks.add(chunk);
                }
                try {
                    nf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .toString() + "/Fetched Videos/" + channelName + "_" + videoID + ".mp4");
                    fw = new FileOutputStream(nf, true);
                    for (byte[] ar : chunks) {
                        fw.write(ar);
                    }

                } catch (IOException e) {
                    successfullPull = false;
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        fw.close();
                    }
                    disconnect();
                }
            }
        } catch(IOException | ClassNotFoundException e){
            successfullPull = false;
            e.printStackTrace();
        }
        return successfullPull;
    }

    public static HashMap<ChannelKey, String> getChannelVideoMap() {
        return channel.getChannelVideoNames();
    }

    public static HashMap<ChannelKey, ArrayList<String>> getChannelHashtagsMap() {
        return channel.getChannelAssociatedHashtags();
    }

    public static HashMap<ChannelKey, String> getHashtagVideoMap(String hashtag) {
        return channel.getChannelVideoNamesByHashtag(hashtag);
    }
}