package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.github.BeatusL.mlnk.component.CollisionComponent
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicsComponent::class, ImageComponent::class])
class PhysicsSystem(
    private val oWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physCmps: ComponentMapper<PhysicsComponent>,
    private val colCmps: ComponentMapper<CollisionComponent>
): ContactListener, IteratingSystem(interval = Fixed(1/60f)) { // fixed timestep`s good for optimization

    init {
        oWorld.setContactListener(this)
    }
    override fun onUpdate() {
        if (oWorld.autoClearForces) {
            oWorld.autoClearForces = false
            log.error { "autoClearForces must be false but was true instead" }
        }
        super.onUpdate()
        oWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        oWorld.step(deltaTime, 6, 2) // 6 & 2 just taken from docs. dunno what they`re for
    }

    override fun onTickEntity(entity: Entity) {
        val physCmp = physCmps[entity]
        val imageCmp = imageCmps[entity]

        if (!physCmp.impulse.isZero) {
            physCmp.body.applyLinearImpulse(physCmp.impulse, physCmp.body.worldCenter, true)
            physCmp.impulse.setZero()
        }

        val (x, y) = physCmp.body.position
        imageCmp.image.run {
            setPosition(x - width * 0.5f, y - height * 0.5f)
        }
    }


    override fun beginContact(contact: Contact) {
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val enabledA = if (contact.fixtureA.entity in colCmps)
            colCmps[contact.fixtureA.entity].borderCollisionEnabled else null
        val enabledB = if (contact.fixtureB.entity in colCmps)
            colCmps[contact.fixtureB.entity].borderCollisionEnabled else null
        contact.isEnabled =
            (isStatic(contact.fixtureA.body) && enabledB == true) ||
                (isStatic(contact.fixtureB.body) && enabledA == true)
    }

    private fun isStatic(body: Body) = body.type == BodyDef.BodyType.StaticBody
    private fun isDynamic(body: Body) = body.type == BodyDef.BodyType.DynamicBody
    private fun isKinematic(body: Body) = body.type == BodyDef.BodyType.KinematicBody
    private val Fixture.entity:Entity
        get() = this.body.userData as Entity


    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit


    companion object {
        private val log = logger<PhysicsSystem>()
    }
}

