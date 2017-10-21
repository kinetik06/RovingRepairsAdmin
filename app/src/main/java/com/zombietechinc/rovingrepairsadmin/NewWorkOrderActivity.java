package com.zombietechinc.rovingrepairsadmin;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewWorkOrderActivity extends AppCompatActivity {
    LinearLayout mLinearLayout;
    EditText laborET;
    EditText partsET;
    Button laborBtn;
    Button partsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work_order);

        mLinearLayout = findViewById(R.id.workOrderLayout);
        laborET = findViewById(R.id.laborET);
        partsET = findViewById(R.id.partsET);
        laborBtn = findViewById(R.id.addLaborbtn);
        partsBtn = findViewById(R.id.addPartsbtn);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(params);

        laborBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = new TextView(NewWorkOrderActivity.this);
                mLinearLayout.addView(textView);
                textView.setText(laborET.getText());
                laborET.setText("");
            }
        });
    }
}
