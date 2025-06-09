package com.moneyfoward.githubclone.github.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UserRepoKtTest {
    @Test
    fun `when stars is under 1000 show as it is`() {
        val userRepo =
            UserRepo(
                id = 1,
                name = "somename",
                fullName = "",
                stars = 10,
                isFork = false,
            )
        val expectedOutput = "10"
        assertEquals(expectedOutput, userRepo.stars())
    }

    @Test
    fun `when stars is more than 1000 show as with K format`() {
        val userRepo =
            UserRepo(
                id = 1,
                name = "somename",
                fullName = "",
                stars = 1752,
                isFork = false,
            )
        val expectedOutput = "1.7K"
        assertEquals(expectedOutput, userRepo.stars())
    }

    @Test
    fun `when stars is way more than 1000 show as with K format`() {
        val userRepo =
            UserRepo(
                id = 1,
                name = "somename",
                fullName = "",
                stars = 239193,
                isFork = false,
            )
        val expectedOutput = "239.1K"
        assertEquals(expectedOutput, userRepo.stars())
    }
}
