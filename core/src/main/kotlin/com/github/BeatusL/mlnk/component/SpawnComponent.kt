package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import ktx.math.vec2

data class SpawnConfig(
    val model: AnimationModel,
    val animationMode: AnimationPlaymode,
    val speed: Float,
    val physicScaling: Vector2 = vec2(1f, 1f),
    val physicOffset: Vector2 = vec2(0f, 0f),
    val bodyType: BodyType = BodyType.DynamicBody,
)


data class SpawnComponent (
    var type: String = "",
    var location: Vector2 = vec2()
)
