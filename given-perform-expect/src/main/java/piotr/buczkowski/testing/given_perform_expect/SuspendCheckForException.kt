// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package piotr.buczkowski.testing.given_perform_expect

import io.mockk.coVerify
import kotlin.reflect.KClass
import kotlin.test.expect
import kotlin.test.fail

suspend fun coTest(
    given: () -> Unit,
    perform: suspend () -> Unit,
    expect: () -> KClass<out Exception>,
    called: (suspend () -> Any?)? = null,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        fail("Expected Exception has not been observed: $expect")
    } catch (e: Exception) {
        expect(expect(), { e::class })
        called?.let { coVerify { it() } }
    } finally {
        release?.invoke()
    }
}
