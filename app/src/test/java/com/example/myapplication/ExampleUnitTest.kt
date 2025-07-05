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
//            validate = { result ->
//                result.expected = changedState
//                result.actual = myClass.itsState
//            }
            validate = {
                Result(
                    actual = myClass.itsState,
                    expected = changedState,
                )
            }
        )
    }

    @Test
    fun `testing ClassWithInt`() {
        lateinit var classWithInt: ClassWithInt

        test(
            given = { classWithInt = ClassWithInt() },
            perform = {},
            validate = { Result(
                actual = classWithInt.int,
                expected = null,
            ) },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState() },
            validate = { Result(
                actual = classWithInt.int,
                expected = 0,
            ) },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState(7) },
            validate = { Result(
                actual = classWithInt.int,
                expected = 7,
            ) }
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.signalException() },
            validate = { Result(
                wasException = MyException2::class
            ) },
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
    validate: () -> Result,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        val result = validate()
        expect(result.expected, { result.actual })
    } catch (e: Exception) {
        val result = validate()
        expect(result.wasException, { e::class })
    } finally {
        release?.invoke()
    }
}

//fun testForResult(
//    given: () -> Unit,
//    perform: () -> Unit,
//    validate: () -> Result,
//    release: (() -> Unit)? = null,
//) {
//    given()
//
//    try {
//        perform()
//    } catch (e: Exception) {
//        fail(e.toString())
//    } finally {
//        release?.invoke()
//    }
//
//    val result = validate()
//    expect(result.expected, { result.actual })
//}
//
//fun testForException(
//    given: () -> Unit,
//    perform: () -> Unit,
//    validate: () -> Result,
//    release: (() -> Unit)? = null,
//) {
//    given()
//
//    try {
//        perform()
//        fail("Expected Exception was not observed: $")
//    } catch (e: Exception) {
//        val result = validate()
//        expect(result.wasException, { e::class })
//    } finally {
//        release?.invoke()
//    }
//}

class MyClass(
    var itsState: InternalState
) {
    fun changeState(anotherState: InternalState) { itsState = anotherState }
}

class InternalState

data class Result(
    var expected: Any? = null,
    var actual: Any? = null,
    var wasException: KClass<out Exception>? = null,
)

class ClassWithInt {
    var int: Int? = null

    fun initState(value: Int = 0) { int = value }

    fun signalException() {
        throw MyException2()
    }
}
