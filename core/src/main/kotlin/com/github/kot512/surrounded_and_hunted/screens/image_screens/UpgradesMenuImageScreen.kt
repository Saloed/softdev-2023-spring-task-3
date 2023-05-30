package com.github.kot512.surrounded_and_hunted.screens.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import ktx.app.KtxGame
import ktx.app.KtxScreen

class UpgradesMenuImageScreen() : BaseImageScreen(
    Texture("graphics/screen_backgrounds/empty_menu.png")
) {
    private val font = BitmapFont().apply {
        data.setScale(4f)
        color = Color.WHITE
    }

    //    загрузка текстур
    private val returnButtonTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_dead_return")
    )
    private val returnButtonPressedTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_dead_return_pressed")
    )
    private val upgradeBarBaseTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("hud_choose_upgrade_base")
    )
    private val healthUpgradeTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("upgrade_health")
    )
    private val speedUpgradeTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("upgrade_movement_speed")
    )
    private val gunUpgradeTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("upgrade_gunfire")
    )


    //    создание кнопок
    //    кнопка возвращения к главному меню
    private val returnButtonStyle =TextButton.TextButtonStyle().apply {
        font = this@UpgradesMenuImageScreen.font
        up = TextureRegionDrawable(returnButtonPressedTexture)
        down = TextureRegionDrawable(returnButtonTexture)
        checked = TextureRegionDrawable(returnButtonTexture)
    }
    private val returnButton: TextButton = TextButton(null, returnButtonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(MainMenuImageScreen())
                        setScreen<MainMenuImageScreen>()
                        removeScreen<UpgradesMenuImageScreen>()
                    }
                    this@UpgradesMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(CONST_AND_VAR.SCREEN_WIDTH / 2 - width / 2, height / 4)
    }

//    отрисовка базы для прокачки
    private val upgradeBase = object : Actor() {
        init {
            width = upgradeBarBaseTexture.texture.width / 5f * scaleCoeff
            height = upgradeBarBaseTexture.texture.height / 10f * scaleCoeff
            setPosition(
                CONST_AND_VAR.SCREEN_WIDTH / 2 - width / 2,
                CONST_AND_VAR.SCREEN_HEIGHT - height
            )
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            batch.draw(upgradeBarBaseTexture, x, y, width, height)
        }
    }


//    загрузка актуальных цен на прокачку
    private var hpPrice = 5
    private var speedPrice = 5
    private var gunPrice = 5

    init {
        if (!CONST_AND_VAR.SAVE_DATA.contains("hp_price"))
            CONST_AND_VAR.SAVE_DATA.apply {
                putInteger("hp_price", 5)
                flush()
            }
        else hpPrice = CONST_AND_VAR.SAVE_DATA.getInteger("hp_price")

        if (!CONST_AND_VAR.SAVE_DATA.contains("speed_price"))
            CONST_AND_VAR.SAVE_DATA.apply {
            putInteger("speed_price", 5)
            flush()
        }
        else speedPrice = CONST_AND_VAR.SAVE_DATA.getInteger("speed_price")

        if (!CONST_AND_VAR.SAVE_DATA.contains("gun_price"))
            CONST_AND_VAR.SAVE_DATA.apply {
                putInteger("gun_price", 5)
                flush()
            }
        else gunPrice = CONST_AND_VAR.SAVE_DATA.getInteger("gun_price")
    }

//    кнопка увеличения хп
    private val healthUpgrade =
        ImageButton(TextureRegionDrawable(healthUpgradeTexture)).apply {
            addListener(
                object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (CONST_AND_VAR.UPGRADE_POINTS >= hpPrice) {
                            CONST_AND_VAR.UPGRADE_POINTS -= hpPrice
                            hpPrice *= 2

                            CONST_AND_VAR.SAVE_DATA.apply {
                                putInteger("upgrade", CONST_AND_VAR.UPGRADE_POINTS)
                                putInteger("hp_price", hpPrice)
                                putInteger("hp_lvl", getInteger("hp_lvl") + 1)
                                flush()
                            }
                        }
                    }
                }
            )
            setSize(
                healthUpgradeTexture.texture.width / 20f * scaleCoeff,
                healthUpgradeTexture.texture.height / 20f * scaleCoeff
            )
            setPosition(
                upgradeBase.x + 0.2f * upgradeBase.width - width / 2,
                upgradeBase.y + upgradeBase.height / 2 - height / 2,
            )
        }

