package com.naeem.fetchgithubuserslist_mvvm_room.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.naeem.fetchgithubuserslist_mvvm_room.data.local.dao.PostsDao
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserRepoModel


/**
 * Abstract Foodium database.
 * It provides DAO [PostsDao] by using method [getPostsDao].
 */
@Database(
    entities = [UserRepoModel::class],
    version = 1
)
abstract class GithubUserDatabase : RoomDatabase() {

    /**
     * @return [PostsDao] Foodium Posts Data Access Object.
     */
    abstract fun getPostsDao(): PostsDao

    companion object {
        const val DB_NAME = "gituserdb_database"

        @Volatile
        private var INSTANCE: GithubUserDatabase? = null

        fun getInstance(context: Context): GithubUserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
