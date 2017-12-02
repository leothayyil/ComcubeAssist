package com.example.user.comcubeassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedback=(EditText)findViewById(R.id.EdtFeedback);
        feedback.setMaxLines(5);
        feedback.setSelected(true);
    }
}
