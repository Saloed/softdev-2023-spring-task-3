package com.github.kot512.surrounded_and_hunted.screen.image_screens.buttons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.TEXTURE_ATLAS
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen

class NextScreenButton(
    width: Float,
    height: Float,
    position: Point,
    val nextScreen: KtxScreen,
    val currentScreen: KtxScreen
) : ButtonBase(
    null,
    TextureRegion(TEXTURE_ATLAS.findRegion("menu_begin_button")),
    TextureRegion(TEXTURE_ATLAS.findRegion("menu_begin_button_pressed")),
    width, height, position
) {
    override fun getButton() = TextButton(text, buttonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).addScreen(nextScreen)
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).setScreen<KtxScreen>()
                    currentScreen.dispose()
                }
            }
        )

        setPosition(position.x, position.y)
        this.width = widthParam
        this.height = heightParam
    }

}
