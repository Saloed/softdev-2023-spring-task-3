package com.github.BeatusL.mlnk.component

import com.badlogic.gdx.utils.TimeUtils
import com.github.BeatusL.mlnk.system.EntityType

data class ProjectileComponent(
    var prevTime: Long = TimeUtils.nanoTime(),
    var prjType: EntityType = EntityType.Default
)

