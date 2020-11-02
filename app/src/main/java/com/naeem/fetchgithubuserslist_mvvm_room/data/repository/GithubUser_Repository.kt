package com.naeem.fetchgithubuserslist_mvvm_room.data.repository

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.naeem.fetchgithubuserslist_mvvm_room.data.local.dao.PostsDao
import com.naeem.fetchgithubuserslist_mvvm_room.data.remote.api.RetofitService
import com.naeem.fetchgithubuserslist_mvvm_room.data.repository.NetworkBoundRepository
import com.naeem.fetchgithubuserslist_mvvm_room.model.State
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserDetailModel_Api_Response
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability. This is Single source of data.
 */
@ExperimentalCoroutinesApi
@Singleton
class GithubUser_Repository @Inject constructor(
    private val postsDao: PostsDao,
    private val retofitService: RetofitService
) {

    /**
     * Fetched the User from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    fun getAllusers(since:Int): Flow<State<List<UserRepoModel>>> {
        return object : NetworkBoundRepository<List<UserRepoModel>, JsonArray>() {
            override suspend fun saveRemoteData(response: JsonArray) {
                val mUserRepoModellist = ArrayList<UserRepoModel>()
                val gson = GsonBuilder().create()
                Log.e("response_api",response.toString())
                if (response != null) {

                    for (i in 0..response.size() - 1) {
                        val jsonobj = JsonParser().parse(response.get(i).toString()).asJsonObject
                        val mUserRepoModel=UserRepoModel();
                        if (jsonobj.has("id")){
                            mUserRepoModel.id=jsonobj.get("id").asInt
                        }
                        if (jsonobj.has("login")){
                            mUserRepoModel.username=jsonobj.get("login").asString
                        }
                        if (jsonobj.has("avatar_url")){
                            mUserRepoModel.imageUrl=jsonobj.get("avatar_url").asString
                        }
                        mUserRepoModellist.add(mUserRepoModel)
                    }
                    postsDao.insertPosts(mUserRepoModellist)
                }

               /* if ()


                postsDao.insertPosts(response)*/
            }


            override fun fetchFromLocal(): Flow<List<UserRepoModel>> = postsDao.getAllPosts()
           // override fun fetchFromLocal(): Flow<List<UserRepoModel>> = postsDao.userByName(searchkey)

            override suspend fun fetchFromRemote(): Response<JsonArray> = retofitService.getUsers(since)

        }.asFlow()
    }


    /**
     search by name
     */

    fun getsearchPosts(searchkey:String): Flow<List<UserRepoModel>> {

        return postsDao.userByName(searchkey)
    }

    /**
     * Retrieves a post with specified [Userid].
     * @param Userid Unique id of a [UserRepoModel].
     * @return [UserRepoModel] data fetched from the database.
     */



    /**
     * Fetched the User detail from network and Update it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */

    fun getuserDetail(usernamed:String,userid: Int): Flow<State<UserRepoModel>> {
        return object : NetworkBoundRepository<UserRepoModel, UserDetailModel_Api_Response>() {
            override suspend fun saveRemoteData(response: UserDetailModel_Api_Response) {

                val gson = GsonBuilder().create()
                Log.e("response_api",response.toString())
                if (response != null) {

                    postsDao.update( response.name, response.company.toString(),
                        response.blog.toString(), response.following!!,
                        response.followers!!,userid)
                }


            }


            override fun fetchFromLocal(): Flow<UserRepoModel> = postsDao.getuserById(userid)

            override suspend fun fetchFromRemote(): Response<UserDetailModel_Api_Response> = retofitService.getuserdetail(usernamed)

        }.asFlow()
    }




    @WorkerThread
    suspend fun updatenote(usernote:String, userid: Int) = postsDao.update_usernote(usernote!!,userid)
}
