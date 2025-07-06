// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package com.example.myapplication

class MyException1: Exception()
class MyException2: Exception()

class MyClass(
    var itsState: InternalState
) {
    fun changeState(anotherState: InternalState) { itsState = anotherState }
    fun someAction() {
        itsState.internalStateAction()
        itsState.anotherAction()
    }
}

class InternalState {
    fun internalStateAction() {}
    fun anotherAction() {}
}

class ClassWithInt(val internalState: InternalState = InternalState()) {
    var int: Int? = null

    fun initState(value: Int = 0) { int = value }

    fun signalException() {
        internalState.internalStateAction()
        throw MyException2()
    }
}
