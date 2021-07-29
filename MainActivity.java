package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class    MainActivity extends AppCompatActivity {
    public static final String Bundle_Msg = "com.example.myapplication.extra.Bundle_Msg";
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText i_user;
    private Button btn_multi,btn_single,btn_save,btn_load,btn_history,btn_instru;
    static int size;
    private String text;
    Animation scaleUP,scaleDown;
    private boolean ModeOffSw;
    public static final String  SHARED_PREFS="sharedPrefs";
    public static final String  TEXT="text";
    String[] names = new String[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        size = 0;
        i_user = findViewById(R.id.i_name);
        btn_single = findViewById(R.id.S_btn);
        btn_instru=findViewById(R.id.btn_instructions);
        btn_save = findViewById(R.id.save_btn);
        btn_history = findViewById(R.id.history);
        btn_load = findViewById(R.id.load_btn);
        scaleUP = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        btn_save.setOnTouchListener(new View.OnTouchListener() {//save button + animation
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                saveData();
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    btn_save.startAnimation(scaleUP);

                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    btn_save.startAnimation(scaleDown);
                }
                return true;
            }});
        btn_load.setOnTouchListener(new View.OnTouchListener() { //load button + animation
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                load_Data();
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    btn_load.startAnimation(scaleUP);

                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    btn_load.startAnimation(scaleDown);
                }
                return true;
            }});
        btn_multi = findViewById(R.id.m_players);
        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_user_details(names);
            }

        });

    }

    public void saveData(){// save data with shared preferences
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(TEXT,i_user.getText().toString());//save the edit text name
        editor.apply();
        Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
    }
    public void load_Data()// load the data from the edit text when click on button load
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text=sharedPreferences.getString(TEXT,"");
        i_user.setText(text);
    }

    public void put_user_details(String [] names )// put the names info to array
    {
            btn_single.setVisibility(View.GONE);
            btn_instru.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            btn_load.setVisibility(View.GONE);
            btn_history.setVisibility(View.GONE);
            if(size!=2) {// for the prev button when we dont get on create so we need to initialize the array
                names[size] = i_user.getText().toString();
            }
            else{
                Toast.makeText(MainActivity.this, "please insert the first name", Toast.LENGTH_SHORT).show();
                size = 0;
                names[0] = "";
                names[1] = "";
                names[size] = i_user.getText().toString();

            }
            if (names[0].equals("")) {
                Toast.makeText(MainActivity.this, "please insert the first name", Toast.LENGTH_SHORT).show();
            }
            if (size == 1 && names[size].equals("")) {
                Toast.makeText(MainActivity.this, "please insert the second name", Toast.LENGTH_SHORT).show();
            }
            if (!(names[size].equals("")))// if the user enter name to edit text so we put in on array
            {
                i_user.setText("");
                size++;
            }
            if (size == 2) {
                btn_instru.setVisibility(View.VISIBLE);
                btn_single.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_history.setVisibility(View.VISIBLE);
               openActivity2(names);
            }
        }

    public void openActivity2(String[] Message) {// open activity to the screen on game multi player
        Log.d(TAG, "Clicked btn multiplayers");
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(Bundle_Msg, Message);
        startActivity(intent);
    }

    public void SendToSecond(View view) {// on click button start 1 player game check if insert the name of him

        Log.d(TAG, "Clicked button start");
        Intent intent = new Intent(this, SecondActivity.class);
        String[] Message =new String[1];
         Message[0] = i_user.getText().toString();
        if (Message[0].equals("")) {
            Toast.makeText(MainActivity.this, "please insert name", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(Bundle_Msg, Message);
            startActivity(intent);
        }
    }
    public void SendToThrid(View view) {// list view with firebase of history result game
        Log.d(TAG, "Clicked button history");
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
    public void SendToInstru(View view)
    {
        Log.d(TAG, "Clicked button instruction");
        Intent intent = new Intent(this, Instruction.class);
        startActivity(intent);
    }
}
