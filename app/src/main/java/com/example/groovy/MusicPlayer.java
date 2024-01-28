package com.example.groovy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    TextView title,current,total;
    SeekBar seekBar;
    int x=0;
    ImageView previous,pause,next,musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer=MyMediaPlayer.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);
        title=findViewById(R.id.song_title);
        current=findViewById(R.id.current_time);
        total=findViewById(R.id.total_time);
        seekBar=findViewById(R.id.seekbar);
        previous=findViewById(R.id.previous);
        pause=findViewById(R.id.pause);
        next=findViewById(R.id.next);
        musicIcon=findViewById(R.id.music_icon_big);
        title.setSelected(true);
        songsList= (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();

        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    current.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        pause.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);

                    }else
                    {
                        pause.setImageResource(R.drawable.baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    void setResourcesWithMusic(){
        currentSong=songsList.get(MyMediaPlayer.currentIndex);
        title.setText(currentSong.getTitle());
        total.setText(convertToMMSS(currentSong.getDuration()));
        pause.setOnClickListener(view -> pausePlay());
        next.setOnClickListener(view -> playNextSong());
        previous.setOnClickListener(view -> playPreviousSong());
        playMusic();
    }
    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

        } catch (IOException e) {
                  e.printStackTrace();
        }

    }
    private void playNextSong(){
         if(MyMediaPlayer.currentIndex==songsList.size()-1)
             return;
         MyMediaPlayer.currentIndex +=1;
         mediaPlayer.reset();
         setResourcesWithMusic();
    }
    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex==0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();

    }
    public  static String convertToMMSS(String duration){
        Long millis=Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) %TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) %TimeUnit.MINUTES.toSeconds(1));
    }
}
