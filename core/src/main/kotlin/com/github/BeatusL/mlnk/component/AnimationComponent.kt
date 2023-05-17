package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

enum class AnimationModel {
    player, S, M, B, RP, BP, exps, Default;

    val atlasKey: String = this.toString()
}

/*         На будущее
enum class AnimationType {
    player, S, M, B, RP, BP, exps;

    val atlasKey: String = this.toString()
}
*/

enum class AnimationPlaymode {
    Loop, Normal, Reversed;

    val key: String = this.toString()
}

class AnimationComponent(
    var animationModel: AnimationModel = AnimationModel.Default,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation: Animation<TextureRegionDrawable>


    var nextAnimation: String = ""

    fun nextAnimation(model: AnimationModel, mode: AnimationPlaymode) {
        when (mode.key) {
            "Loop" -> playMode = Animation.PlayMode.LOOP
            "Normal" -> playMode = Animation.PlayMode.NORMAL
            "Reversed" -> playMode = Animation.PlayMode.REVERSED
        }
        this.animationModel = model
        nextAnimation = "${model.atlasKey}/${model.atlasKey}"
    }

    companion object {
        val no_animation = ""
    }
}
