package com.github.kot512.surrounded_and_hunted.screen.image_screens.buttons

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen

abstract class ButtonBase(
    val text: String? = null,
    private val texture: TextureRegion,
    private val pressedTexture: TextureRegion,
    val widthParam: Float,
    val heightParam: Float,
    val position: Point
) {
    open val buttonStyle: TextButtonStyle = TextButtonStyle().apply {
        font = BitmapFont()
        up = TextureRegionDrawable(texture)
        down = TextureRegionDrawable(pressedTexture)
        checked = TextureRegionDrawable(pressedTexture)
    }

    abstract fun getButton() : Button
}
