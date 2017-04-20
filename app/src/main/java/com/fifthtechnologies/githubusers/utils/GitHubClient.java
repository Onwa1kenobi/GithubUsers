package com.fifthtechnologies.githubusers.utils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ameh on 19/04/2017.
 */

public interface GitHubClient {
    @Headers("Authorization: token ae83237ec881dfa65587a5d66e123f82451d1cf2")

    @GET("/search/users")
    Call<GitHubResponse> getUsers(
//            @QueryMap Map<String, String> filter
            @Query(value = "q", encoded = true)  String filter,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("per_page") String count
//            @Query("location") String location,
//            @Query("language") String language
    );

}
