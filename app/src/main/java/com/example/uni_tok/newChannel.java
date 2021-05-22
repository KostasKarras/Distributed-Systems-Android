package com.example.uni_tok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class newChannel extends AppCompatActivity {

    EditText channelName;
    Button submitButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);

        channelName = (EditText) findViewById(R.id.ChannelName);

        submitButton = (Button) findViewById(R.id.channelButton);
        submitButton.setOnClickListener(v -> runUser());
    }

    public void runUser(){
        Intent intent = new Intent(this, runUser.class);
        startActivity(intent);
    }
}