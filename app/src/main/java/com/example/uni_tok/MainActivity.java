package com.example.uni_tok;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText IP;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IP = (EditText) findViewById(R.id.AddressKeeperIP);

        submitButton = (Button) findViewById(R.id.AddressKeeperSubmit);
        submitButton.setOnClickListener(v -> newChannel());
    }

    public void newChannel(){
        Intent intent = new Intent(this, newChannel.class);
        startActivity(intent);
    }

}