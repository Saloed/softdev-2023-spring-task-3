package com.example.be.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.be.utilits.AUTH
import com.example.be.databinding.ActivityRegistrationBinding
import com.example.be.utilits.CHILD_FULLNAME
import com.example.be.utilits.CHILD_ID
import com.example.be.utilits.CHILD_PROFILEIMAGE
import com.example.be.utilits.CHILD_USERNAME
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.initFirebase
import com.example.be.utilits.replaceActivity
import com.example.be.utilits.showToast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registration : AppCompatActivity() {
    lateinit var bindingClass: ActivityRegistrationBinding

    private lateinit var databaseReference: DatabaseReference

    /*цикл жизни*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass =
            ActivityRegistrationBinding.inflate(layoutInflater)/*хранит все элементы экрана*/
        setContentView(bindingClass.root)

        supportActionBar?.title = "BE"

        /*initFirebase()*/

        bindingClass.signIn.setOnClickListener {
            val email = bindingClass.etEmail.text.toString()
            val password = bindingClass.etPassword.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showToast("Заполните все поля")
            } else {
                AUTH.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val intent = Intent(this@Registration, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showToast("Email или пароль заполнены неверно")
                        }
                    }
            }
        }

        bindingClass.signUp.setOnClickListener {
            if (bindingClass.etConfirmPassword.visibility == View.GONE) {
                bindingClass.etName.visibility = View.VISIBLE
                bindingClass.etConfirmPassword.visibility = View.VISIBLE
                bindingClass.signIn.visibility = View.GONE

            } else {
                val userName = bindingClass.etName.text.toString()
                val userPassword = bindingClass.etPassword.text.toString()
                val userEmail = bindingClass.etEmail.text.toString()
                val userConfirmPassword = bindingClass.etConfirmPassword.text.toString()

                if (TextUtils.isEmpty(userName)) { /*почти то же самое, что и isNullOrEmpty, только для Android*/
                    showToast("Заполните поле \"Введите имя\"")
                }
                if (TextUtils.isEmpty(userEmail)) {
                    showToast("Заполните поле \"Введите Email\"")

                }
                if (TextUtils.isEmpty(userPassword)) {
                    showToast("Заполните поле \"Введите пароль\"")
                }
                if (TextUtils.isEmpty(userConfirmPassword)) {
                    showToast("Заполните поле \"Введите пароль еще раз\"")
                }
                if (userPassword != userConfirmPassword) {
                    showToast("Пароли не совпадают")
                }
                registerUser(userEmail, userPassword, userName)
            }
        }
    }


    private fun registerUser(email: String, password: String, userName: String) {
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) { /*если успешно выполнено, то добавляем юзера в базу */
                    /*возвращает фактический идентификатор (ID) пользователя вызывающего процесса*/

                    /*databaseReference = FirebaseDatabase.getInstance().getReference("Users/$CURRENT_UID").child("Data")*//*создаем дерево*/
                    val uid = AUTH.currentUser?.uid.toString()
                    val hashMap: MutableMap<String, Any> = mutableMapOf()
                    hashMap[CHILD_ID] = uid
                    hashMap[CHILD_USERNAME] = uid
                    hashMap[CHILD_PROFILEIMAGE] = ""
                    hashMap[CHILD_FULLNAME] = userName

                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(hashMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                /*открываем MainActivity*/
                                showToast("Добро пожаловать")
                                replaceActivity(MainActivity())
                            }
                        }
                } else showToast("error")

            }
    }


}