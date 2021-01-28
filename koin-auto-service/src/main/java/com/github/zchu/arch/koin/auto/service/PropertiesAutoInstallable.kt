package com.github.zchu.arch.koin.auto.service

import org.koin.core.module.Module

abstract class PropertiesAutoInstallable : KoinAutoInstallable {

    final override val modules: List<Module>
        get() = emptyList()

    abstract override val properties: Map<String, String>

}