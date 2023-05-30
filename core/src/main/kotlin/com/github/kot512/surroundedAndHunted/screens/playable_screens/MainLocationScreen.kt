package com.github.kot512.surroundedAndHunted.screens.playable_screens

import com.badlogic.gdx.graphics.Texture

class MainLocationScreen : BaseLocationScreen() {
    override val locationTexture: Texture = Texture("graphics/locations/main_location.jpg")
    override val locationWidth: Float = locationTexture.width * 5f
    override val locationHeight: Float = locationTexture.height * 5f
}
