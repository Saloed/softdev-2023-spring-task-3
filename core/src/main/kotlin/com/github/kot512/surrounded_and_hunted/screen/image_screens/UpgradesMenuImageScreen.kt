package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
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
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.UPGRADE_POINTS
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SAVE_DATA
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
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return")
    )
    private val returnButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return_pressed")
    )
    private val upgradeBarBaseTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("hud_choose_upgrade_base")
    )
    private val healthUpgradeTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("upgrade_health")
    )
    private val speedUpgradeTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("upgrade_movement_speed")
    )
    private val gunUpgradeTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("upgrade_gunfire")
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
        setPosition(SCREEN_WIDTH / 2 - width / 2, height / 4)
    }

//    отрисовка базы для прокачки
    private val upgradeBase = object : Actor() {
        init {
            width = upgradeBarBaseTexture.texture.width / 5f * scaleCoeff
            height = upgradeBarBaseTexture.texture.height / 10f * scaleCoeff
            setPosition(
                SCREEN_WIDTH / 2 - width / 2,
                SCREEN_HEIGHT - height
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
        if (!SAVE_DATA.contains("hp_price"))
            SAVE_DATA.apply {
                putInteger("hp_price", 5)
                flush()
            }
        else hpPrice = SAVE_DATA.getInteger("hp_price")

        if (!SAVE_DATA.contains("speed_price"))
        SAVE_DATA.apply {
            putInteger("speed_price", 5)
            flush()
        }
        else speedPrice = SAVE_DATA.getInteger("speed_price")

        if (!SAVE_DATA.contains("gun_price"))
            SAVE_DATA.apply {
                putInteger("gun_price", 5)
                flush()
            }
        else gunPrice = SAVE_DATA.getInteger("gun_price")
    }

//    кнопка увеличения хп
    private val healthUpgrade =
        ImageButton(TextureRegionDrawable(healthUpgradeTexture)).apply {
            addListener(
                object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (UPGRADE_POINTS >= hpPrice) {
                            UPGRADE_POINTS -= hpPrice
                            hpPrice *= 2

                            SAVE_DATA.apply {
                                putInteger("upgrade", UPGRADE_POINTS)
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
                        if (UPGRADE_POINTS >= speedPrice) {
                            UPGRADE_POINTS -= speedPrice
                            speedPrice *= 2

                            SAVE_DATA.apply {
                                putInteger("upgrade", UPGRADE_POINTS)
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
                        if (UPGRADE_POINTS >= gunPrice) {
                            UPGRADE_POINTS -= gunPrice
                            gunPrice *= 2

                            SAVE_DATA.apply {
                                putInteger("upgrade", UPGRADE_POINTS)
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
            val hpText = """LEVEL: ${SAVE_DATA.getInteger("hp_lvl")}
                    |PRICE: $hpPrice
                """.trimMargin()
            val speedText = """LEVEL: ${SAVE_DATA.getInteger("speed_lvl")}
                    |PRICE: $speedPrice
                """.trimMargin()
            val gunText = """LEVEL: ${SAVE_DATA.getInteger("gun_lvl")}
                    |PRICE: $gunPrice
                """.trimMargin()

            font.apply {
                draw(batch, "YOUR POINTS: $UPGRADE_POINTS", 0f, SCREEN_HEIGHT - 10f)
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

//    override fun render(delta: Float) {
//        Gdx.gl.glClearColor(0.82353f, 0.27843f, 0.14902f, 1f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//
//        super.batch.begin()
//        super.batch.draw(
//            backgroundTexture,
//            backgroundPosition.x - scaledWidth / 2,
//            backgroundPosition.y - scaledHeight / 2,
//            scaledWidth, scaledHeight
//        )
//        super.batch.draw(
//            upgradeBarBaseTexture,
//            SCREEN_WIDTH / 2 - upgradeBarBaseTexture.texture.width / 10f * scaleCoeff,
//            SCREEN_HEIGHT - upgradeBarBaseTexture.texture.height / 8f * scaleCoeff,
//            upgradeBarBaseTexture.texture.width / 5f * scaleCoeff,
//            upgradeBarBaseTexture.texture.height / 10f * scaleCoeff
//        )
//        super.batch.end()
//
//        stage.act()
//        stage.draw()
//    }
}
