package com.naeem.fetchgithubuserslist_mvvm_room.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Data class for Database entity and Serialization.
 */
@Entity(tableName = "gituhuser_table")
data class UserRepoModel(

    @PrimaryKey
    var id: Int? = 0,
    var username: String? = null,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    var followers: Int? = 0,
    var following: Int? = 0,
    var note: String? = null,
    var imageUrl: String? = null
) {

}
