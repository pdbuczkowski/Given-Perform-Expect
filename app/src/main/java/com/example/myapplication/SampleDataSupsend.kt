// Copyright (c) 2025 Piotr Buczkowski – Licensed under the MIT License

package com.example.myapplication

class MyClassSuspend(
    var itsState: InternalStateSuspend
) {
    suspend fun changeState(anotherState: InternalStateSuspend) { itsState = anotherState }
    suspend fun someAction() {
        itsState.internalStateAction()
        itsState.anotherAction()
    }
}

class InternalStateSuspend {
    suspend fun internalStateAction() {}
    suspend fun anotherAction() {}
}

class ClassWithIntSuspend(val internalState: InternalStateSuspend = InternalStateSuspend()) {
    private var int: Int? = null
    suspend fun readValue(): Int? = int


    fun initState(value: Int = 0) { int = value }

    suspend fun signalException() {
        internalState.internalStateAction()
        throw MyException2()
    }
}
