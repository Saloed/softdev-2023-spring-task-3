package com.github.kot512.surrounded_and_hunted.objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class GraphicsObject(texture: Texture) {

    abstract fun draw(batch: SpriteBatch)
    abstract fun update()
}
