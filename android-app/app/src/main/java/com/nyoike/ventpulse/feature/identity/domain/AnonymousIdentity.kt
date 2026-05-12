package com.nyoike.ventpulse.feature.identity.domain

data class AnonymousIdentity(
    val profileId: String,
    val alias: String,
    val avatarSeed: String,
    val communityId: String? = null
)
