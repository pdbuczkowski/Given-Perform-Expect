// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package com.example.myapplication

import io.mockk.mockk
import org.junit.Test
import piotr.buczkowski.testing.given_perform_expect.coTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SampleTest {

    var a = 0
    var b = 0

    @Test
    fun `check for expected result`() = coTest(
        given = {
            a = 9
            b = 8
        },
        actual = { a + b },
        expect = { 17 },
    )

    @Test
    fun `checking for expected exception`() = coTest(
        given = {},
        perform = { throw MyException1() },
        expect = { MyException1::class },
    )

    lateinit var myClass: MyClass
    lateinit var changedState: InternalState

    @Test
    fun `testing for expected result`() = coTest(
        given = {
            myClass = MyClass(InternalState())
        },
        perform = {
            changedState = InternalState()
            myClass.changeState(changedState)
        },
        expect = { changedState },
        actual = { myClass.itsState },
    )

    lateinit var classWithInt: ClassWithInt
    @Test
    fun `reading uninitialized 'null' state`() = coTest(
        given = { classWithInt = ClassWithInt() },
        perform = {},
        actual = { classWithInt.int },
        expect = { null },
    )

    @Test
    fun `checking state after initialization`() = coTest(
        given = { classWithInt = ClassWithInt() },
        perform = { classWithInt.initState() },
        actual = { classWithInt.int },
        expect = { 0 },
    )

    @Test
    fun `checking state after change`() = coTest(
        given = { classWithInt = ClassWithInt() },
        perform = { classWithInt.initState(7) },
        actual = { classWithInt.int },
        expect = { 7 },
    )

    lateinit var internalState: InternalState
    @Test
    fun `check for an exception and if a method was called during the test`() = coTest(
        given = {
            internalState = mockk(relaxed = true)
            classWithInt = ClassWithInt(internalState)
        },
        perform = { classWithInt.signalException() },
        expect = { MyException2::class },
        called = { classWithInt.internalState.internalStateAction() }
    )

    lateinit var mockedState: InternalState
    @Test
    fun `check if some methods were called during the test`() = coTest(
        given = {
            mockedState = mockk(relaxed = true)
            myClass = MyClass(mockedState)
        },
        perform = { myClass.someAction() },
        called = {
            mockedState.internalStateAction()
            mockedState.anotherAction()
        },
    )
}
