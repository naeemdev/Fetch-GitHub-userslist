package com.naeem.fetchgithubuserslist_mvvm_room.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.naeem.fetchgithubuserslist_mvvm_room.R
import com.naeem.fetchgithubuserslist_mvvm_room.databinding.ItemPostBinding
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel


class UserViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: UserRepoModel, onItemClicked: (UserRepoModel, ImageView) -> Unit) {
        binding.postTitle.text = post.username
        binding.postAuthor.text = post.username
        binding.imageView.load(post.imageUrl) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }
        if (post.note.isNullOrEmpty()){
            binding.imgNote.visibility=View.GONE
        }else{
            binding.imgNote.visibility=View.VISIBLE
        }
        binding.root.setOnClickListener {
            onItemClicked(post, binding.imageView)
        }
    }
}
