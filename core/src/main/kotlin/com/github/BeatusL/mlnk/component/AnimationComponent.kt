package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

enum class AnimationModel {
    player, S, M, B, RP, BP, exps, Box, Default;

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
}

class AnimationComponent(
    var animationModel: AnimationModel = AnimationModel.Default,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation: Animation<TextureRegionDrawable>


    var nextAnimation: String = noAnimation

    fun nextAnimation(model: AnimationModel, mode: AnimationPlaymode) {
        playMode = when (mode) {
            AnimationPlaymode.Loop -> Animation.PlayMode.LOOP
            AnimationPlaymode.Normal -> Animation.PlayMode.NORMAL
            AnimationPlaymode.Reversed -> Animation.PlayMode.REVERSED
        }
        this.animationModel = model
        nextAnimation = "${model.atlasKey}/${model.atlasKey}"
    }

    companion object {
        const val noAnimation = ""
    }
}
