package com.github.kot512.surrounded_and_hunted.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import tools.Point2D

class Player(texture: Texture,
             override val position: Point2D,
             override var speed: Float,
             override var radius: Float
) : Actor(texture) {
    override fun draw(batch: SpriteBatch) {
        TODO("Not yet implemented")
    }

    override fun update() {
        TODO("Not yet implemented")
    }


}
