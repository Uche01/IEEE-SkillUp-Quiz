package com.example.skillupquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

public class CourseDetails extends AppCompatActivity {

    ImageView imageView;
    TextView course_title_tv;
    TextView course_desc_tv;
    TextView instructor_name_tv;
    TextView instuctor_bio_tv;
    Button start_btn;

    ArrayList<CourseModel> courses;
    CourseModel course;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courses = StaticList.getInstance().getCourses();
        position = getIntent().getIntExtra("position", 0);
        course = courses.get(position);

        imageView = findViewById(R.id.image_view);
        course_title_tv = findViewById(R.id.tv_course_title);
        course_desc_tv = findViewById(R.id.tv_course_desc);
        instructor_name_tv = findViewById(R.id.tv_instructor_name);
        instuctor_bio_tv = findViewById(R.id.tv_instructor_desc);
        start_btn = findViewById(R.id.start_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetails.this, CBT.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        String url_string = course.getCourse_image_url();
        /*try {
            URL course_url = new URL(url_string);
            Bitmap bitmap = BitmapFactory.decodeStream(course_url.openConnection().getInputStream());
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e){
//            Log.e("TAG", e.getMessage());
        }*/

        Glide.with(this).load(url_string).into(imageView);

        course_title_tv.setText(course.getCourse_title());
        course_desc_tv.setText(course.getCourse_description());
        instructor_name_tv.setText(course.getInstructor_name());
        instuctor_bio_tv.setText(course.getInstructor_bio());


    }
}
