package com.naeem.fetchgithubuserslist_mvvm_room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.naeem.fetchgithubuserslist_mvvm_room.databinding.ItemPostBinding
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel


class UserListAdapter(
    private val onItemClicked: (UserRepoModel, ImageView) -> Unit
) : ListAdapter<UserRepoModel, UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserRepoModel>() {
            override fun areItemsTheSame(oldItem: UserRepoModel, newItem: UserRepoModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UserRepoModel, newItem: UserRepoModel): Boolean =
                oldItem == newItem
        }
    }
}
