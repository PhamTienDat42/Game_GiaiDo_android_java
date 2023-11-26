package com.example.game_giaido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HelperClass> playerList = new ArrayList<>();

                int rank = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HelperClass player = snapshot.getValue(HelperClass.class);
                    playerList.add(player);
                }

                // Sắp xếp danh sách người chơi theo điểm giảm dần
                Collections.sort(playerList, new Comparator<HelperClass>() {
                    @Override
                    public int compare(HelperClass player1, HelperClass player2) {
                        return Integer.compare(player2.getScore(), player1.getScore());
                    }
                });

                // Thêm số thứ tự vào danh sách
                for (int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).setRank(i + 1);
                }

                RankAdapter rankAdapter = new RankAdapter(playerList);
                recyclerView.setAdapter(rankAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}
