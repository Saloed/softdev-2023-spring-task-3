package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.utils.TimeUtils

data class ProjectileComponent(
    var prevTime: Long = TimeUtils.nanoTime(),
    var prjType: String = ""
)

