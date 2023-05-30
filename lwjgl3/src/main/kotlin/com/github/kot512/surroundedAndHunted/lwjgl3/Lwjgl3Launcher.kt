@file:JvmName("Lwjgl3Launcher")

package com.github.kot512.surroundedAndHunted.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.kot512.surroundedAndHunted.SurroundedAndHunted

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(SurroundedAndHunted(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Surrounded_and_Hunted")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
