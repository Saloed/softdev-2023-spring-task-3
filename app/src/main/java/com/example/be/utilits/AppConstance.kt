package com.example.be.utilits

import com.example.be.activity.MainActivity

lateinit var APP_ACTIVITY: MainActivity
var COUNT_SNAPSHOT = 0
var COUNT_MESSAGE = 0
var COUNT_SNAPSHOT_PLUS = 0

private val http = "https://firebasestorage.googleapis.com/v0/b/mybe-d2532.appspot.com/o/lectures%2F"
private val back = "?alt=media"

val URL_FIRST_LECTURE = http + "Upravlenie_soboy.pdf" + back
val URL_SECOND_LECTURE = http + "Samoopredelenie.pdf" + back
val URL_THIRD_LECTURE = http + "Lichnye_granitsy.pdf" + back
val URL_FOURTH_LECTURE = http + "Emotsionalnoe_vygoranie.pdf" + back

const val identifier_first_lection:Long = 100
const val identifier_second_lection:Long = 101
const val identifier_third_lection:Long = 102
const val identifier_forth_lection:Long = 103