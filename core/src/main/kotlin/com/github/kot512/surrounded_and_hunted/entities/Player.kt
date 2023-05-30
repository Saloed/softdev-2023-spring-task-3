package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.hud.controls.AimJoystick
import com.github.kot512.surrounded_and_hunted.hud.controls.MovementJoystick
import com.github.kot512.surrounded_and_hunted.combat.weapons.ProjectileManager
import com.github.kot512.surrounded_and_hunted.combat.weapons.SemiAutomaticBalls
import com.github.kot512.surrounded_and_hunted.screens.image_screens.DeathImageScreen
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.MainLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen
import kotlin.math.pow

class Player(
    screen: BaseLocationScreen,
    characterTexture: TextureRegion,
    private val movementController: MovementJoystick,
    private val aimController: AimJoystick,
) : BaseEntity(
    screen, characterTexture,
    Point(CONST_AND_VAR.SCREEN_WIDTH, CONST_AND_VAR.SCREEN_HEIGHT),
    160f, 160f
) {
//    обновление уровней прокачки
    var healthLevel = 1
    var gunLevel = 1
    var speedLevel = 1
    init {
        if (CONST_AND_VAR.SAVE_DATA.contains("hp_lvl"))
            healthLevel = CONST_AND_VAR.SAVE_DATA.getInteger("hp_lvl")
        else CONST_AND_VAR.SAVE_DATA.apply {
            putInteger("hp_lvl", 1)
            flush()
        }

        if (CONST_AND_VAR.SAVE_DATA.contains("speed_lvl"))
            speedLevel = CONST_AND_VAR.SAVE_DATA.getInteger("speed_lvl")
        else CONST_AND_VAR.SAVE_DATA.apply {
            putInteger("speed_lvl", 1)
            flush()
        }

        if (CONST_AND_VAR.SAVE_DATA.contains("gun_lvl"))
            gunLevel = CONST_AND_VAR.SAVE_DATA.getInteger("gun_lvl")
        else CONST_AND_VAR.SAVE_DATA.apply {
            putInteger("gun_lvl", 1)
            flush()
        }
    }

    var maxHealth = 100f * 1.2f.pow(healthLevel)
    override var health = maxHealth
    override var damage = 10f
    override var movementSpeed = 400f * 1.2f.pow(speedLevel)

    private val combatManager: ProjectileManager =
        SemiAutomaticBalls(this, screen, viewAngle, Point(originBasedX, originBasedY))

    override fun draw(batch: Batch) {
        super.draw(batch)
        combatManager.draw(batch)
    }

    override fun update(delta: Float) {
//        обработка действий игрока
        inputCheck(delta)
        combatManager.update(
            aimController.isTouched,
            originBasedX, originBasedY,
            viewAngle
        )

        if (health <= 0) die()
    }

    override fun move(delta: Float) {
        setPosition(x, y)
        collisionBounds.set(originBasedX, originBasedY)
    }

    override fun changeViewDirection(delta: Float) {
        rotation = viewAngle
    }

    private fun inputCheck(delta: Float) {
//       обработка левого джойстика
        val powerOutput = movementController.knobDeviation
        velocity.x = movementSpeed * powerOutput.x
        velocity.y = movementSpeed * powerOutput.y

        if (velocity.x > movementSpeed) velocity.x = movementSpeed
        if (velocity.x < -movementSpeed) velocity.x = -movementSpeed
        if (velocity.y > movementSpeed) velocity.y = movementSpeed
        if (velocity.y < -movementSpeed) velocity.y = -movementSpeed

//        проверка, не зайдет ли игрок за пределы карты
        if (allowedToMoveX(x + velocity.x * delta))
            x += velocity.x * delta
        if (allowedToMoveY(y + velocity.y * delta))
            y += velocity.y * delta

        move(delta)



//        обработка правого джойстика
        viewAngle = aimController.degreeAngle
        changeViewDirection(delta)
    }

    override fun receiveDamage(damage: Float) {
        health -= damage
    }

    override fun die() {
        if (CONST_AND_VAR.CURRENT_SCORE > CONST_AND_VAR.RECORD_SCORE)
            CONST_AND_VAR.RECORD_SCORE = CONST_AND_VAR.CURRENT_SCORE
        CONST_AND_VAR.UPGRADE_POINTS += CONST_AND_VAR.CURRENT_SCORE / 10
        CONST_AND_VAR.SAVE_DATA.apply {
            putInteger("record", CONST_AND_VAR.RECORD_SCORE)
            putInteger("upgrade", CONST_AND_VAR.UPGRADE_POINTS)
            flush()
        }

        (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
            addScreen(DeathImageScreen())
            setScreen<DeathImageScreen>()
            removeScreen<MainLocationScreen>()
        }
        screen.dispose()
    }
}
