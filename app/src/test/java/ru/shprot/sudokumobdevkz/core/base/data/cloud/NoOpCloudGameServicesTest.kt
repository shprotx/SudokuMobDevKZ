package ru.shprot.sudokumobdevkz.core.base.data.cloud

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState

class NoOpCloudGameServicesTest {

    private val noOp = NoOpCloudGameServices()

    @Test
    fun `isAvailable is false`() {
        assertFalse(noOp.isAvailable)
    }

    @Test
    fun `signInState is NotAvailable`() {
        assertEquals(SignInState.NotAvailable, noOp.signInState.value)
    }

    @Test
    fun `trySilentSignIn returns NotAvailable`() = runTest {
        assertEquals(SignInResult.NotAvailable, noOp.trySilentSignIn())
    }

    @Test
    fun `requestSignIn returns NotAvailable`() = runTest {
        assertEquals(SignInResult.NotAvailable, noOp.requestSignIn())
    }

    @Test
    fun `loadTopScores returns empty list`() = runTest {
        assertTrue(noOp.loadTopScores("any", 10).isEmpty())
    }

    @Test
    fun `loadPlayerScore returns null`() = runTest {
        assertNull(noOp.loadPlayerScore("any"))
    }

    @Test
    fun `readSnapshot returns null`() = runTest {
        assertNull(noOp.readSnapshot("any"))
    }

    @Test
    fun `mutator methods are no-op and do not throw`() = runTest {
        noOp.signOut()
        noOp.unlockAchievement("any")
        noOp.incrementAchievement("any", 1)
        noOp.submitScore("any", 1L)
        noOp.writeSnapshot("any", byteArrayOf(), "desc")
        noOp.detachActivity()
    }
}
