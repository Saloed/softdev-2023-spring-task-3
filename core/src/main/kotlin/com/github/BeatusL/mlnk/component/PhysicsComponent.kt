package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateCfg
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2

class PhysicsComponent {         // should`ve called it PhysicComponent tho :(
    val impulse = vec2()
    lateinit var body: Body

    companion object {
        fun EntityCreateCfg.physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,  // Static 0|Kinematic 1|Dynamic 2
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

        class PhysicsComponentListener: ComponentListener<PhysicsComponent> {
            override fun onComponentAdded(entity: Entity, component: PhysicsComponent) {
                component.body.userData = entity
            }

            override fun onComponentRemoved(entity: Entity, component: PhysicsComponent) {
                val body = component.body
                component.body.world.destroyBody(body)
                body.userData = null
            }
        }
    }
}
