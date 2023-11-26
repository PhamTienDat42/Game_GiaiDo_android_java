package com.example.game_giaido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail, profileUsername, profilePassword, profileRank, profileScore, profileTime;
    TextView titleName, titleUsername;
    Button editProfile, showMap, showMapFragment;

    //FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        profileRank = findViewById(R.id.rank);
        profileScore = findViewById(R.id.score);
        profileTime = findViewById(R.id.time);

        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        showMap = findViewById(R.id.showMap);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonAddQuestion = findViewById(R.id.buttonAddQuestion);
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang AddQuestionActivity
                Intent intent = new Intent(ProfileActivity.this, AddQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showAllUserData() {
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);

        if (preferences.contains("username")) {
            String nameUser = preferences.getString("name", "");
            String emailUser = preferences.getString("email", "");
            String usernameUser = preferences.getString("username", "");
            String passwordUser = preferences.getString("password", "");
            int rankUser = preferences.getInt("rank", 0);
            int scoreUser = preferences.getInt("score", 0);
            float timeUser = preferences.getFloat("time", 0.0f);

            titleName.setText(nameUser);
            titleUsername.setText(usernameUser);
            profileName.setText(nameUser);
            profileEmail.setText(emailUser);
            profileUsername.setText(usernameUser);
            profilePassword.setText(passwordUser);
            profileRank.setText(String.valueOf(rankUser));
            profileScore.setText(String.valueOf(scoreUser));
            profileTime.setText(String.valueOf(timeUser));
        }
    }

    public void passUserData(){
        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);

                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}