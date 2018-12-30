package com.example.seungmyungchoi.svmandroidtest_choism.userlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.seungmyungchoi.svmandroidtest_choism.R;
import com.example.seungmyungchoi.svmandroidtest_choism.userinfo.UserInfoActivity;
import com.example.seungmyungchoi.svmandroidtest_choism.util.RecyclerViewOnItemClickListener;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
    1. 어플 실행시 바로 실행 됨
    2. onCreated()에서 UI를 비롯한 필요한 값 초기화
    3. RecyclerView 어댑터, 홀더는 클래스 파일을 따로 빼서 관리
    4. Retrofit을 이용해 비동기 방식으로 유저 리스트 받아오기
    5. API를 통해 Json(Object or Array) 형식으로 받아온 값을 ArrayList에 넣어줌
    6. 새로고침 될 때마다 ArrayList에 add(item)을 이용해 뒤에 붙여줌
        6-1. SwipeRefreshLayout을 이용해 최상단에서 밑으로 스크롤시 새로고침 동작
        6-2. 목록 마지막쯤 도착했을때 아이템 추가 동작 (RecyclerView의 ScrollListener를 이용해서 목록의 마지막 파악)
            6-2-1. 목록의 마지막 '쯤'에서 불러오기 위해 "userListItems.size() - 3"을 이용함(-1 = 아이템 최하단)
    7. 아이템 클릭시 "login" 정보를 인텐트로 전달해줌
    8. "login" 정보를 가지고 repository 정보를 가져옴
 */
public class UserListActivity extends AppCompatActivity {
    private final String TAG = "UserListActivity";
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    //RecyclerView Set
    private RecyclerView userListRecyclerView;
    private RecyclerView.LayoutManager userListLayoutManager;
    private UserListAdapter userListAdapter;

    //SwipeResfresh View
    private SwipeRefreshLayout swipeRefreshLayout;

    //RecyclerView Item Set
    private ArrayList<UserListItem> userListItems = new ArrayList<>();

    private int since;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        CLIENT_ID = getResources().getString(R.string.client_id);
        CLIENT_SECRET = getResources().getString(R.string.client_secret);
        since = 0;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_user_list_swiperefresh);
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userListItems.clear();
                since = 0;
                makeUserList();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        userListRecyclerView = (RecyclerView) findViewById(R.id.activity_user_List_recyclerview);
        userListRecyclerView.setHasFixedSize(true);

        userListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastItem >= userListItems.size() - 3) {
                    makeUserList();
                }
            }
        });

        userListRecyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(), userListRecyclerView, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                intent.putExtra("login", userListItems.get(position).login);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        userListLayoutManager = new LinearLayoutManager(this);
        userListRecyclerView.setLayoutManager(userListLayoutManager);

        userListAdapter = new UserListAdapter(userListItems);
        userListRecyclerView.setAdapter(userListAdapter);
        makeUserList();
    }


    private interface GitHubUserService {
        @GET("users")
        Call<JsonArray> getUsers(
                @Query("since") int since,
                @Query("client_id") String client_id,
                @Query("client_secret") String client_secret);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void makeUserList() {
        GitHubUserService service = GitHubUserService.retrofit.create(GitHubUserService.class);
        Call<JsonArray> request = service.getUsers(since, CLIENT_ID, CLIENT_SECRET);
        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    String avatar_url = response.body().get(i).getAsJsonObject().get("avatar_url").toString().trim();
                    int id = response.body().get(i).getAsJsonObject().get("id").getAsInt();
                    String login = response.body().get(i).getAsJsonObject().get("login").toString();

                    UserListItem userListItem = new UserListItem(avatar_url, id, login);
                    userListItems.add(userListItem);
                }

                since = userListItems.get(userListItems.size() - 1).id;
                Log.e("since", "" + since);
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
