package com.example.mehedihassanpiash.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Thread updateSeekbar;

    SeekBar seekBar;
    Button buttonPlay;
    Button buttonNext;
    Button buttonFF;
    Button buttonFB;
    Button buttonRV;
    int position;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        buttonPlay = (Button)findViewById(R.id.btPlay);
        buttonNext = (Button)findViewById(R.id.btNext);
        buttonFF = (Button)findViewById(R.id.btFF);
        buttonFB = (Button)findViewById(R.id.btFB);
        buttonRV = (Button)findViewById(R.id.btRV);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        buttonPlay.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonFF.setOnClickListener(this);
        buttonFB.setOnClickListener(this);
        buttonRV.setOnClickListener(this);

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration =  mp.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration ){
                    try {
                        sleep(500);
                        currentPosition =mp.getCurrentPosition();
                        seekBar.setProgress( currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        Intent i = getIntent();
        Bundle b = i.getExtras();

         mySongs = (ArrayList)b.getParcelableArrayList("songlist");
         position = b.getInt("pos");

        if(mp != null ){
            mp.stop();
            mp.release();
        }

        uri = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        seekBar.setMax(mp.getDuration());

        updateSeekbar.start();

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        mp.seekTo(seekBar.getProgress());
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
        case R.id.btPlay:
            if( mp.isPlaying())
            {
                buttonPlay.setText("<");
                mp.pause();

            }
            else {
                buttonPlay.setText("||");
                mp.start();

            }
                    break;

            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;

            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;

            case R.id.btNext:
                mp.stop();
                mp.release();
                position = ( position + 1 ) % mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;

            case R.id.btRV:
                mp.stop();
                mp.release();
                position = ( position - 1 < 0 ) ? mySongs.size() - 1 : position - 1;
                uri = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),uri );
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
        }
    }
}
