package com.example.musible

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.example.musible.Auth.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28]) 

class GoogleAuthUiClientTest {

    private lateinit var googleAuthUiClient: GoogleAuthUiClient
    private lateinit var context: Context
    private lateinit var oneTapClient: SignInClient

    @Before
    fun setUp() {
        context = Robolectric.buildActivity(MockActivity::class.java).create().get()
        oneTapClient = mock(SignInClient::class.java)
        googleAuthUiClient = GoogleAuthUiClient(context, oneTapClient)
    }

    @Test
    fun `signIn should return IntentSender on success`() = runTest {
        val intentSender = mock<IntentSender>()
        val beginSignInResult = mock<BeginSignInResult> {
            on { pendingIntent.intentSender } doReturn intentSender
        }
        val task: Task<BeginSignInResult> = mock()
        whenever(task.isSuccessful).thenReturn(true)
        whenever(task.result).thenReturn(beginSignInResult)

        whenever(oneTapClient.beginSignIn(any())).thenReturn(task)

        val result = googleAuthUiClient.signIn()

        assertEquals(intentSender, result)
    }

    @Test
    fun `signIn should return null on failure`() = runTest {
        whenever(oneTapClient.beginSignIn(any())).thenThrow(RuntimeException("Sign-in failed"))

        val result = googleAuthUiClient.signIn()

        assertNull(result)
    }
}

class MockActivity : Activity()
