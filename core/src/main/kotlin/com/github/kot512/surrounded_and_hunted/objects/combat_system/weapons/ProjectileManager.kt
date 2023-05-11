package com.github.kot512.surrounded_and_hunted.objects.combat_system.weapons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.kot512.surrounded_and_hunted.objects.combat_system.ammo.Projectile
import com.github.kot512.surrounded_and_hunted.tools.Point

// TODO(Перекинуть создание шаров на менеджер, перекинуть параметры сюда)
abstract class ProjectileManager(
    protected var directionAngle: Float, // передается угол, куда смотрит сущность
    protected val shootPoint: Point, // место вылета снарядов
) {
//    параметры снаряда
    abstract var projectileSpeed: Float
    abstract var projMaxDistance: Float // макс. расстояние для снаряда, после которого тот удалится
    abstract var projDamage: Float

//    параметры менеджера
    private var readyToShoot: Boolean = true // завершен ли предыдущий выстрел
    private val launchedProjs: MutableList<Projectile> = mutableListOf() // список запущенных снарядов

    protected abstract val cooldown: Float // время перерыва между выстрелами
    private var currentCooldown: Float = 0f // время до следующего выстрела

    protected open val projInShot: Int = 1 // сколько снарялов вылетает за выстрел (всега нечетное)

//    если в выстреле более одного снаряда
    protected open val angleBetweenShots: Float = 0f // угол между одновременно вылетающими пулями
    protected open val cooldownBetweenProjs: Float = 0f // время между снарядами в одном выстреле
    private var currentCooldownBetweenProjs = 0f // время до след. снаряда в выстреле
    private var projsCounter: Int = 0 // кол-во запущенных снарядов за выстрел
    // (посл. 2 параметра используются, если за один выстрел вылетает несколько снарядов)

    abstract fun createProj(): Projectile

    fun draw(
        batch: Batch, //shouldShoot: Boolean,
//        originX: Float, originY: Float, newAngle: Float
    ) {
//        update(shouldShoot, originX, originY, newAngle)
        launchedProjs.forEach { proj ->
            proj.draw(batch)
        }
    }

    fun update(shouldShoot: Boolean, originX: Float, originY: Float, newAngle: Float) {
        if (shouldShoot /*&& readyToShoot*/) shoot(Gdx.graphics.deltaTime)
        changeShootPoint(originX, originY)
        changeDirectionAngle(newAngle)
    }

    fun shoot(delta: Float) {
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
//        Thread.sleep((timeUntilNextShot * 1000).toLong())
            currentCooldown = cooldown
        }

        currentCooldown -= delta
//        readyToShoot = false
//
//        readyToShoot = true
    }

    private fun launchProj() { // потом используя массив отрисую через batch
        val projectile = createProj()
        launchedProjs.add(projectile)
    }

//    fun disposeProjAtIndex(index: Int) {
//        launchedProjs.removeAt(index)
//    }
    fun disposeProj(projectile: Projectile) {
        launchedProjs.remove(projectile)
    }

    private fun changeShootPoint(newX: Float, newY: Float) {
        shootPoint.set(newX, newY)
    }
    private fun changeDirectionAngle(newAngle: Float) {
        directionAngle = newAngle
    }
}
