package com.example.game_giaido;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText editTextQuestion, editTextAnswerA, editTextAnswerB, editTextAnswerC, editTextAnswerD;
    private RadioGroup radioGroup;
    private Button buttonSubmit;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        // Khởi tạo các thành phần giao diện
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswerA = findViewById(R.id.editTextAnswerA);
        editTextAnswerB = findViewById(R.id.editTextAnswerB);
        editTextAnswerC = findViewById(R.id.editTextAnswerC);
        editTextAnswerD = findViewById(R.id.editTextAnswerD);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions");

        // Sự kiện khi người dùng bấm nút "Gửi câu hỏi"
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionToFirebase();
            }
        });
    }

    private void addQuestionToFirebase() {
        // Lấy dữ liệu từ các EditText và RadioButton
        String questionText = editTextQuestion.getText().toString();
        String answerA = editTextAnswerA.getText().toString();
        String answerB = editTextAnswerB.getText().toString();
        String answerC = editTextAnswerC.getText().toString();
        String answerD = editTextAnswerD.getText().toString();
        String correctAnswer = getSelectedAnswer();

        // Kiểm tra xem đã nhập đầy đủ thông tin chưa
        if (questionText.isEmpty() || answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty() || answerD.isEmpty() || correctAnswer.isEmpty()) {
            // Hiển thị thông báo cho người dùng rằng cần nhập đầy đủ thông tin
            // Ví dụ: Toast.makeText(AddQuestionActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            // Tạo đối tượng Question và thêm vào Firebase Realtime Database
            Question question = new Question(questionText, answerA, answerB, answerC, answerD, correctAnswer);
            databaseReference.push().setValue(question);

            // Hiển thị thông báo cho người dùng rằng câu hỏi đã được gửi thành công
            // Ví dụ: Toast.makeText(AddQuestionActivity.this, "Câu hỏi đã được gửi thành công", Toast.LENGTH_SHORT).show();

            // Xóa nội dung của các EditText sau khi gửi câu hỏi thành công
            Toast.makeText(this, "Da them cau hoi thanh cong!", Toast.LENGTH_LONG);
            clearEditTexts();
        }
    }

    private String getSelectedAnswer() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    private void clearEditTexts() {
        editTextQuestion.setText("");
        editTextAnswerA.setText("");
        editTextAnswerB.setText("");
        editTextAnswerC.setText("");
        editTextAnswerD.setText("");
        radioGroup.clearCheck();
    }
}
