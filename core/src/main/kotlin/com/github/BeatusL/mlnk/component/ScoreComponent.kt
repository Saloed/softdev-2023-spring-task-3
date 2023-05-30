package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier


class ScoreComponent {
    val text: String = "Score: "
    var score: Long = 0
    lateinit var label: Label
    lateinit var location: Vector2

    fun addpoint(multiplier: Int) {
        score += minScore * multiplier
    }

    companion object {
        const val minScore = 10


        class ScoreComponentListener(
            @Qualifier("UI") private val uiStage: Stage,
        ): ComponentListener<ScoreComponent> {

            override fun onComponentAdded(entity: Entity, component: ScoreComponent) {
                component.label.setPosition(component.location.x, component.location.y)
                uiStage.addActor(component.label)
            }

            override fun onComponentRemoved(entity: Entity, component: ScoreComponent) {
                uiStage.root.removeActor(component.label)
            }
        }
    }


}
