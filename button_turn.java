package com.example.project;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class button_turn extends androidx.appcompat.widget.AppCompatButton {
    private  int player;

    public button_turn(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.player=player;
    }

    public void start_player(int num)
    {
        this.player=num;
    }
    public int Get_turn()
    {
        return this.player;
    }
    public void change_turn()
    {
        switch(this.player)
        {
            case 1:
                this.player=2;
                break;
            case 2:
                this.player=1;
                break;

        }

    }
}