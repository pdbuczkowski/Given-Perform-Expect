// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package piotr.buczkowski.testing.given_perform_expect

import io.mockk.verify
import kotlin.reflect.KClass
import kotlin.test.expect
import kotlin.test.fail

fun test(
    given: () -> Unit,
    perform: () -> Unit,
    expect: () -> KClass<out Exception>,
    called: (() -> Any?)? = null,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        fail("Expected Exception has not been observed: $expect")
    } catch (e: Exception) {
        expect(expect(), { e::class })
        called?.let { verify { it() } }
    } finally {
        release?.invoke()
    }
}
