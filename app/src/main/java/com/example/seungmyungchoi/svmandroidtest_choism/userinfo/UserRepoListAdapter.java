package com.example.seungmyungchoi.svmandroidtest_choism.userinfo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.seungmyungchoi.svmandroidtest_choism.R;


import java.util.ArrayList;

/*Todo
    주석작성 & 코드정리
    다른해상도에서 확인해보기
     */

public class UserRepoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public class UserRepoListHolder extends RecyclerView.ViewHolder{
        private TextView repoName;
        private TextView repoDescription;
        private TextView repoStargazersCount;
        private TextView repoWatchersCount;
        private TextView repoCreatedAt;

        public UserRepoListHolder(@NonNull View itemView) {
            super(itemView);
            repoName = (TextView)itemView.findViewById(R.id.user_info_repo_item_repository_name);
            repoName.setSelected(true);
            repoDescription = (TextView)itemView.findViewById(R.id.user_info_repo_item_description);
            repoDescription.setSelected(true);
            repoStargazersCount = (TextView)itemView.findViewById(R.id.user_info_repo_item_star);
            repoWatchersCount = (TextView)itemView.findViewById(R.id.user_info_repo_item_watcher);
            repoCreatedAt = (TextView)itemView.findViewById(R.id.user_info_repo_item_created_at);
        }
    }

    private ArrayList<UserRepoListItem> userRepoListItems;
    public UserRepoListAdapter(ArrayList<UserRepoListItem> userRepoListItems){
        this.userRepoListItems = userRepoListItems;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        UserRepoListHolder userRepoListHolder = (UserRepoListHolder) viewHolder;

        userRepoListHolder.repoName.setText(userRepoListItems.get(i).getRepoName());
        userRepoListHolder.repoDescription.setText(userRepoListItems.get(i).getRepoDescription());
        userRepoListHolder.repoStargazersCount.setText("star : "+userRepoListItems.get(i).getRepoStargazersCount());
        userRepoListHolder.repoWatchersCount.setText("watcher : "+userRepoListItems.get(i).getRepoWatchersCount());
        userRepoListHolder.repoCreatedAt.setText(userRepoListItems.get(i).getRepoCreatedAt());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_info_repo_item,viewGroup,false);
        return new UserRepoListHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return userRepoListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