//    кнопка увеличения скорости
    private val speedUpgrade =
        ImageButton(TextureRegionDrawable(speedUpgradeTexture)).apply {
            addListener(
                object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (CONST_AND_VAR.UPGRADE_POINTS >= speedPrice) {
                            CONST_AND_VAR.UPGRADE_POINTS -= speedPrice
                            speedPrice *= 2

                            CONST_AND_VAR.SAVE_DATA.apply {
                                putInteger("upgrade", CONST_AND_VAR.UPGRADE_POINTS)
                                putInteger("speed_price", speedPrice)
                                putInteger("speed_lvl", getInteger("speed_lvl") + 1)
                                flush()
                            }
                        }
                    }
                }
            )
            setSize(
                healthUpgrade.width,
                healthUpgrade.width
            )
            setPosition(
                upgradeBase.x + 0.8f * upgradeBase.width - width / 2,
                healthUpgrade.y,
            )
        }

//    кнопка усиления волыны
    private val gunUpgrade =
        ImageButton(TextureRegionDrawable(gunUpgradeTexture)).apply {
            addListener(
                object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (CONST_AND_VAR.UPGRADE_POINTS >= gunPrice) {
                            CONST_AND_VAR.UPGRADE_POINTS -= gunPrice
                            gunPrice *= 2

                            CONST_AND_VAR.SAVE_DATA.apply {
                                putInteger("upgrade", CONST_AND_VAR.UPGRADE_POINTS)
                                putInteger("gun_price", gunPrice)
                                putInteger("gun_lvl", getInteger("gun_lvl") + 1)
                                flush()
                            }
                        }
                    }
                }
            )
            setSize(
                healthUpgrade.width,
                healthUpgrade.width
            )
            setPosition(
                (speedUpgrade.x - healthUpgrade.x) / 2 + healthUpgrade.x,
                healthUpgrade.y,
            )
        }

//    актер со всем текстом на экране
    private val textActor = object : Actor() {
        init {
            setPosition(0f, 0f)

        }

        override fun draw(batch: Batch?, parentAlpha: Float) {
            val hpText = """LEVEL: ${CONST_AND_VAR.SAVE_DATA.getInteger("hp_lvl")}
                    |PRICE: $hpPrice
                """.trimMargin()
            val speedText = """LEVEL: ${CONST_AND_VAR.SAVE_DATA.getInteger("speed_lvl")}
                    |PRICE: $speedPrice
                """.trimMargin()
            val gunText = """LEVEL: ${CONST_AND_VAR.SAVE_DATA.getInteger("gun_lvl")}
                    |PRICE: $gunPrice
                """.trimMargin()

            font.apply {
                draw(
                    batch, "YOUR POINTS: ${CONST_AND_VAR.UPGRADE_POINTS}",
                    0f, CONST_AND_VAR.SCREEN_HEIGHT - 10f
                )
                draw(batch, hpText, healthUpgrade.x, upgradeBase.y - 10f)
                draw(batch, speedText, speedUpgrade.x, upgradeBase.y - 10f)
                draw(batch, gunText, gunUpgrade.x, upgradeBase.y - 10f)
            }
        }
    }


    override fun show() {
        stage.addActor(returnButton)

        stage.addActor(upgradeBase)
        stage.addActor(healthUpgrade)
        stage.addActor(speedUpgrade)
        stage.addActor(gunUpgrade)

        stage.addActor(textActor)
    }
}
