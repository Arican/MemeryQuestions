package com.example.textexp;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;


import static android.content.ContentValues.TAG;

public class LeveNumLimit extends Activity {


    private Button button_plus;
    private Button button_minus;
    private TextView limit_number;
    Myapplication myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.leve_num_limit);
        limit_number = this.findViewById(R.id.limit_number);
        myapplication = (Myapplication) getApplication();
        if (myapplication.leveNumLimit == 0) {
            Log.i(TAG, "onCreate: myapplication.leveNumLimit = 为零");
            limit_number.setText("难度为零");
        } else
            limit_number.setText("难度: " + myapplication.leveNumLimit);  //在界面中显示难度限制
        //        Intent intent = getIntent();

        /*
        这个界面和主界面没有数据传输，点击加减按钮，更改 myapplication. 的数值，并在本界面中显示
         */
        button_plus = (Button) findViewById(R.id.limit_plus);
        button_plus.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               myapplication.leveNumLimit++;
                                               limit_number = findViewById(R.id.limit_number);
                                               if (myapplication.leveNumLimit == 0) {
                                                   Log.i(TAG, "onCreate: myapplication.leveNumLimit =  button_plus 为零");
                                                   limit_number.setText("难度为零");
                                               } else
                                                   limit_number.setText("难度: " + myapplication.leveNumLimit);
                                           }
                                       }
        );
        button_minus = (Button) findViewById(R.id.limit_minus);
        button_minus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (myapplication.leveNumLimit > 0)
                                                    myapplication.leveNumLimit--;
                                                if (myapplication.leveNumLimit == 0) {
                                                    Log.i(TAG, "onCreate: myapplication.leveNumLimit =  button_minus 为零");
                                                    limit_number.setText("难度为零");
                                                } else
                                                    limit_number.setText("难度: " + myapplication.leveNumLimit);
                                            }
                                        }
        );


    }
}
