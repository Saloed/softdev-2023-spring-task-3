package com.github.kot512.surrounded_and_hunted.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CURRENT_SCORE
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.hud.controls.AimJoystick
import com.github.kot512.surrounded_and_hunted.hud.controls.MovementJoystick
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen

class UIManager(val screen: BaseLocationScreen) {
    //    джойстики
    private val movJoystick: MovementJoystick =
        MovementJoystick(SurroundedAndHunted.L_JOYSTICK_POS)
    private val aimJoystick: AimJoystick =
        AimJoystick(SurroundedAndHunted.R_JOYSTICK_POS)

    //    полоска хп
    private val hpPosition: Point = Point(0f, 0f)
    private val hpBaseTexture: TextureRegion = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("hud_health_bar_base")
    )
    private val hpLineTexture: TextureRegion = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("hud_health_bar")
    )
    private var widthCoeff: Float = 1f

    //    окно со счетчиком
    private val scorePosition: Point = Point(SCREEN_WIDTH - 300f, SCREEN_HEIGHT - 300f)
    private val scoreBase: TextureRegion = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("hud_score_base")
    )
    private val font = BitmapFont()

//

    fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)

//        отрисовка полоски хп
        batch.draw(
            hpBaseTexture,
            hpPosition.x,
            hpPosition.y,
            hpBaseTexture.texture.width.toFloat(),
            hpBaseTexture.texture.height.toFloat()
        )
        batch.draw(
            hpLineTexture,
            hpPosition.x + 20f,
            hpPosition.y + 20f,
            hpLineTexture.texture.width * widthCoeff,
            hpLineTexture.texture.height.toFloat()
        )

//        отрисовка счетчика очков
        batch.draw(
            scoreBase,
            scorePosition.x,
            scorePosition.y
        )
        font.draw(
            batch, CURRENT_SCORE.toString(),
            scorePosition.x + scoreBase.texture.width / 2,
            scorePosition.y + scoreBase.texture.height / 2
        )
    }

    private fun update(deltaTime: Float) {
//        обновление длины полоски хп
        widthCoeff = screen.player.maxHealth / screen.player.health
    }

    fun setupActors() {
        screen.stage.addActor(movJoystick)
        screen.stage.addActor(aimJoystick)
    }
}
