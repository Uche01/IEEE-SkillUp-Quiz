package com.example.skillupquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder> {
     class CoursesViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTV;
        TextView instructorNameTV;
        TextView studentCountTV;
        RelativeLayout rl_container;


        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.course_title_tv);
            instructorNameTV = itemView.findViewById(R.id.instructor_name_tv);
            studentCountTV = itemView.findViewById(R.id.student_count_tv);
            rl_container = itemView.findViewById(R.id.course_layout);
        }
    }
    Activity activity;
    ArrayList<CourseModel> courses;

    public CoursesAdapter(Activity activity, ArrayList<CourseModel> courses) {
        this.activity = activity;
        this.courses = courses;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        CoursesViewHolder viewHolder = new CoursesViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, final int position) {
        CourseModel course = courses.get(position);
        holder.courseNameTV.setText(course.getCourse_title());
        holder.instructorNameTV.setText("By "+course.getInstructor_name());
        holder.studentCountTV.setText(String.valueOf(course.getStudent_count())+" Students");

        holder.rl_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), CourseDetails.class);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
