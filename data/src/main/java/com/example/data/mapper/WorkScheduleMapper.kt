package com.example.data.mapper

import com.example.data.entity.WorkScheduleEntity
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType

fun WorkScheduleEntity.toDomain(): WorkSchedule {
    return WorkSchedule(
        date = date,
        type = WorkType.valueOf(type),
        note = note
    )
}

fun WorkSchedule.toEntity(): WorkScheduleEntity {
    return WorkScheduleEntity(
        date = date,
        type = type.name,
        note = note
    )
}
