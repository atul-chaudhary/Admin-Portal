package com.example.atulc.adminportal_roots;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewFastAdapter extends AbstractItem<RecyclerViewFastAdapter, RecyclerViewFastAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewFastAdapter";
    private int userId;
    private String hashname;
    private String email;
    private String imageUrl;
    private String name;
    private String subdivision;
    private String year;
    private String section;
    private int teacher_or_student;
    private String divisionA;
    private String divisionB;
    private String divisionC;

    public int getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacher_or_student() {
        return teacher_or_student;
    }

    public void setTeacher_or_student(int teacher_or_student) {
        this.teacher_or_student = teacher_or_student;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDivisionA() {
        return divisionA;
    }

    public String getHashname() {
        return hashname;
    }

    public void setHashname(String hashname) {
        this.hashname = hashname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDivisionA(String divisionA) {
        this.divisionA = divisionA;
    }

    public String getDivisionB() {
        return divisionB;
    }

    public void setDivisionB(String divisionB) {
        this.divisionB = divisionB;
    }

    public String getDivisionC() {
        return divisionC;
    }

    public void setDivisionC(String divisionC) {
        this.divisionC = divisionC;
    }

    @Override
    public int getType() {
        return R.id.action_settings;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.search_recycleview_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        Log.e(TAG, "bindView: " + getImageUrl());
        Log.e(TAG, "bindView: " + getName());
        Log.e(TAG, "bindView: " + getTeacher_or_student());
        Log.e(TAG, "bindView:outside  >>>>" + getTeacher_or_student());

        if (getTeacher_or_student() == 1) {

            Picasso.get().load(getImageUrl()).transform(new CircleTransform()).into(viewHolder.usersProfile);
            viewHolder.usersName.setText(getName());
            viewHolder.teacher_or_student_logo.setVisibility(View.VISIBLE);
            viewHolder.divisionA.setText(getDivisionA());
            viewHolder.divisionB.setText(getDivisionB());
            viewHolder.divisionC.setVisibility(View.GONE);


        } else {

            Picasso.get().load(getImageUrl()).transform(new CircleTransform()).into(viewHolder.usersProfile);
            viewHolder.usersName.setText(getName());
            viewHolder.teacher_or_student_logo.setVisibility(View.INVISIBLE);
            viewHolder.divisionA.setText(getDivisionA());
            viewHolder.divisionB.setText(getDivisionB());
            viewHolder.divisionC.setVisibility(View.VISIBLE);
            viewHolder.divisionC.setText(getDivisionC());


        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView usersProfile;
        TextView usersName;
        ImageView teacher_or_student_logo;
        TextView divisionA;
        TextView divisionB;
        TextView divisionC;

        public ViewHolder(View view) {
            super(view);

            usersProfile = view.findViewById(R.id.profile_image_users);
            usersName = view.findViewById(R.id.users_name);
            teacher_or_student_logo = view.findViewById(R.id.teacher_or_student_icon);
            divisionA = view.findViewById(R.id.division_users);
            divisionB = view.findViewById(R.id.branch_year_users);
            divisionC = view.findViewById(R.id.section_users);

        }
    }
}
