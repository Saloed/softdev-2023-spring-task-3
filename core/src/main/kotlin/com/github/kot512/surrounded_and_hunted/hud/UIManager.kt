package com.github.kot512.surrounded_and_hunted.hud

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.hud.controls.AimJoystick
import com.github.kot512.surrounded_and_hunted.hud.controls.MovementJoystick
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen

class UIManager(val screen: BaseLocationScreen) {
//    джойстики
    val movJoystick: MovementJoystick =
        MovementJoystick(CONST_AND_VAR.L_JOYSTICK_POS)
    val aimJoystick: AimJoystick =
        AimJoystick(CONST_AND_VAR.R_JOYSTICK_POS)

//    загрузка текстур
    private val hpBaseTexture: TextureRegion = TextureRegion(
    CONST_AND_VAR.TEXTURE_ATLAS.findRegion("hud_health_bar_base")
    )
    private val hpLineTexture: TextureRegion = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("hud_health_bar")
    )
    private val scoreBase: TextureRegion = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("hud_score_base")
    )

//    полоска хп
    val healthPointBase = object : Actor() {
        init {
            setPosition(50f, CONST_AND_VAR.SCREEN_HEIGHT - 100f)
            width = hpBaseTexture.texture.width / 10f
            height = hpBaseTexture.texture.height / 60f
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            batch.draw(hpBaseTexture, x, y, width, height)
        }
    }
    val healthPointLine = object : Actor() {
        private var widthCoeff: Float = 1f
        init {
            setPosition(healthPointBase.x + 10f, healthPointBase.y + 10f)
            width = hpLineTexture.texture.width / 10f - 15f
            height = hpLineTexture.texture.height / 70f - 10f
        }

        fun update() {
            widthCoeff =
                screen.player.health / screen.player.maxHealth
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            update()
            batch.draw(hpLineTexture, x, y, width * widthCoeff, height)
        }
    }

    //    окно со счетчиком
    private val score = object : Actor() {
        private val font = BitmapFont().apply {
            data.setScale(4f)
            color = Color.WHITE
        }

        init {
            setPosition(
                CONST_AND_VAR.SCREEN_WIDTH / 2 - scoreBase.texture.width / 40,
                CONST_AND_VAR.SCREEN_HEIGHT - scoreBase.texture.height / 20
            )
            width = scoreBase.texture.width / 20f
            height = scoreBase.texture.height / 20f
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            batch.draw(scoreBase, x, y, width, height)

            val score = CONST_AND_VAR.CURRENT_SCORE.toString()
            font.draw(
                batch, score,
                x + width / 2 - 15f * score.length,
                y + 125f,
            )
        }
    }

    fun setupActors() {
        screen.stage.addActor(movJoystick)
        screen.stage.addActor(aimJoystick)

        screen.stage.addActor(healthPointBase)
        screen.stage.addActor(healthPointLine)

        screen.stage.addActor(score)
    }
}
