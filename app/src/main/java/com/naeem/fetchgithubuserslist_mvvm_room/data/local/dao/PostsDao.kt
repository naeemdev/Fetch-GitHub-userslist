package com.naeem.fetchgithubuserslist_mvvm_room.data.local.dao


import androidx.room.*
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel
import kotlinx.coroutines.flow.Flow


@Dao
interface PostsDao {

    /**
     * Inserts [UserRepoModel] into the  table.
     * Duplicate values are replaced in the table.
     * @param UserRepoModel Posts
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPosts(posts: List<UserRepoModel>)



    @Query("UPDATE gituhuser_table SET name=:name,company=:company,blog=:blog,following=:following,followers=:followers WHERE id = :id")
    suspend  fun update(
        name: String?,
        company: String,
        blog: String,
        following: Int,
        followers: Int,
        id: Int
    )



    @Query("UPDATE gituhuser_table SET note=:note WHERE id = :id")
    suspend fun update_usernote(note: String, id: Int)


    /**
     * Deletes all the posts from the [UserRepoModel] table.
     */
    @Query("DELETE FROM gituhuser_table")
    suspend fun deleteAllPosts()

    /**
     * Fetches the User from the table whose id .
     * @param Userid Unique ID of [UserRepoModel]
     * @return [Flow] of [UserRepoModel] from database table.
     */
    @Query("SELECT * FROM  gituhuser_table  WHERE ID = :userid")
    fun getuserById(userid: Int): Flow<UserRepoModel>

    /**
     * Fetches all the posts from the table.
     * @return [Flow]
     */
    @Query("SELECT * FROM  gituhuser_table ")
    fun getAllPosts(): Flow<List<UserRepoModel>>

//"SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE  ":queryString) ORDER BY  name ASC")

    @Query("SELECT * FROM  gituhuser_table  WHERE username Like :queryString ORDER BY  username ASC ")
    fun userByName(queryString: String): Flow<List<UserRepoModel>>
}
