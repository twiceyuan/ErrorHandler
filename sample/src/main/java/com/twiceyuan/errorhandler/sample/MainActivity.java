package com.twiceyuan.errorhandler.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.twiceyuan.errorhandler.sample.exceptions.FirstException;
import com.twiceyuan.errorhandler.sample.exceptions.SecondException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test1(View view) {
        new Thread(new Runnable() {
            @Override public void run() {
                throw new FirstException();
            }
        }).start();
    }


    public void test2(View view) {
        new Thread(new Runnable() {
            @Override public void run() {
                throw new SecondException();
            }
        }).start();
    }

    public void test3(View view) {
        //noinspection MismatchedReadAndWriteOfArray
        Button[] buttons = new Button[1];
        // make a npe
        buttons[0].setText("");
    }
}
