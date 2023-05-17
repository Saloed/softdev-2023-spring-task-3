package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

data class SpawnConfig(
    val model: AnimationModel, val animationMode: AnimationPlaymode, val speed: Float
)


data class SpawnComponent (
    var type: String = "",
    var location: Vector2 = vec2()
)
