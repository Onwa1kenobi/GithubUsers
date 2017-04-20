package com.fifthtechnologies.githubusers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fifthtechnologies.githubusers.R;
import com.fifthtechnologies.githubusers.fragment.MainFragment;
import com.fifthtechnologies.githubusers.utils.GitHubUser;

import java.util.List;

/**
 * Created by Jules on 8/24/2016.
 */
public class GitHubUsersListAdapter extends RecyclerView.Adapter<GitHubUsersListAdapter.ViewHolder> {

    private final MainFragmentInterface mListener;
    List<GitHubUser> mUsers;
    Context mContext;

    public interface MainFragmentInterface {
        void onUserClicked(GitHubUser user);
    }

    public GitHubUsersListAdapter(Context context, List<GitHubUser> users, MainFragment listener) {
        mUsers = users;
        mContext = context;
        mListener = listener;
    }

    @Override
    public GitHubUsersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GitHubUsersListAdapter.ViewHolder holder, final int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void refill(List<GitHubUser> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        ImageView userImage;
        GitHubUser user;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.username);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            user = mUsers.get(position);

            userName.setText(user.getLogin());

            Glide.with(mContext)
                    .load(user.getAvatar_url())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .crossFade()
                    .into(userImage);
        }

        @Override
        public void onClick(View v) {
            mListener.onUserClicked(user);
        }
    }

}
