package ru.shprot.sudokumobdevkz.core.base.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ReviewRepositoryImplTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repo: ReviewRepository

    @Before
    fun setUp() {
        val file: File = tmp.newFile("review_state.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(produceFile = { file })
        repo = ReviewRepositoryImpl(dataStore)
    }

    @Test
    fun `wasSessionWon returns false by default`() = runTest {
        assertFalse(repo.wasSessionWon())
    }

    @Test
    fun `markSessionWon sets the flag`() = runTest {
        repo.markSessionWon()
        assertTrue(repo.wasSessionWon())
    }

    @Test
    fun `clearSessionWon resets the flag`() = runTest {
        repo.markSessionWon()
        repo.clearSessionWon()
        assertFalse(repo.wasSessionWon())
    }

    @Test
    fun `lastReviewRequestedAt returns zero by default`() = runTest {
        assertEquals(0L, repo.lastReviewRequestedAt())
    }

    @Test
    fun `markReviewRequested stores epoch millis`() = runTest {
        val before = System.currentTimeMillis()
        repo.markReviewRequested()
        val after = System.currentTimeMillis()
        val stored = repo.lastReviewRequestedAt()
        assertTrue("stored=$stored not in [$before,$after]", stored in before..after)
    }
}