package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.BeatusL.mlnk.MLNK.Companion.scale
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.collection.compareEntity
import ktx.graphics.use
import ktx.tiled.forEachLayer


@AllOf([ImageComponent::class])
class RenderSystem(
    private val gStage: Stage,
    @Qualifier("UI")private val uiStage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>
    ): EventListener, IteratingSystem(
    comparator = compareEntity{ e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }   // just a very fast way to compare entities
) {

    private val bLayers = mutableListOf<TiledMapTileLayer>()
    private val fLayers = mutableListOf<TiledMapTileLayer>()
    private val mRender = OrthogonalTiledMapRenderer(null, scale, gStage.batch)
    private val cam = gStage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

        with(gStage) {
            viewport.apply()
            mRender.setView(cam)
            if (bLayers.isNotEmpty()) {
                gStage.batch.use(cam) {
                    bLayers.forEach { mRender.renderTileLayer(it) }
                }
            }

            act(deltaTime)
            draw()

            mRender.setView(cam)
            if (fLayers.isNotEmpty()) {
                gStage.batch.use(gStage.camera as OrthographicCamera) {
                    fLayers.forEach { mRender.renderTileLayer(it) }
                }
            }


        }

        with(uiStage) { //rendering ui
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                bLayers.clear()
                fLayers.clear()

                event.map.forEachLayer<TiledMapTileLayer> { layer ->
                    if (layer.name.startsWith("fgd")) {
                        fLayers.add(layer)
                    } else {
                        bLayers.add(layer)
                    }
                }
                return true
            }


        }
        return false
    }
}
