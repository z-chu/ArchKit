package com.github.zchu.arch.koin.auto.service

import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.Module
import java.util.*
import kotlin.collections.HashMap

fun KoinApplication.installAutoRegister() {
    val installableServiceLoader = ServiceLoader.load(KoinAutoInstallable::class.java)
    val modules = ArrayList<Module>(20)
    val properties = HashMap<String, String>()
    var installables: ArrayList<KoinAutoInstallable>? = null

    if (koin._logger.isAt(Level.DEBUG)) {
        installables = ArrayList<KoinAutoInstallable>(20)
    }

    installableServiceLoader.iterator().forEach {
        installables?.add(it)
        modules.addAll(it.modules)
        for (property in it.properties) {
            val value = properties[property.key]
            if (value != null) {
                if (koin._logger.isAt(Level.ERROR)) {
                    koin._logger.error("A property with the same key appears, please modify the key to prevent conflicts。key=${property.key},value1=${value}]，value2=${value}]。")
                }
            }
            properties[property.key] = property.value
        }
    }
    if (installables != null) {
        koin._logger.debug("Koin auto install :${installables.map { it.javaClass.name }}")
    }
    modules(modules)
    properties(properties)
}