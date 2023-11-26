package com.example.game_giaido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail, profileUsername, profileRank, profileScore, profileTime, profilePassword;
    TextView titleName, titleUsername;
    Button editProfile, showMap, signOut;
    //EditText profilePassword;
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
        signOut = findViewById(R.id.signOutButton);

        //ImageButton showPasswordButton = findViewById(R.id.showPasswordButton);
        //profilePassword = findViewById(R.id.profilePassword);
//        showPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Đảo ngược trạng thái hiển thị mật khẩu
//                if (profilePassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
//                    profilePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    showPasswordButton.setImageResource(R.drawable.hopcauhoi1);
//                } else {
//                    profilePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    showPasswordButton.setImageResource(R.drawable.ic_baseline_lock_24);
//                }
//
//                // Di chuyển con trỏ về cuối chuỗi mật khẩu
//                profilePassword.setSelection(profilePassword.getText().length());
//            }
//        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        profileRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });

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
                passUserDataToGame();
            }
        });

//        Button buttonAddQuestion = findViewById(R.id.buttonAddQuestion);
//        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Chuyển sang AddQuestionActivity
//                Intent intent = new Intent(ProfileActivity.this, AddQuestionActivity.class);
//                startActivity(intent);
//            }
//        });
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

    public void passUserDataToGame() {
        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer scoreDB = snapshot.child(userUsername).child("score").getValue(Integer.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    float timeDB = snapshot.child(userUsername).child("time").getValue(Float.class);

                    Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);

                    intent.putExtra("score", scoreDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("time", timeDB);

                    startActivityForResult(intent, 1); // Sử dụng startActivityForResult để nhận kết quả từ MapsActivity
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Nhận dữ liệu trả về từ MapsActivity
                int newScore = data.getIntExtra("newScore", 0);
                float additionalTime = data.getFloatExtra("additionalTime", 0.0f);

                // Cập nhật thời gian trên giao diện
                //float currentTime = Float.parseFloat(profileTime.getText().toString());
                float updatedTime = additionalTime;
                profileTime.setText(String.valueOf(updatedTime));

                updateScoreAndTimeOnFirebase(profileUsername.getText().toString(), newScore, additionalTime);

                // Cập nhật điểm trên Firebase
                //updateScoreOnFirebase(profileUsername.getText().toString(), newScore);

                // Cập nhật điểm trên giao diện
                profileScore.setText(String.valueOf(newScore));

                // Cập nhật rank trên Firebase và trên giao diện
                updateRankOnFirebase(profileUsername.getText().toString(), newScore);
            }
        }
    }

    private void updateScoreOnFirebase(String username, int newScore) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);

        userRef.child("score").setValue(newScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Điểm đã được cập nhật thành công
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Lỗi khi cập nhật điểm
                    }
                });
    }

    private void updateScoreAndTimeOnFirebase(String username, int newScore, float additionalTime) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);

        userRef.child("score").setValue(newScore);
        userRef.child("time").setValue(additionalTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Điểm và thời gian đã được cập nhật thành công
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Lỗi khi cập nhật điểm và thời gian
                    }
                });
    }

    private void updateRankOnFirebase(String username, int newScore) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);

        userRef.child("score").setValue(newScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Điểm đã được cập nhật thành công
                        // Gọi phương thức để cập nhật rank trên Firebase
                        calculateAndUpdateRank();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Lỗi khi cập nhật điểm
                    }
                });
    }

    private void calculateAndUpdateRank() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalPlayers = (int) dataSnapshot.getChildrenCount();
                int rank = totalPlayers;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);

                    if (username.equals(profileUsername.getText().toString())) {
                        // Cập nhật rank trên giao diện
                        profileRank.setText(String.valueOf(rank));
                        // Cập nhật rank trên Firebase
                        snapshot.getRef().child("rank").setValue(rank);
                        break;
                    }

                    rank--;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}