package com.example.game_giaido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// RankAdapter.java
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<HelperClass> playerList;

    public RankAdapter() {
        this.playerList = new ArrayList<>();
    }

    public RankAdapter(List<HelperClass> playerList) {
        this.playerList = playerList;
    }

    public void clearData() {
        playerList.clear();
    }

    public void addPlayer(HelperClass player) {
        playerList.add(player);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rank_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelperClass player = playerList.get(position);

        // Thiết lập giá trị cho textRank
        holder.textRank.setText((position + 1) + "."); // Số thứ tự bắt đầu từ 1

        // Thiết lập giá trị cho textUsername và textScore
        holder.textUsername.setText(player.getUsername() + " ");
        holder.textScore.setText("Score: " + player.getScore() + " ");
        holder.textTime.setText("Time: " + player.getTime());
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRank, textUsername, textScore, textTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Khởi tạo các View, bao gồm textRank
            textRank = itemView.findViewById(R.id.textRank);
            textUsername = itemView.findViewById(R.id.textUsername);
            textScore = itemView.findViewById(R.id.textScore);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}
