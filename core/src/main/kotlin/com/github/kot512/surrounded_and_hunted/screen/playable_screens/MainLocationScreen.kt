package com.github.kot512.surrounded_and_hunted.screen.playable_screens

import com.badlogic.gdx.graphics.Texture

class MainLocationScreen : BaseLocationScreen() {
    override val locationTexture: Texture = Texture("graphics/test_image/test_location.png")
    override val locationWidth: Float = locationTexture.width * 5f
    override val locationHeight: Float = locationTexture.height * 5f
}
