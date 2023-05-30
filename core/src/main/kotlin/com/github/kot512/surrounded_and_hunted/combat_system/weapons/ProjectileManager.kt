package com.github.kot512.surrounded_and_hunted.combat_system.weapons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.kot512.surrounded_and_hunted.combat_system.ammo.Projectile
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point

abstract class ProjectileManager(
    protected val screen: BaseLocationScreen,
    protected var directionAngle: Float, // передается угол, куда смотрит сущность
    protected val shootPoint: Point, // место вылета снарядов
) {
//    параметры снаряда
    abstract var projectileSpeed: Float
    abstract var projMaxDistance: Float // макс. расстояние для снаряда, после которого тот удалится
    abstract var projDamage: Float

//    параметры менеджера
    var launchedProjs: MutableList<Projectile> = mutableListOf() // список запущенных снарядов

    protected abstract val cooldown: Float // время перерыва между выстрелами
    private var currentCooldown: Float = 0f // время до следующего выстрела

    protected open val projInShot: Int = 1 // сколько снарялов вылетает за выстрел (всега нечетное)

//    если в выстреле более одного снаряда
    protected open val angleBetweenShots: Float = 0f // угол между одновременно вылетающими пулями
    protected open val cooldownBetweenProjs: Float = 0f // время между снарядами в одном выстреле
    private var currentCooldownBetweenProjs = 0f // время до след. снаряда в выстреле
    private var projsCounter: Int = 0 // кол-во запущенных снарядов за выстрел

    fun draw(batch: Batch) {
        launchedProjs = launchedProjs.filter { !it.disposable }.toMutableList()

        for (proj in launchedProjs)
             proj.draw(batch)
    }

    fun update(shouldShoot: Boolean, originX: Float, originY: Float, newAngle: Float) {
        updateShootPoint(originX, originY)
        updateDirectionAngle(newAngle)
        if (shouldShoot) shoot(Gdx.graphics.deltaTime)
    }

    private fun shoot(delta: Float) {
        if (currentCooldown < 1e-5) {
            when {
                projInShot > 1 ->
                    if (angleBetweenShots < 1e-5f) {// для выстрела очередью
                        while (projsCounter <= projInShot) {
                            if (currentCooldownBetweenProjs < 1e-5) {
                                launchProj()
                                projsCounter++
                                currentCooldownBetweenProjs = cooldownBetweenProjs
                            } else {
                                currentCooldownBetweenProjs -= delta
                            }
                        }
                        projsCounter = 0
                    } else {
                        directionAngle -= angleBetweenShots * projInShot / 2 // для выстрела дробью
                        repeat(projInShot - 1) {
                            launchProj()
                            directionAngle += angleBetweenShots
                        }
                        launchProj()
                        directionAngle -= angleBetweenShots * projInShot / 2
                    }

                else -> launchProj()
            }
            currentCooldown = cooldown
        }

        currentCooldown -= delta
    }

    private fun launchProj() { // потом используя массив отрисую через batch
        val projectile = createProj()
        launchedProjs += projectile
    }

    abstract fun createProj(): Projectile

    private fun updateShootPoint(newX: Float, newY: Float) {
        shootPoint.set(newX, newY)
    }
    private fun updateDirectionAngle(newAngle: Float) {
        directionAngle = newAngle
    }
}
