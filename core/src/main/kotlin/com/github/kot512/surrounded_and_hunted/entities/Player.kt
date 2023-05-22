package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.PLAYER_HP
import com.github.kot512.surrounded_and_hunted.hud.controls.AimJoystick
import com.github.kot512.surrounded_and_hunted.hud.controls.MovementJoystick
import com.github.kot512.surrounded_and_hunted.combat_system.weapons.ProjectileManager
import com.github.kot512.surrounded_and_hunted.combat_system.weapons.SemiAutomaticBalls
import com.github.kot512.surrounded_and_hunted.screen.image_screens.DeathImageScreen
import com.github.kot512.surrounded_and_hunted.screen.image_screens.MainMenuImageScreen
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.MainLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Player(
    screen: BaseLocationScreen,
    characterTexture: TextureRegion,
    private val movementController: MovementJoystick,
    private val aimController: AimJoystick,
) : BaseEntity(
    screen, characterTexture,
    Point(100f, 100f)
) {
    var maxHealth = PLAYER_HP
    override var health = maxHealth
    override var movementSpeed = 500f
    override var damage = 10f

    private val combatManager: ProjectileManager =
        SemiAutomaticBalls(screen, viewAngle, Point(originBasedX, originBasedY))

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
        (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
            addScreen(DeathImageScreen())
            setScreen<DeathImageScreen>()
            removeScreen<MainLocationScreen>()
        }
        screen.dispose()
    }
}
