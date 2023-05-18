package com.github.kot512.surrounded_and_hunted.screen.image_screens.buttons

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.screen.image_screens.BaseImageScreen

object StartButtonStyle : TextButton.TextButtonStyle() {
    private val startButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_begin_button")
    )
    private val startButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_begin_button_pressed")
    )

    init {
        font = BitmapFont()
        up = TextureRegionDrawable(startButtonTexture)
        down = TextureRegionDrawable(startButtonPressedTexture)
        checked = TextureRegionDrawable(startButtonPressedTexture)
    }
}
