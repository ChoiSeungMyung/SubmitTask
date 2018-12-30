package com.example.seungmyungchoi.svmandroidtest_choism.userlist;

public class UserListItem {
    public String avatarUrl;
    public int id;
    public String login;

    public UserListItem(String avatarUrl, int id, String login){
        this.avatarUrl = avatarUrl;
        this.id = id;
        this.login = login.replace("\"","");
    }

    public void setAvatar_url(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }
    public void setLogin(String login){
        this.login = login;
    }

    public String getAvatarUrl(){
        return this.avatarUrl.replace("\"",""); // Api의 응답값에서 그대로 넣어버리면 "때문에 picasso에서 에러 일으킴, String.replace(target, replace)이용해서 " 제거 해줌
    }
    public String getLogin(){
        return this.login.replace("\"","");
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserListItem{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}

