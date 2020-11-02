package com.naeem.fetchgithubuserslist_mvvm_room.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.naeem.fetchgithubuserslist_mvvm_room.base.BaseActivity
import com.naeem.fetchgithubuserslist_mvvm_room.databinding.ActivityMainBinding
import com.naeem.fetchgithubuserslist_mvvm_room.utils.NetworkUtils
import com.naeem.fetchgithubuserslist_mvvm_room.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naeem.fetchgithubuserslist_mvvm_room.R
import com.naeem.fetchgithubuserslist_mvvm_room.adapter.UserListAdapter
import com.naeem.fetchgithubuserslist_mvvm_room.model.State
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel
import com.naeem.fetchgithubuserslist_mvvm_room.ui.userdetail.UserDetailsActivity
import com.naeem.fetchgithubuserslist_mvvm_room.utils.getColorRes
import dev.shreyaspatil.foodium.utils.hide
import dev.shreyaspatil.foodium.utils.show

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    var user_list:ArrayList<UserRepoModel> = ArrayList<UserRepoModel>()
var since_id =0

    private var isLoading: Boolean = false
    var serchkey=""
    private val mAdapter = UserListAdapter(this::onItemClicked)
    override val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        // Initialize RecyclerView
        mViewBinding.postsRecyclerView.adapter = mAdapter



        mViewBinding.edtSearch.doOnTextChanged { text, start, before, count ->

            val data = text.toString()
            Log.e("search", data)
            if (data.length>0) {
                val query = "%${data.replace(' ', '%')}%"
                mViewModel.getsearchPosts(query).observe(this, Observer {user_list->
                    Log.e("search","innnnnn")

                        Log.e("search", user_list.size.toString())
                        mAdapter.submitList(user_list)



                })
                // mLocalPhoneContact_CustomAdapter!!.filterData(data.trim())
            }else{
              getPosts()
            }

        }
        val linearLayoutManager =  mViewBinding.postsRecyclerView.layoutManager as LinearLayoutManager?
        mViewBinding.postsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    if (linearLayoutManager!!.findLastCompletelyVisibleItemPosition() == user_list.size - 1) {
                        isLoading = true
                        Log.e("inscrol", "innscrool")
                        since_id = user_list.get(user_list.size - 1).id!!
                        Log.e("since_id", since_id.toString());
                        getPosts()

                    }
                }

            }
        })
        handleNetworkChanges()
    }

    override fun onResume() {
        super.onResume()
        initPosts()
        mAdapter.notifyDataSetChanged()
    }
    private fun initPosts() {
        user_list.clear()
        mViewModel.postsLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> {showLoading(true)
                    mViewBinding.pbLoading.visibility=View.VISIBLE}
                is State.Success -> {

                    if (state.data.isNotEmpty()) {
                        isLoading = false
                        user_list.addAll((state.data.toMutableList()))
                        mAdapter.submitList(user_list)
                        showLoading(false)
                        mViewBinding.pbLoading.visibility=View.GONE

                    }
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                    mViewBinding.pbLoading.visibility=View.GONE
                }
            }
        }



        // If State isn't `Success` then reload posts.
        if (mViewModel.postsLiveData.value !is State.Success) {
            getPosts()
        }
    }

    private fun getPosts() {
        Log.e("id",since_id.toString())
        mViewModel.getUsers(since_id)

    }

    private fun showLoading(isLoading: Boolean) {

    }

    /**
     * Observe network changes i.e. Internet Connectivity
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text =
                    getString(R.string.text_no_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.postsLiveData.value is State.Error || mAdapter.itemCount == 0) {
                    getPosts()
                }
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })

                }
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> true
        }
    }


    private fun onItemClicked(mUserRepoModel: UserRepoModel, imageView: ImageView) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(UserDetailsActivity._ID, mUserRepoModel.id)
        intent.putExtra(UserDetailsActivity._USERNAME, mUserRepoModel.username)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )

        startActivity(intent, options.toBundle())
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }
}