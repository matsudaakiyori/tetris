package com.example.akiyori.down_grade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by akiyori on 2015/08/31.
 */
//ゲームオーバー時に表示
public class GameOver extends Activity {
    private TextView textView;

    private Intent intent;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        textView = (TextView) findViewById(R.id.game_over);
         intent = getIntent();
        setVisible(false);
        if (intent != null) {
            textView.setText(intent.getStringExtra("スコア"));
            setVisible(true);
        }
    }

}
