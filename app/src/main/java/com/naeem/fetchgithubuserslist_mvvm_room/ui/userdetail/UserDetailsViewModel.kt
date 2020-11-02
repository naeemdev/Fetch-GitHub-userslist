package com.naeem.fetchgithubuserslist_mvvm_room.ui.userdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.naeem.fetchgithubuserslist_mvvm_room.data.repository.GithubUser_Repository
import com.naeem.fetchgithubuserslist_mvvm_room.model.State
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel for [UserDetailsActivity]
 */
@ExperimentalCoroutinesApi
class UserDetailsViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUser_Repository) :
    ViewModel() {


    private val _userdetailsLiveData = MutableLiveData<State<UserRepoModel>>()

    val userdetailLiveData: LiveData<State<UserRepoModel>>
        get() = _userdetailsLiveData



     fun updateusernote(note:String, id: Int) {
         viewModelScope.launch {
             githubUserRepository.updatenote(note,id)
         }
     }




/*
    fun getPost(id: Int) = githubUserRepository.getPostById(id).asLiveData()
*/




    fun getuserDetail(username:String,id: Int) {
        viewModelScope.launch {
            githubUserRepository.getuserDetail(username,id).collect {
                _userdetailsLiveData.value = it
            }
        }
    }




}
