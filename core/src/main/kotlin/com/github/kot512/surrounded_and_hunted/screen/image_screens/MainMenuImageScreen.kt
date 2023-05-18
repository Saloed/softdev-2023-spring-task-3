package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.TEXTURE_ATLAS
import com.github.kot512.surrounded_and_hunted.screen.image_screens.buttons.NextScreenButton
import com.github.kot512.surrounded_and_hunted.screen.image_screens.buttons.StartButtonStyle
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.TestLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen


class MainMenuImageScreen : BaseImageScreen(
    Texture("graphics/screen_backgrounds/main_menu.png"),
    1280f, 720f //TODO(пошаманить с размерами)
) {
//    private val startButton: TextButton = TextButton(null, StartButtonStyle).apply {
//        addListener(
//            object : ClickListener() {
//                override fun clicked(event: InputEvent, x: Float, y: Float) {
//                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).addScreen(TestLocationScreen())
//                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).setScreen<TestLocationScreen>()
//                    this@MainMenuImageScreen.dispose()
//                }
//            }
//        )
//        setPosition(200f, 200f)
//    }
    private val startButton = //TODO(разбраться хули не работает)
        NextScreenButton(
            400f, 100f, Point(0f, 0f),
            TestLocationScreen(), this
        ).getButton()

    override fun show() {
        stage.addActor(startButton)
    }
}
