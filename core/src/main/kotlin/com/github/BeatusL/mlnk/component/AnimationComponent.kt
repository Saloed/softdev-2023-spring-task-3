package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


enum class AnimationType {
    player, S, M, B, RP, BP;

    val atlasKey: String = this.toString()
}

class AnimationComponent(
    var atlasKey: String = "",
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation: Animation<TextureRegionDrawable>

    var nextAnimation: String = ""

    fun nextAnimation(atlasKey: String, type: AnimationType) {
        this.atlasKey = atlasKey
        nextAnimation = "$atlasKey/${type.atlasKey}"
    }

    companion object {
        val no_animation = ""
    }
}
