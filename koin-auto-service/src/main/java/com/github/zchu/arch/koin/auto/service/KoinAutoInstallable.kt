package com.github.zchu.arch.koin.auto.service

import org.koin.core.module.Module

@Suppress("unused")
interface KoinAutoInstallable {

    val modules: List<Module>

    val properties: Map<String, String>

}