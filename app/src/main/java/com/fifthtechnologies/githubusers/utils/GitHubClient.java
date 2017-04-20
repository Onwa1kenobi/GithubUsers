package com.fifthtechnologies.githubusers.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ameh on 19/04/2017.
 */

public interface GitHubClient {
    @GET("/search/users")
    Call<GitHubResponse> getUsers(
//            @QueryMap Map<String, String> filter
            @Query(value = "q", encoded = true) String filter,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("per_page") String count
//            @Query("location") String location,
//            @Query("language") String language
    );

}
