package com.github.kot512.surrounded_and_hunted.controls

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class Joystick : Actor() {
    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor {
        return super.hit(x, y, touchable)
    }
}
