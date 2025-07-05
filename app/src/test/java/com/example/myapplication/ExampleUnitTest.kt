package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KClass
import kotlin.test.expect

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
//        test(
//            given = {
//                a = 9
//                b = 2
//            },
//            on = { a + b },
//            then = { it == 111 },
//        )

//        expect(4 , { 4 + 9 })
        test(
            given = {
                a = 9
                b = 8
            },
            after = { a + b },
            result = 17,
        )

        test(
            given = {
                a = 5
                b = 2
            },
            after = { throw MyException1() },
            exception = MyException1::class,
        )

        lateinit var myClass: MyClass
        lateinit var changedState: InternalState
        test(
            given = {
                myClass = MyClass(InternalState())
            },
            after = {
                changedState = InternalState()
                myClass.changeState(changedState)
            },
            verify = { result ->
                result.expected = changedState
                result.actual = myClass.itsState
            }
        )

//        class TestState {
//            lateinit var myClass: MyClass
//        }
//        test(
//            testState = TestState(),
//            given = {
//                myClass = MyClass(InternalState())
//            },
//            after = { myClass.changeState(InternalState()) },
//            verify = { result ->
//                result.expected = changedState
//                result.actual = myClass.itsState
//            }
//        )
    }
}

private class MyException1: Exception()
private class MyException2: Exception()

//fun <T> test(
//    given: () -> Unit,
//    on: () -> T,
//    then: (T) -> Boolean,
//) {
//    given()
//    val result = on()
//    expect(true, { then(result) })
//}

fun test(
    given: () -> Unit,
    after: () -> Unit,
    exception: KClass<out Exception>
) {
    given()
    try {
        after()
    } catch (e: Exception) {
        expect(exception, { e::class })
    }
}

fun <T> test(
    given: () -> Unit,
    after: () -> T,
    result: T,
) {
    given()
    expect(result, { after() })
}

fun <R> test(
    given: () -> Unit,
    after: () -> Unit,
    verify: (ActionResult<R>) -> Unit,
) {
    given()
    after()
    val result = ActionResult<R>()
    verify(result)
    expect(result.expected, { result.actual })
}

class MyClass(
    var itsState: InternalState
) {
    fun changeState(anotherState: InternalState) { itsState = anotherState }
}

class InternalState

class ActionResult<R> {
    var expected: R? = null
    var actual: R? = null
}

//data class Actual<R>(
//    var value: R? = null
//)

//fun <TestState, Result> test(
//    testState: TestState,
//    given: TestState.() -> Unit,
//    after: TestState.() -> Unit,
//    verify: TestState.(ActionResult<Result>) -> Unit,
//) {
//    given(testState)
//    testState.after()
//    val result = ActionResult<Result>()
//    verify(testState, result)
//    expect(result.expected, { result.actual} )
//}
