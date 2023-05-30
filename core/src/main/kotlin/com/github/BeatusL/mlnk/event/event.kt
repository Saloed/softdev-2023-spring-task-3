package com.github.BeatusL.mlnk.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.BeatusL.mlnk.system.EntityType

fun Stage.fire(event: Event) {
    this.root.fire(event)
}

data class MapChangeEvent(val map: TiledMap): Event()

data class ObjCreation(val type: String, val location: Vector2): Event()

data class EnemyDead(val type: EntityType, val location: Vector2): Event()

