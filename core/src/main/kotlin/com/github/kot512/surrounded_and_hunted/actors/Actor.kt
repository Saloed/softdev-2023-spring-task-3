package com.github.kot512.surrounded_and_hunted.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.objects.GraphicsObject
import tools.Point2D

abstract class Actor(
    texture: Texture): GraphicsObject(texture) {

    abstract val position: Point2D
    abstract var speed: Float
    abstract var radius: Float



}
