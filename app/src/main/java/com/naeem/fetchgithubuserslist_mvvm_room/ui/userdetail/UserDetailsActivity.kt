package com.naeem.fetchgithubuserslist_mvvm_room.ui.userdetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import coil.api.load
import com.naeem.fetchgithubuserslist_mvvm_room.R
import com.naeem.fetchgithubuserslist_mvvm_room.base.BaseActivity
import com.naeem.fetchgithubuserslist_mvvm_room.databinding.ActivityPostDetailsBinding
import com.naeem.fetchgithubuserslist_mvvm_room.model.State
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel
import com.naeem.fetchgithubuserslist_mvvm_room.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserDetailsActivity : BaseActivity<UserDetailsViewModel, ActivityPostDetailsBinding>() {

    override val mViewModel: UserDetailsViewModel by viewModels()

    private lateinit var post: UserRepoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.extras?.getInt(_ID)
            ?: throw IllegalArgumentException("`postId` must be non-null")

        val username = intent.extras?.getString(_USERNAME)
            ?: throw IllegalArgumentException("`Username` must be non-null")

        initPosts(username,userId)
        mViewBinding.postContent.btnNote.setOnClickListener(View.OnClickListener {
            if (mViewBinding.postContent.edtNote.text.toString().isNullOrEmpty()){
                showToast(getString(R.string.pleaseenternote))
            }else{
            mViewModel.updateusernote(
                note = mViewBinding.postContent.edtNote.text.toString(),
                id = userId
            )
               showToast(getString(R.string.noteadded))
            }
        })
    }





    private fun initPosts(username:String,userId: Int) {

        mViewModel.userdetailLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    mViewBinding.postContent.pbLoading.visibility=View.VISIBLE}
                is State.Success -> {

                    post=state.data
                    mViewBinding.postContent.item=state.data
                    mViewBinding.toolbar.setTitle(post.username)
                    mViewBinding.postContent.imageView.load(post.imageUrl)

                    mViewBinding.postContent.pbLoading.visibility=View.GONE

                }
                is State.Error -> {
                    showToast(state.message)

                    mViewBinding.postContent.pbLoading.visibility=View.GONE
                }
            }
        }



        // If State isn't `Success` then reload posts.
        if (mViewModel.userdetailLiveData.value !is State.Success) {
            getuserdetail(username,userId)
        }
    }



    private fun getuserdetail(username:String,userId: Int) {



        mViewModel.getuserDetail(username,userId)

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }


        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val _ID = "postId"
        const val _USERNAME = "username"
    }

    override fun getViewBinding(): ActivityPostDetailsBinding = ActivityPostDetailsBinding.inflate(layoutInflater)
}