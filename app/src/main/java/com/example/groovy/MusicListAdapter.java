package com.example.groovy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends  RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    ArrayList<AudioModel> songsList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder,  int position) {
      AudioModel songsData=songsList.get(position);
      holder.textView.setText(songsData.getTitle());

      if(MyMediaPlayer.currentIndex==position){
          holder.textView.setTextColor(Color.parseColor("#FF0C91A2"));
      }else{
          holder.textView.setTextColor(Color.parseColor("#FFFFFFFF"));
      }
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              MyMediaPlayer.getInstance().reset();
              MyMediaPlayer.currentIndex =position;
              Intent intent=new Intent(context,MusicPlayer.class);
              intent.putExtra("LIST",songsList);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.music_title);
            imageView=itemView.findViewById(R.id.icon);
        }
    }

}
