package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    Random r;
    Animation scaleUP,scaleDown;
    TextView current_lv, display_num,nick,dis_color;
    EditText i_num;
    button_turn btn_try;
    Button btn_load,btn_save;
    pl.droidsonroids.gif.GifImageView pic_win;
    String random_num, msg_from_main [],color;
    int lv = 1,cooldown=2500,numberofdigit=1;
    public static final String  SHARED_PREFS="sharedPrefs";
    RadioGroup RG;
    RadioButton RB;
    DatabaseReference ref;
    String winner,loser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        scaleUP = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        ref= FirebaseDatabase.getInstance().getReference();
        current_lv = findViewById(R.id.user_lv);
        display_num = findViewById(R.id.display_number);
        i_num = findViewById(R.id.enter_num);
        pic_win=findViewById(R.id.gifwin);
        RG=findViewById(R.id.radio_group);
        btn_try = findViewById(R.id.btn_try);
        btn_load=findViewById(R.id.load_game);
        btn_save=findViewById(R.id.save_game);
        dis_color=findViewById(R.id.display_color);
        current_lv.setText("Level:" + lv);
        pic_win.setVisibility(View.GONE);
        Intent incomigIntent = getIntent();
        msg_from_main  = incomigIntent.getStringArrayExtra(MainActivity.Bundle_Msg);
        nick = findViewById(R.id.nickname);
        nick.setText("Nickname:" + msg_from_main[0]);
        btn_try.start_player(1);
        r = new Random();
        random_num=generate_num(lv);
        display_num.setText(random_num);
        btn_try.setVisibility(View.GONE);
        dis_color.setVisibility(View.GONE);
        RG.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
        btn_load.setVisibility(View.GONE);
        i_num.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {//the code will run inside  after the second in the variable time
            @Override
            public void run() {
                btn_try.setVisibility(View.VISIBLE);
                i_num.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_load.setVisibility(View.VISIBLE);
                display_num.setVisibility(View.GONE);
            }
            },cooldown);
        btn_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId=RG.getCheckedRadioButtonId();
                RB=findViewById(radioId);
                if((random_num.equals(i_num.getText().toString()))&&(lv<5||(lv>4&& RB.getText().toString().equals(color))))//correct answer
                {

                    radioId=RG.getCheckedRadioButtonId();
                    RB=findViewById(radioId);

                    if (msg_from_main.length==2)
                    {
                        btn_try.change_turn();
                        nick.setText("Nickname:" + msg_from_main[btn_try.Get_turn() - 1]);
                    }
                    btn_try.setVisibility(View.GONE);
                    i_num.setVisibility(View.GONE);
                    btn_save.setVisibility(View.GONE);
                    btn_load.setVisibility(View.GONE);
                    dis_color.setVisibility(View.GONE);
                    RG.setVisibility(View.GONE);
                    display_num.setVisibility(View.VISIBLE);
                    lv++;
                    current_lv.setText("level:" + lv);
                    if(lv<5)
                    {
                        numberofdigit++;
                        random_num=generate_num(numberofdigit);
                    }

                        if(lv>5&&lv%5==0)
                        {
                            numberofdigit++;
                            random_num = generate_num(numberofdigit);
                            setColor();
                        }
                    if((lv>4&&lv%5!=0)||lv==5)
                    {
                        random_num = generate_num(numberofdigit);
                        setColor();
                    }
                    if(cooldown>500)
                    {
                        cooldown = cooldown - 100;
                    }
                    if(lv<5)
                    {
                        display_num.setText(random_num);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(lv>4)//only for lv 5+
                            {
                                RG.setVisibility(View.VISIBLE);
                                dis_color.setVisibility(View.VISIBLE);
                            }
                            btn_try.setVisibility(View.VISIBLE);
                            btn_save.setVisibility(View.VISIBLE);
                            btn_load.setVisibility(View.VISIBLE);
                            i_num.setVisibility(View.VISIBLE);
                            display_num.setVisibility(View.GONE);
                        }
                    }, cooldown);

                }
                    else// eror number input
                {
                    display_num.setVisibility(View.VISIBLE);
                    if ((msg_from_main.length==2))// multi player game
                    {
                    loser=msg_from_main[btn_try.Get_turn()-1];
                        btn_try.change_turn();
                    winner=msg_from_main[btn_try.Get_turn()-1];
                        display_num.setText("Game over "+loser+", "+winner+ " you're the winner!!!");
                    }
                    else
                        {
                        display_num.setText("Game over, You've reached level:" + lv);
                        }
                    pic_win.setVisibility(View.VISIBLE);
                    btn_try.setVisibility(View.GONE);
                    btn_save.setVisibility(View.GONE);
                    btn_load.setVisibility(View.GONE);
                    dis_color.setVisibility(View.GONE);
                    RG.setVisibility(View.GONE);
                    display_num.setVisibility(View.VISIBLE);
                    i_num.setVisibility(View.GONE);
                    if(msg_from_main.length==2)
                    {
                        ref.push().setValue(msg_from_main[btn_try.Get_turn() - 1] + " got level: " + lv);
                    }
                    else
                    {
                        ref.push().setValue(msg_from_main[0] + " got level: " + lv);
                    }

                }
                i_num.setText("");
            }
        });
        btn_save.setOnTouchListener(new View.OnTouchListener() { //save button + animation
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               Save_game();
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

                Load_game();
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    btn_load.startAnimation(scaleUP);
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    btn_load.startAnimation(scaleDown);
                }
                return true;
            }});
    }

    private void setColor()
    {
        int num;
       SpannableString spantext= new SpannableString(random_num);
       ForegroundColorSpan fcsred=new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan fcsblue=new ForegroundColorSpan(Color.BLUE);
        ForegroundColorSpan fcsgreen=new ForegroundColorSpan(Color.GREEN);
        num=r.nextInt(3);
        switch (num)
        {
            case 0:
            {
                spantext.setSpan(fcsred, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                color="Red";
                break;
            }
            case 1:
            {
                spantext.setSpan(fcsblue, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                color="Blue";
                break;
            }
            case 2:
            {
                spantext.setSpan(fcsgreen, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                color="Green";
                break;
            }
        }
       display_num.setText(spantext);
    }
    private String generate_num(int lv)
    {
        String new_num="";
        for(int i=0;i<lv;i++)
        {
            int digit=r.nextInt(10);
            new_num=new_num+""+digit;
        }
        return new_num;
    }
    public void Save_game(){/// on click button save data
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("level",lv);
        editor.putString("color",color);
        editor.putInt("cooldown_timer",cooldown);
        editor.putInt("NumbersOfPlayers",msg_from_main.length);
        editor.putInt("numberofdigits",numberofdigit);
        editor.putString("current_num",random_num);
        editor.putInt("current_turn",(btn_try.Get_turn()-1));
        editor.putString("Nickname1",msg_from_main[0]);
        if(msg_from_main.length==2)
        {
            editor.putString("Nickname2",msg_from_main[1]);
        }
        editor.putInt("NumbersOfPlayers",msg_from_main.length);
        editor.apply();
        Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
    }
    public void Load_game()
    {
        i_num.setText("");
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        lv=sharedPreferences.getInt("level",1);
        cooldown=sharedPreferences.getInt("cooldown_timer",1);
        numberofdigit=sharedPreferences.getInt("numberofdigits",1);
        random_num=sharedPreferences.getString("current_num","");
        SpannableString spantext= new SpannableString(random_num);
        if(lv>4) {
            RG.setVisibility(View.VISIBLE);
            color = sharedPreferences.getString("color", "");

            if (color.equals("Red")) {
                ForegroundColorSpan fcscolor = new ForegroundColorSpan(Color.RED);
                spantext.setSpan(fcscolor, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (color.equals("Blue")) {
                ForegroundColorSpan fcscolor = new ForegroundColorSpan(Color.BLUE);
                spantext.setSpan(fcscolor, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }  if(color.equals("Green"))
            {
                ForegroundColorSpan fcscolor = new ForegroundColorSpan(Color.GREEN);
                spantext.setSpan(fcscolor, 0, random_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            display_num.setText(spantext);
        }
        if(sharedPreferences.getInt("current_turn",3)!=btn_try.Get_turn()-1)
        {
            btn_try.change_turn();
        }
        if(sharedPreferences.getInt("NumbersOfPlayers",1) == 2 )
        {
            msg_from_main=new String[2];
            msg_from_main[1]=sharedPreferences.getString("Nickname2","");
        }
        else
        {
            msg_from_main=new String[1];
        }
        msg_from_main[0]=sharedPreferences.getString("Nickname1","");
        current_lv.setText("level:" + lv);
        nick.setText("Nickname:" + msg_from_main[btn_try.Get_turn()-1]);
        if(lv<5)
        {
            display_num.setText(random_num);
        }
        else
        {
            display_num.setText(spantext);
        }
        new Handler().postDelayed(new Runnable() {//the code will run inside  after the second in the variable time
            @Override
            public void run() {
                btn_try.setVisibility(View.GONE);
                i_num.setVisibility(View.GONE);
                btn_load.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                display_num.setVisibility(View.VISIBLE);
                pic_win.setVisibility(View.GONE);
            }},250);
        new Handler().postDelayed(new Runnable() {//the code will run inside  after the second in the variable time
            @Override
            public void run() {
                btn_try.setVisibility(View.VISIBLE);
                btn_load.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                i_num.setVisibility(View.VISIBLE);
                display_num.setVisibility(View.GONE);
            }
        },cooldown);
    }
}