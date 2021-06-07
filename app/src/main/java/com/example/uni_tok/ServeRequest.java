package com.example.uni_tok;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ServeRequest extends Thread {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    ServeRequest(Socket s) {
        socket = s;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try{

            int option = (int) objectInputStream.readObject();

            if (option == 1) { //Pull List

                //Choice between sending whole channel or files based on hashtag
                String choice = (String) objectInputStream.readObject();
                System.out.println(choice);
                if (choice.equals("CHANNEL")) {
                    //HashMap<ChannelKey, String> videoList = AppNodeImpl.getChannelVideoMap();
                    // DIMITRIS
                    ArrayList<VideoInformation> videoList = new ArrayList<>();
                    for (ChannelKey ck : AppNodeImpl.getChannelVideoMap().keySet()) {
                        VideoInformation vi = new VideoInformation(ck, AppNodeImpl.getChannelVideoMap().get(ck), AppNodeImpl.getChannelHashtagsMap().get(ck));
                        videoList.add(vi);
                    }
                    objectOutputStream.writeObject(videoList);
                }
                else {
                    //HashMap<ChannelKey, String> videoList = AppNodeImpl.getHashtagVideoMap(choice);
                    ArrayList<VideoInformation> videoList = new ArrayList<>();
                    for (ChannelKey ck : AppNodeImpl.getHashtagVideoMap(choice).keySet()) {
                        VideoInformation vi = new VideoInformation(ck, AppNodeImpl.getChannelVideoMap().get(ck), AppNodeImpl.getChannelHashtagsMap().get(ck));
                        videoList.add(vi);
                    }
                    objectOutputStream.writeObject(videoList);
                }

            } else if (option == 2) { //Pull Video

                ChannelKey channelKey = (ChannelKey) objectInputStream.readObject();
                try {
                    AppNodeImpl.push(channelKey.getVideoID(), objectInputStream, objectOutputStream);
                } catch (NoSuchElementException nsee) {
                    objectOutputStream.writeObject(false);
                    objectOutputStream.flush();
                }

            } else if (option == 3) { //Notification Message

                String notificationMessage = (String) objectInputStream.readObject();
                //Michalis
                ChannelKey key = (ChannelKey) objectInputStream.readObject();
                String title = (String)objectInputStream.readObject();
                AppNodeImpl.refreshHomePage(key, title);
                //
                System.out.println(notificationMessage);
                //Toast.makeText(getApplicationContext(), notificationMessage,
                //        Toast.LENGTH_SHORT).show();

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}