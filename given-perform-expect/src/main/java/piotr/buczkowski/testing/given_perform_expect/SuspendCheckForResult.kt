// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package piotr.buczkowski.testing.given_perform_expect

import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import kotlin.test.expect
import kotlin.test.fail

fun test(
    given: () -> Unit,
    perform: (suspend () -> Unit)? = null,
    actual: (suspend () -> Any?)? = null,
    expect: (() -> Any?)? = null,
    called: (suspend () -> Any?)? = null,
    release: (() -> Unit)? = null,
) = runTest {
    given()
    try {
        perform?.invoke()
        expect(expect?.invoke(), { actual?.invoke() })
        called?.let { coVerify { it() } }
    } catch (e: Exception) {
        fail("Unexpected Exception.", e)
    } finally {
        release?.invoke()
    }
}
