package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KClass
import kotlin.test.expect
import kotlin.test.fail

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        expect(8, { 3 + 5 })
    }

    var a = 0
    var b = 0

    @Test
    fun runTests() {
        test(
            given = {
                a = 9
                b = 8
            },
            perform = { a + b },
            expectedResult = 17,
        )

        test(
            given = {
                a = 5
                b = 2
            },
            perform = { throw MyException1() },
            expectedException = MyException1::class,
        )

        lateinit var myClass: MyClass
        lateinit var changedState: InternalState
        test(
            given = {
                myClass = MyClass(InternalState())
            },
            perform = {
                changedState = InternalState()
                myClass.changeState(changedState)
            },
            expected = { changedState },
            actual = { myClass.itsState }
        )
    }

    @Test
    fun `testing ClassWithInt`() {
        lateinit var classWithInt: ClassWithInt

        test(
            given = { classWithInt = ClassWithInt() },
            perform = {},
            expected = { null },
            actual = { classWithInt.int },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState() },
            expected = { 0 },
            actual = { classWithInt.int }
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState(7) },
            expected = { 7 },
            actual = { classWithInt.int },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.signalException() },
            expected = { MyException2::class },
        )
    }
}

private class MyException1: Exception()
private class MyException2: Exception()

fun test(
    given: () -> Unit,
    perform: () -> Unit,
    expectedException: KClass<out Exception>
) {
    given()
    try {
        perform()
    } catch (e: Exception) {
        expect(expectedException, { e::class })
    }
}

fun <T> test(
    given: () -> Unit,
    perform: () -> T,
    expectedResult: T,
) {
    given()
    expect(expectedResult, { perform() })
}

fun test(
    given: () -> Unit,
    perform: () -> Unit,
    actual: () -> Any?,
    expected: () -> Any?,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        with (expected()) {
            expect(this, actual)
        }
    } catch (e: Exception) {
        fail("Unexpected Exception.", e)
    } finally {
        release?.invoke()
    }
}

fun test(
    given: () -> Unit,
    perform: () -> Unit,
    expected: () -> KClass<out Exception>,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        fail("Expected Exception has not been observed: ${expected()}")
    } catch (e: Exception) {
        expect(expected(), { e::class })
    } finally {
        release?.invoke()
    }
}

class MyClass(
    var itsState: InternalState
) {
    fun changeState(anotherState: InternalState) { itsState = anotherState }
}

class InternalState

class ClassWithInt {
    var int: Int? = null

    fun initState(value: Int = 0) { int = value }

    fun signalException() {
        throw MyException2()
    }
}
