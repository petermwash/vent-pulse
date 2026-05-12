package com.nyoike.ventpulse.feature.identity.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class AnonymousIdentityGeneratorTest {

    @Test
    fun createReturnsAnonymousIdentityWithAliasAndAvatarSeed() {
        val generator = AnonymousIdentityGenerator(
            random = Random(1),
            idProvider = { "profile-id" }
        )

        val identity = generator.create()

        assertEquals("profile-id", identity.profileId)
        assertTrue(identity.alias.isNotBlank())
        assertEquals(
            identity.alias.lowercase().replace(" ", "-"),
            identity.avatarSeed
        )
    }
}
