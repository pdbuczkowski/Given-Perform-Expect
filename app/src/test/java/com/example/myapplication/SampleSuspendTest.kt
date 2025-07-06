// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package com.example.myapplication

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import piotr.buczkowski.testing.given_perform_expect.coTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SampleSuspendTest {

    lateinit var myClass: MyClassSuspend
    lateinit var changedState: InternalStateSuspend

    @Test
    fun `testing for expected result`() = runTest {
        coTest(
            given = {
                myClass = MyClassSuspend(InternalStateSuspend())
            },
            perform = {
                changedState = InternalStateSuspend()
                myClass.changeState(changedState)
            },
            expect = { changedState },
            actual = { myClass.itsState },
        )
    }

    lateinit var classWithInt: ClassWithIntSuspend
    @Test
    fun `reading uninitialized 'null' state`() = runTest {
        coTest(
            given = { classWithInt = ClassWithIntSuspend() },
            perform = {},
            actual = { classWithInt.readValue() },
            expect = { null },
        )
    }

    @Test
    fun `checking state after initialization`() = runTest {
        coTest(
            given = { classWithInt = ClassWithIntSuspend() },
            perform = { classWithInt.initState() },
            actual = { classWithInt.readValue() },
            expect = { 0 },
        )
    }

    @Test
    fun `checking state after change`() = runTest {
        coTest(
            given = { classWithInt = ClassWithIntSuspend() },
            perform = { classWithInt.initState(7) },
            actual = { classWithInt.readValue() },
            expect = { 7 },
        )
    }

    lateinit var internalState: InternalStateSuspend
    @Test
    fun `check for an exception and if a method was called during the test`() = runTest {
        coTest(
            given = {
                internalState = mockk(relaxed = true)
                classWithInt = ClassWithIntSuspend(internalState)
            },
            perform = { classWithInt.signalException() },
            expect = { MyException2::class },
            called = { classWithInt.internalState.internalStateAction() }
        )
    }

    lateinit var mockedState: InternalStateSuspend
    @Test
    fun `check if some methods were called during the test`() = runTest {
        coTest(
            given = {
                mockedState = mockk(relaxed = true)
                myClass = MyClassSuspend(mockedState)
            },
            perform = { myClass.someAction() },
            called = {
                mockedState.internalStateAction()
                mockedState.anotherAction()
            },
        )
    }
}
