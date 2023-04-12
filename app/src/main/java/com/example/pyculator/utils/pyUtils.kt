package com.example.pyculator.utils

import com.example.pyculator.py
import com.example.pyculator.viewmodels.FavoriteVariable

fun eval(
    memoryList: MutableList<FavoriteVariable>,
    toEval: String,
): String {
    val rememberedConstants = StringBuilder()
    memoryList.forEach { rememberedConstants.append(it) }

    return py.getModule("main").callAttr(
        "main",
        rememberedConstants.toString(),
        toEval,
    ).toString()
}

fun compile(
    toCompile: String
): String {
    return py.getModule("main").callAttr(
        "compileCode",
        toCompile
    ).toString()
}