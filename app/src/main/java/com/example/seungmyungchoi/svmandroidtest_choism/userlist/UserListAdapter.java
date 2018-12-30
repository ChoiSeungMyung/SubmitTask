package com.example.seungmyungchoi.svmandroidtest_choism.userlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seungmyungchoi.svmandroidtest_choism.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        private ImageView userProfileImage;
        private TextView userId;
        private TextView userName;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userItem_user_profile_image);
            userId = itemView.findViewById(R.id.userItem_user_id);
            userName = itemView.findViewById(R.id.userItem_user_name);
        }
    }

    private ArrayList<UserListItem> userListItems;

    public UserListAdapter(ArrayList<UserListItem> userListItems){
        this.userListItems = userListItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return new UserListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        UserListViewHolder userListViewHolder = (UserListViewHolder) viewHolder;
        //이미지 처리 picasso 이용 처리

        Picasso.get().load(userListItems.get(position).getAvatarUrl()).into(userListViewHolder.userProfileImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        userListViewHolder.userId.setText(""+userListItems.get(position).getId());
        userListViewHolder.userName.setText(userListItems.get(position).getLogin());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return userListItems.size();
    }
}
