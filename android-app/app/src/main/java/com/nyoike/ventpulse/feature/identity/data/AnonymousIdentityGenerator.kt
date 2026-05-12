package com.nyoike.ventpulse.feature.identity.data

import com.nyoike.ventpulse.feature.identity.domain.AnonymousIdentity
import java.util.UUID
import kotlin.random.Random

class AnonymousIdentityGenerator(
    private val random: Random = Random.Default,
    private val idProvider: () -> String = { UUID.randomUUID().toString() }
) {
    fun create(): AnonymousIdentity {
        val adjective = adjectives[random.nextInt(adjectives.size)]
        val noun = nouns[random.nextInt(nouns.size)]
        val alias = "$adjective $noun"
        val avatarSeed = alias.lowercase().replace(" ", "-")

        return AnonymousIdentity(
            profileId = idProvider(),
            alias = alias,
            avatarSeed = avatarSeed
        )
    }

    private companion object {
        val adjectives = listOf("Quiet", "Soft", "Gentle", "Calm", "Kind", "Warm")
        val nouns = listOf("Cloud", "River", "Orbit", "Echo", "Bloom", "Spark")
    }
}
