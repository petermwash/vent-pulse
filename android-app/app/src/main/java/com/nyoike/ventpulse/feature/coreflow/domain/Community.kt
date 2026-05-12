package com.nyoike.ventpulse.feature.coreflow.domain

data class Community(
    val id: String,
    val parentId: String?,
    val name: String,
    val path: String,
    val level: String,
    val sortOrder: Int
)
