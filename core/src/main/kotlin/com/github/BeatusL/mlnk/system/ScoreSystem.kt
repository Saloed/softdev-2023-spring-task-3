package com.github.BeatusL.mlnk.system


import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.BeatusL.mlnk.component.ScoreComponent
import com.github.BeatusL.mlnk.event.EnemyDead
import com.github.BeatusL.mlnk.game
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.log.logger


@AllOf([ScoreComponent::class])
class ScoreSystem(
    private val scoreCmps: ComponentMapper<ScoreComponent>,
): EventListener, IteratingSystem() {


    override fun handle(event: Event?): Boolean {
        if (event is EnemyDead) {
            val scoreCmp = scoreCmps[Entity(scoreID)]
            val multiplier = when (event.type) {
                EntityType.B -> 3
                EntityType.M -> 2
                EntityType.S -> 1
                else -> gdxError("${event.type} is not an enemy")
            }

            scoreCmp.addpoint(multiplier)
            log.debug { "Score is ${scoreCmp.score}" }
            game.setScore(scoreCmp.score)
            return true
        }
        return false
    }


    override fun onTickEntity(entity: Entity) {
        val scoreCmp = scoreCmps[entity]
        scoreCmp.label.setText(scoreCmp.text + scoreCmp.score.toString())
    }



    companion object {
        const val scoreID = 2
        private val log = logger<ScoreSystem>()
    }
}
