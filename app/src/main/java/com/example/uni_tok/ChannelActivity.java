package com.example.uni_tok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ChannelActivity extends AppCompatActivity {

    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel);

        search_bar = (EditText)findViewById(R.id.search_bar);

    }

    public void channelActivity(View v) {}

    public void searchActivity(View v) {
        String topic = search_bar.getText().toString();
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void uploadActivity(View v) {}

    public void homeActivity(View v) {

        Intent intent = new Intent(this, runUser.class);
        startActivity(intent);

    }

    public void exit(View v) {}

}
