package com.nyoike.ventpulse.feature.coreflow.data

import com.nyoike.ventpulse.core.data.database.CachedCommunityEntity
import com.nyoike.ventpulse.feature.coreflow.domain.Community

fun CommunityDto.toEntity(): CachedCommunityEntity {
    return CachedCommunityEntity(
        id = id,
        parentId = parentId,
        name = name,
        slug = slug,
        path = path,
        level = level,
        sortOrder = sortOrder
    )
}

fun CachedCommunityEntity.toCommunity(): Community {
    return Community(
        id = id,
        parentId = parentId,
        name = name,
        path = path,
        level = level,
        sortOrder = sortOrder
    )
}
