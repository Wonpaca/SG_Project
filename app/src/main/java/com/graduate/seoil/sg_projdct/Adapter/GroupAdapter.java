package com.graduate.seoil.sg_projdct.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.graduate.seoil.sg_projdct.ChatActivity;
import com.graduate.seoil.sg_projdct.GroupInformation;
import com.graduate.seoil.sg_projdct.Model.Group;
import com.graduate.seoil.sg_projdct.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by baejanghun on 28/03/2019.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private Context mContext;
    private List<Group> mGroups;

    private JSONArray jsonArray;
    private JSONObject jsonObject;

    FirebaseUser fuser;

    public GroupAdapter(Context mContext, List<Group> mGroups) {
        this.mGroups = mGroups;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_item, viewGroup, false);
        return new GroupAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Group group = mGroups.get(i);
        viewHolder.group_title.setText(group.getTitle());
        viewHolder.group_planTime.setText(String.format("#") + String.valueOf(group.getPlanTime() / 60) + String.format("시간"));
        viewHolder.group_dayCycle.setText(String.format("#") + group.getDayCycle());
        viewHolder.group_current_user.setText(String.valueOf(group.getcurrent_user()));
        viewHolder.group_maxCount.setText(String.valueOf(group.getUser_max_count()));

        final String admin_uid = group.getAdminName();
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        // 그룹 리스트 프로필 사진.
        // TODO : [3월 30일 에러] 앱 내에서 DB 읽기도 전에 액션을 하면 뻑 나는 경우 있음.
        if (group.getImageURL().equals("default")) {
            viewHolder.group_profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(group.getImageURL()).into(viewHolder.group_profile_image);
        }

        // 그룹 리스트 클릭시 그룹 이동(그룹 가입 X시 그룹 정보로 이동)
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admin_uid.equals(fuser.getUid())) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, GroupInformation.class);

                    // 그룹 리스트에서 그룹 정보 액티비티로 데이터 전달
                    Bundle bundle = new Bundle();
                    bundle.putString("group_id", group.getAdminName());
                    bundle.putString("group_title", group.getTitle());
                    bundle.putString("group_registDate", group.getRegistDate());
                    bundle.putString("group_currentUser", String.valueOf(group.getcurrent_user()));
                    bundle.putString("group_maxUser", String.valueOf(group.getUser_max_count()));
                    bundle.putString("group_planTime", String.valueOf(group.getPlanTime() / 60));
                    bundle.putString("group_dayCycle", group.getDayCycle());
                    bundle.putString("group_announce", group.getContent());
                    intent.putExtras(bundle);

                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView group_title;
        public ImageView group_profile_image;
        public TextView group_planTime;
        public TextView group_dayCycle;
        public TextView group_current_user;
        public TextView group_maxCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group_title = itemView.findViewById(R.id.tv_groupList_title);
            group_profile_image = itemView.findViewById(R.id.groupList_profile_image);
            group_planTime = itemView.findViewById(R.id.tv_groupList_hashTime);
            group_dayCycle = itemView.findViewById(R.id.tv_groupList_hashCycle);
            group_current_user = itemView.findViewById(R.id.tv_groupList_currentUser);
            group_maxCount = itemView.findViewById(R.id.tv_groupList_maxCount);
        }
    }

}