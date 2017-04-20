package com.fifthtechnologies.githubusers.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ameh on 19/04/2017.
 */

public class GitHubResponse {
    @SerializedName("items")
    private List<GitHubUser> items;

    public GitHubResponse() {

    }

    public List<GitHubUser> getGitHubUserList() {
        return items;
    }
}
