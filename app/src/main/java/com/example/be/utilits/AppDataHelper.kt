package com.example.be.utilits

import com.example.be.models.Folder
import com.example.be.models.Message
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
lateinit var MESSAGE: Message
lateinit var FOLDER: Folder
lateinit var REF_STORAGE_ROOT: StorageReference


const val NODE_USERS = "Users"
const val FOLDER_PROFILE_IMAGE = "profileImage"

const val CHILD_ID = "id"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_PROFILEIMAGE = "profileImage"
const val CHILD_FOLDERS = "folders"

const val TEXT_MESSAGE = "text"
const val TYPE_MESSAGE = "type"
const val ID_MESSAGE = "id"
const val TITLE_MESSAGE = "title"
const val TYPE_TEXT = "text"
const val TYPE_VOICE = "voice"






fun initFirebase() {
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    AUTH = FirebaseAuth.getInstance()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    USER = User()
    FOLDER = Folder()
    MESSAGE = Message()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}