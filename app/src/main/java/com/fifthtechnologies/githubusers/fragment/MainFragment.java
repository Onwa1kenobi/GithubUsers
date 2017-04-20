package com.fifthtechnologies.githubusers.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fifthtechnologies.githubusers.R;
import com.fifthtechnologies.githubusers.adapter.GitHubUsersListAdapter;
import com.fifthtechnologies.githubusers.utils.GitHubClient;
import com.fifthtechnologies.githubusers.utils.GitHubResponse;
import com.fifthtechnologies.githubusers.utils.GitHubUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements GitHubUsersListAdapter.MainFragmentInterface {

    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private BottomSheetDialog mBottomSheetDialog;
    private View bottomView;

    private GitHubClient mClient;
    private List<GitHubUser> mGitHubUsers = new ArrayList<>();
    private int page = 1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.retryOnConnectionFailure(true);

        String API_BASE_URL = "https://api.github.com/";
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        mClient = retrofit.create(GitHubClient.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_main, container, false);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchGitHubUsers();
    }

    private void fetchGitHubUsers() {
        Map<String, String> data = new HashMap<>();
        data.put("location", "lagos");
        data.put("language", "java");

//        Call<List<GitHubUser>> call = mClient.getUsers(data);


//        Call<GitHubResponse> listCall = mClient.getUsers("location:lagos+language:java&sort=joined&order=desc&per_page=100");
        Call<GitHubResponse> listCall = mClient.getUsers("location:lagos+language:java", "joined", "desc", "100");
        listCall.enqueue(new Callback<GitHubResponse>() {
            @Override
            public void onResponse(Call<GitHubResponse> call, Response<GitHubResponse> response) {
                if (response.isSuccessful()) {

                    view.findViewById(R.id.loading_indicator).setVisibility(View.GONE);

                    mGitHubUsers = response.body().getGitHubUserList();

                    if (mRecyclerView.getAdapter() == null) {
                        mAdapter = new GitHubUsersListAdapter(getActivity(), mGitHubUsers, MainFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        //Refill adapter
                        ((GitHubUsersListAdapter) mRecyclerView.getAdapter()).refill(mGitHubUsers);
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_fragment_coordinator),
                            "The response was not successful.", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(),
                            R.color.colorAccent));
                    snackbar.setActionTextColor(ContextCompat.getColor(getActivity(),
                            R.color.colorPrimaryDark));
                    snackbar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchGitHubUsers();
                        }
                    });
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<GitHubResponse> call, Throwable throwable) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_fragment_coordinator),
                        throwable.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.colorAccent));
                snackbar.setActionTextColor(ContextCompat.getColor(getActivity(),
                        R.color.colorPrimaryDark));
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchGitHubUsers();
                    }
                });
                snackbar.show();
            }
        });
    }

    @Override
    public void onUserClicked(final GitHubUser user) {
        bottomView = getActivity().getLayoutInflater().inflate(R.layout.modal_bottom_sheet, null);
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(bottomView);

        ImageView userImage = (ImageView) bottomView.findViewById(R.id.user_image);
        TextView username = (TextView) bottomView.findViewById(R.id.username);
        TextView userLink = (TextView) bottomView.findViewById(R.id.link);
        Button shareButton = (Button) bottomView.findViewById(R.id.share_button);

        username.setText(user.getLogin());

        userLink.setText(user.getHtml_url());
        userLink.setTextColor(Color.BLUE);
        userLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(user.getHtml_url());
            }
        });

        Glide.with(getActivity())
                .load(user.getAvatar_url())
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .crossFade()
                .into(userImage);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserProfile(user.getLogin(), user.getHtml_url()
                );
            }
        });

        mBottomSheetDialog.show();
    }

    private void shareUserProfile(String username, String link) {
        String shareText = "Check out this awesome developer @" + username + ",\n" + link;

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Send Via..."));
    }

    private void loadUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));

        Intent chooser = Intent.createChooser(browserIntent, "Open with...");

        if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
