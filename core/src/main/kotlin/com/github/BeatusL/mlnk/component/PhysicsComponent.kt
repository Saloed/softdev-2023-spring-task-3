package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.EntityCreateCfg
import ktx.box2d.BodyDefinition
import ktx.box2d.body

class PhysicsComponent {
    lateinit var body: Body

    companion object {
        fun EntityCreateCfg.physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(PhysicsComponent, Float, Float) -> Unit
        ) :PhysicsComponent {
            val x = image.x
            val y = image.y
            val w = image.width
            val h = image.height

            return add<PhysicsComponent> {
                body = world.body(bodyType) {
                    position.set(x + w * 0.5f, y + h * 0.5f)     //positioning from the center
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(this@add, w, h)
                }
            }
        }
    }
}
