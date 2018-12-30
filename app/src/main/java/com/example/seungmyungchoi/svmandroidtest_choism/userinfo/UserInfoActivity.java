package com.example.seungmyungchoi.svmandroidtest_choism.userinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seungmyungchoi.svmandroidtest_choism.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class UserInfoActivity extends AppCompatActivity {
    private final String TAG = "UserInfoActivity";
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    //UI Set
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvCompany;
    private TextView tvFollowers;
    private TextView tvFollowing;

    //var Set
    private String login;
    private int page;
    private ArrayList<UserRepoListItem> userRepoListItems = new ArrayList<>();

    //recyclerView Set
    private RecyclerView userRepoListRecyclerView;
    private RecyclerView.LayoutManager userRepoListLayoutManager;
    private UserRepoListAdapter userRepoListAdapter;
    private SwipeRefreshLayout userRepoRefresh;


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private interface GitHubUserService {
        //Get User Info - avatar_url, login, name, location, company, followers, following
        @GET("users/{user}")
        Call<JsonObject> getUserInfo(@Path("user") String login,
                                     @Query("client_id")String client_id,
                                     @Query("client_secret") String client_secret);

        @GET("users/{user}/repos")
        Call<JsonArray> getUserRepo(@Path("user") String login,
                                    @Query("page") int page,
                                    @Query("client_id")String client_id,
                                    @Query("client_secret") String client_secret);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        page = 0;
        CLIENT_ID = getResources().getString(R.string.client_id);
        CLIENT_SECRET = getResources().getString(R.string.client_secret);
        //User Info Layout Set
        ivAvatar = (ImageView) findViewById(R.id.activity_user_info_userprofile);
        tvUserName = (TextView) findViewById(R.id.activity_user_info_username);
        tvName = (TextView) findViewById(R.id.activity_user_info_name);
        tvLocation = (TextView) findViewById(R.id.activity_user_info_location);
        tvCompany = (TextView) findViewById(R.id.activity_user_info_company);
        tvFollowers = (TextView) findViewById(R.id.activity_user_info_followers);
        tvFollowing = (TextView) findViewById(R.id.activity_user_info_following);

        //User Repo List Layout Set
        userRepoListRecyclerView = (RecyclerView) findViewById(R.id.activity_user_info_recyclerview);
        userRepoListRecyclerView.setHasFixedSize(true);
        userRepoListLayoutManager = new LinearLayoutManager(this);
        userRepoListRecyclerView.setLayoutManager(userRepoListLayoutManager);
        userRepoRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_user_info_refresh);

        login = getIntent().getStringExtra("login");
        Log.e(TAG+" login ",login);

        makeUserInfo(login);

        userRepoListAdapter = new UserRepoListAdapter(userRepoListItems);
        userRepoListRecyclerView.setAdapter(userRepoListAdapter);
        makeUserRepoInfo(login, ++page);

        //refreshSet
        userRepoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userRepoListItems.clear();
                page = 0;
                makeUserRepoInfo(login, ++page);
                userRepoRefresh.setRefreshing(false);
            }
        });

        userRepoListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastItem >= userRepoListItems.size() - 3) {
                    makeUserRepoInfo(login, ++page);
                }
            }
        });
    }

    private void makeUserRepoInfo(String login, final int page1) {
        GitHubUserService service = retrofit.create(GitHubUserService.class);
        Call<JsonArray> request = service.getUserRepo(login, page1,CLIENT_ID,CLIENT_SECRET);
        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.body().toString().equals(" []")) {
                    for (int i = 0; i < response.body().size(); i++) {
                        String repoName = response.body().get(i).getAsJsonObject().get("name").toString();
                        String repoDescription = response.body().get(i).getAsJsonObject().get("description").toString();
                        String repoStartgazersCount = response.body().get(i).getAsJsonObject().get("stargazers_count").toString();
                        String repoWatcherCount = response.body().get(i).getAsJsonObject().get("watchers").toString();
                        String repoCreatedAt = response.body().get(i).getAsJsonObject().get("created_at").toString();

                        UserRepoListItem userRepoListItem = new UserRepoListItem(repoName, repoDescription, repoStartgazersCount, repoWatcherCount, repoCreatedAt);
                        userRepoListItems.add(userRepoListItem);
                    }
                    userRepoListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void makeUserInfo(String login) {
        GitHubUserService service = retrofit.create(GitHubUserService.class);
        Call<JsonObject> request = service.getUserInfo(login,CLIENT_ID,CLIENT_SECRET);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String avatar_url = response.body().get("avatar_url").toString().trim();
                final String userName = response.body().get("login").toString();
                final String name = response.body().get("name").toString();
                final String location = response.body().get("location").toString();
                final String company = response.body().get("company").toString();
                final String followers = response.body().get("followers").toString();
                final String following = response.body().get("following").toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(avatar_url.replace("\"", "")).into(ivAvatar);

                        tvUserName.setText(userName.replace("\"", ""));

                        //null handle
                        if (!name.equals("null")) {
                            tvName.setText(name.replace("\"", ""));
                        }
                        if (!location.equals("null")) {
                            tvLocation.setText(location.replace("\"", ""));
                        }
                        if (!company.equals("null")) {
                            tvCompany.setText(company.replace("\"", ""));
                        }

                        tvFollowers.setText("followers : " + followers);
                        tvFollowing.setText("following : " + following);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
