package com.example.skillupquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CBT extends AppCompatActivity {
    LinearLayout linearLayout;
    ProgressBar progressBar;
    TextView noQuestTV;

    TextView questNoTV;
    TextView questTV;
    RadioGroup radioGroup;
    RadioButton optionA_RB;
    RadioButton optionB_RB;
    RadioButton optionC_RB;
    RadioButton optionD_RB;
    Button prevBTN;
    Button submitBTN;
    Button nextBTN;

    FirebaseFirestore database;
    CollectionReference questionReference;

    int position;
    int questCount = 0;
    int questNum = 0;

    View.OnClickListener radBClickListener;

    ArrayList<QuestionModel> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbt);

        position = getIntent().getIntExtra("position", 0);
        String id = StaticList.getInstance().getCourses().get(position).getId();

        linearLayout = findViewById(R.id.quest_LL);
        progressBar = findViewById(R.id.progressBar);
        noQuestTV = findViewById(R.id.no_quest_TV);

        questNoTV = findViewById(R.id.quest_no_TV);
        questTV = findViewById(R.id.quest_TV);
        optionA_RB = findViewById(R.id.optionA_RB);
        optionB_RB = findViewById(R.id.optionB_RB);
        optionC_RB = findViewById(R.id.optionC_RB);
        optionD_RB = findViewById(R.id.optionD_RB);
        prevBTN = findViewById(R.id.prev_BTN);
        submitBTN = findViewById(R.id.submit_BTN);
        nextBTN = findViewById(R.id.next_BTN);
        radioGroup = findViewById(R.id.options_RG);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                QuestionModel currentQuestion = questions.get(questNum);
                RadioButton radioButton = group.findViewById(checkedId);
                int checkedPosition = group.indexOfChild(radioButton);
                currentQuestion.setCheckedPosition(checkedPosition);
                currentQuestion.setCheckedId(checkedId);
                questions.set(questNum, currentQuestion);
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CBT.this)
                        .setTitle("Submit")
                        .setMessage("Are you sure you want to submit? this will submit and end quiz")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                double score = calculateScore(questions);
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                String scoreStr = "You scored " + decimalFormat.format((score/questCount)*100) + " %";
                                new AlertDialog.Builder(CBT.this)
                                        .setTitle("Result")
                                        .setMessage(scoreStr)
                                        .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                startActivity(new Intent(CBT.this, MainActivity.class));
                                            }
                                        })
                                        .setNegativeButton("Retake Test", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                Intent intent = new Intent(CBT.this, CBT.class);
                                                intent.putExtra("position", position);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        progressBar.setVisibility(View.VISIBLE);

        database = FirebaseFirestore.getInstance();
        questionReference = database.collection("courses")
                .document(id).collection("questions");
        questionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String question = document.getString("question");
                        String optionA = document.getString("optionA");
                        String optionB = document.getString("optionB");
                        String optionC = document.getString("optionC");
                        String optionD = document.getString("optionD");
                        int answer =   Integer.parseInt(document.get("answer").toString());

                        QuestionModel questionModel = new QuestionModel(question, optionA,
                                optionB, optionC, optionD, answer);
                        questions.add(questionModel);
                    }
                    questCount = questions.size();
                    if (questCount != 0 && questCount > 0) {
                        loadQuestion(questNum);
                    }
                    else if (questCount == 0) {
                        linearLayout.setVisibility(View.GONE);
                        noQuestTV.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    noQuestTV.setVisibility(View.VISIBLE);
                }
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questNum < questCount-1) {
                    questNum++;
                    loadQuestion(questNum);
                }
                else {
                    Toast.makeText(CBT.this, "This is the last question",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questNum > 0) {
                    questNum--;
                    loadQuestion(questNum);
                }
                else {
                    Toast.makeText(CBT.this, "This is the first question",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuestion(int index) {
        QuestionModel question = questions.get(index);
        int questNum = index + 1;
        if (question.getCheckedId()==-1){
            radioGroup.clearCheck();
        }
        else
            radioGroup.check(question.getCheckedId());

        questNoTV.setText("Question " + questNum + " of " + questCount);
        questTV.setText(question.getQuestion());
        optionA_RB.setText(question.getOptionA());
        optionB_RB.setText(question.getOptionB());
        optionC_RB.setText(question.getOptionC());
        optionD_RB.setText(question.getOptionD());
    }

    private double calculateScore(ArrayList<QuestionModel> questionModels){
        double totalScore = 0;
        for (QuestionModel questionModel : questionModels){
            if(questionModel.getAnswerPosition() == questionModel.getCheckedPosition()){
                totalScore++;
            }
        }
        return totalScore;
    }
}
