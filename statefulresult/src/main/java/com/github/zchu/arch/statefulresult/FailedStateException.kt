package com.github.zchu.arch.statefulresult

class FailedStateException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}