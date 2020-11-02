package com.naeem.fetchgithubuserslist_mvvm_room.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naeem.fetchgithubuserslist_mvvm_room.model.State
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel
import com.naeem.fetchgithubuserslist_mvvm_room.data.repository.GithubUser_Repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel for [MainActivity]
 */
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUser_Repository) :
    ViewModel() {

    private val _postsLiveData = MutableLiveData<State<List<UserRepoModel>>>()

    val postsLiveData: LiveData<State<List<UserRepoModel>>>
        get() = _postsLiveData








    fun getUsers(since:Int) {
        viewModelScope.launch {
            githubUserRepository.getAllusers(since).collect {
                _postsLiveData.value = it
            }
        }
    }

    fun getsearchPosts(serchkey:String):LiveData<List<UserRepoModel>> {
        val _postsLiveData_search = MutableLiveData<List<UserRepoModel>>()
        viewModelScope.launch {
            githubUserRepository.getsearchPosts(serchkey).collect { it ->
                _postsLiveData_search.value = it
            }
        }
        return  _postsLiveData_search
    }
}
