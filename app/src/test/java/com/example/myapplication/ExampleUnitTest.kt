package com.example.myapplication

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
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
            expect = { changedState },
            actual = { myClass.itsState }
        )
    }

    @Test
    fun `testing ClassWithInt`() {
        lateinit var classWithInt: ClassWithInt

        test(
            given = { classWithInt = ClassWithInt() },
            perform = {},
            actual = { classWithInt.int },
            expect = { null },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState() },
            actual = { classWithInt.int },
            expect = { 0 },
        )

        test(
            given = { classWithInt = ClassWithInt() },
            perform = { classWithInt.initState(7) },
            actual = { classWithInt.int },
            expect = { 7 },
        )

        lateinit var internalState: InternalState
        test(
            given = {
                internalState = mockk(relaxed = true)
                classWithInt = ClassWithInt(internalState)
            },
            perform = { classWithInt.signalException() },
            expect = { MyException2::class },
            expectWasCalled = { classWithInt.internalState.internalStateAction() }
        )

        lateinit var mockState: InternalState
        lateinit var myClass: MyClass
        test(
            given = {
                mockState = mockk(relaxed = true)
                myClass = MyClass(mockState)
            },
            perform = { myClass.someAction() },
            expect = { null },
            actual = { null },
            expectWasCalled = { mockState.internalStateAction() },
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
    expect: () -> Any?,
    expectWasCalled: (() -> Any?)? = null,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        expect(expect(), actual)
        expectWasCalled?.let { verify { it() } }
    } catch (e: Exception) {
        fail("Unexpected Exception.", e)
    } finally {
        release?.invoke()
    }
}

fun test(
    given: () -> Unit,
    perform: () -> Unit,
    expect: () -> KClass<out Exception>,
    expectWasCalled: (() -> Any?)? = null,
    release: (() -> Unit)? = null,
) {
    given()
    try {
        perform()
        fail("Expected Exception has not been observed: ${expect()}")
    } catch (e: Exception) {
        expect(expect(), { e::class })
        expectWasCalled?.let { verify { it() } }
    } finally {
        release?.invoke()
    }
}

class MyClass(
    var itsState: InternalState
) {
    fun changeState(anotherState: InternalState) { itsState = anotherState }
    fun someAction() {
        itsState.internalStateAction()
    }
}

class InternalState {
    fun internalStateAction() {}
}

class ClassWithInt(val internalState: InternalState = InternalState()) {
    var int: Int? = null

    fun initState(value: Int = 0) { int = value }

    fun signalException() {
        internalState.internalStateAction()
        throw MyException2()
    }
}
