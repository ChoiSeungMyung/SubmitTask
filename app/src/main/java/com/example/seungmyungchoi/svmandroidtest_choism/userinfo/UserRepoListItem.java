package com.example.seungmyungchoi.svmandroidtest_choism.userinfo;

import android.util.Log;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserRepoListItem {
    private String repoName;
    private String repoDescription;
    private String repoStargazersCount;
    private String repoWatchersCount;
    private String repoCreatedAt;

    public UserRepoListItem(String repoName, String repoDescription, String repoStargazersCount, String repoWatchersCount, String repoCreatedAt) {
        this.repoName = repoName;
        this.repoDescription = repoDescription;
        this.repoStargazersCount = repoStargazersCount;
        this.repoWatchersCount = repoWatchersCount;
        this.repoCreatedAt = repoCreatedAt;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepoDescription(String repoDescription) {
        this.repoDescription = repoDescription;
    }

    public void setRepoStargazersCount(String repoStargazersCount) {
        this.repoStargazersCount = repoStargazersCount;
    }

    public void setRepoWatchersCount(String repoWatchersCount) {
        this.repoWatchersCount = repoWatchersCount;
    }

    public void setRepoCreatedAt(String repoCreatedAt) {
        this.repoCreatedAt = repoCreatedAt;
    }

    public String getRepoName() {
        return this.repoName.replace("\"", "");
    }

    public String getRepoDescription() {
        if (repoDescription.equals("null")) {
            return "";
        }
        return this.repoDescription.replace("\"", "");
    }

    public String getRepoStargazersCount() {
        return this.repoStargazersCount;
    }

    public String getRepoWatchersCount() {
        return this.repoWatchersCount;
    }

    public String getRepoCreatedAt() {
        try {
            SimpleDateFormat convertDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date originalDate = convertDate.parse(repoCreatedAt.replace("\"", "").replace("T", " ").replace("Z", ""));
            SimpleDateFormat viewDate = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            this.repoCreatedAt = viewDate.format(originalDate);
        } catch (ParseException e) {

        }
        return this.repoCreatedAt;
    }

    @Override
    public String toString() {
        return "UserRepoListItem{" +
                "repoName='" + repoName + '\'' +
                ", repoDescription='" + repoDescription + '\'' +
                ", repoStargazersCount='" + repoStargazersCount + '\'' +
                ", repoWatchersCount='" + repoWatchersCount + '\'' +
                ", repoCreatedAt='" + repoCreatedAt + '\'' +
                '}';
    }
}