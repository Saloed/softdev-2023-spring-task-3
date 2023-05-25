package com.example.be.utilits

import com.example.be.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var USER: User
lateinit var REF_STORAGE_ROOT: StorageReference


const val NODE_USERS = "Users"
const val FOLDER_PROFILE_IMAGE = "profileImage"

const val CHILD_ID = "id"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_PROFILEIMAGE = "profileImage"
const val CHILD_FOLDERS = "folders"

fun initFirebase() {
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    AUTH = FirebaseAuth.getInstance()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    USER = User()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}