// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package piotr.buczkowski.testing.given_perform_expect

import io.mockk.verify
import kotlin.test.expect
import kotlin.test.fail

fun test(
    given: () -> Unit,
    perform: (() -> Unit)? = null,
    actual: (() -> Any?)? = null,
    expect: (() -> Any?)? = null,
    called: (() -> Any?)? = null,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform?.invoke()
        expect(expect?.invoke(), { actual?.invoke() })
        called?.let { verify { it() } }
    } catch (e: Exception) {
        fail("Unexpected Exception.", e)
    } finally {
        release?.invoke()
    }
}
