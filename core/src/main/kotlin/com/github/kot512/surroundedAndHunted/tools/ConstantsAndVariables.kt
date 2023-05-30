package com.github.kot512.surroundedAndHunted.tools

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.disposeSafely

class ConstantsAndVariables {
//    числовые константы
    val SCREEN_WIDTH = Gdx.graphics.width.toFloat()
    var SCREEN_HEIGHT = Gdx.graphics.height.toFloat()
    val L_JOYSTICK_POS = Point(SCREEN_WIDTH / 20, SCREEN_HEIGHT / 15)
    val R_JOYSTICK_POS = Point(SCREEN_WIDTH - SCREEN_WIDTH / 20 - 350f, SCREEN_HEIGHT / 15)

//    числовые переменные + считывание сохраненных данных
    val SAVE_DATA = Gdx.app.getPreferences("data")

    var RECORD_SCORE = 0
    var CURRENT_SCORE = 0
    var UPGRADE_POINTS = 0

    init {
        if (!SAVE_DATA.contains("record"))
            SAVE_DATA.apply {
                putInteger("record", 0)
                flush()
            }
        else RECORD_SCORE = SAVE_DATA.getInteger("record")

        if (!SAVE_DATA.contains("upgrade"))
            SAVE_DATA.apply {
                putInteger("upgrade", 0)
                flush()
            }
        else UPGRADE_POINTS = SAVE_DATA.getInteger("upgrade")
    }

    //    текстуры
    val TEXTURE_ATLAS: TextureAtlas = TextureAtlas("graphics/atlas/PPM_atlas.atlas")

    val JOYSTICK_KNOB_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("hud_joystick_knob")
    )
    val JOYSTICK_BASE_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("hud_joystick_base")
    )
    val PROJECTILE_BASE_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("projectile_main")
    )
    val PLAYER_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("entity_player")
    )
    val BALLER_ENEMY_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("entity_enemy_basic")
    )
    val TRIANGLE_ENEMY_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("entity_enemy_fast")
    )
    val GIANT_ENEMY_TXTR: TextureRegion = TextureRegion(
        TEXTURE_ATLAS.findRegion("entity_enemy_big")
    )

    fun dispose() {
        TEXTURE_ATLAS.disposeSafely()
    }
}
