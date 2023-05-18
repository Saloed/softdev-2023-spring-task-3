package com.github.kot512.surrounded_and_hunted.screen.playable_screens

import com.badlogic.gdx.graphics.Texture

class TestLocationScreen : BaseLocationScreen() {
    override val locationTexture: Texture = Texture("graphics/test_image/test_location.png")
    override val locationWidth: Float = 1500f
    override val locationHeight: Float = 1300f
}
