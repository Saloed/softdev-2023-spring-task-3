package com.github.BeatusL.mlnk.system

import com.github.BeatusL.mlnk.component.AnimationModel
import com.github.BeatusL.mlnk.component.AnimationPlaymode
import com.github.BeatusL.mlnk.component.SpawnConfig
import ktx.app.gdxError

class SpawnConfigGetter {

    fun spawmConfig(type: EntityType): SpawnConfig {
        return when (type) {

            EntityType.Player -> SpawnConfig(
                AnimationModel.player,
                AnimationPlaymode.Loop,
                7f
            )

            EntityType.B -> SpawnConfig(
                AnimationModel.B,
                AnimationPlaymode.Loop,
                5f
            )

            EntityType.M -> SpawnConfig(
                AnimationModel.M,
                AnimationPlaymode.Loop,
                4f
            )

            EntityType.S -> SpawnConfig(
                AnimationModel.S,
                AnimationPlaymode.Loop,
                4f
            )

            EntityType.exps -> SpawnConfig(
                AnimationModel.exps,
                AnimationPlaymode.Normal,
                0f
            )

            EntityType.BP -> SpawnConfig(
                AnimationModel.BP,
                AnimationPlaymode.Loop,
                10f
            )

            EntityType.RP -> SpawnConfig(
                AnimationModel.RP,
                AnimationPlaymode.Loop,
                8f
            )

            EntityType.Box -> SpawnConfig(
                AnimationModel.Box,
                AnimationPlaymode.Loop,
                5f
            )

            else -> gdxError("Unexpected Entity type: $type")
        }
    }

    fun stringToType(string: String?) = when (string) {
        "Player" -> EntityType.Player
        "B" -> EntityType.B
        "S" -> EntityType.S
        "M" -> EntityType.M
        "BP" -> EntityType.BP
        "RP" -> EntityType.RP
        "exps" -> EntityType.exps
        "Box" -> EntityType.Box
        else -> gdxError("$string has no spawn configuration!")
    }
}

enum class EntityType {
    Player, B, S, M, BP, RP, exps, Box, Default;

    fun isEnemy() = this in listOf(B, M, S)
}
