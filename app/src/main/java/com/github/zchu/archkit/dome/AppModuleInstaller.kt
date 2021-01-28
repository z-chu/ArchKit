package com.github.zchu.archkit.dome

import com.github.zchu.arch.koin.auto.service.KoinAutoInstallable
import com.github.zchu.arch.koin.auto.service.ModuleAutoInstallable
import com.google.auto.service.AutoService
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@AutoService(KoinAutoInstallable::class)
class AppModuleInstaller : ModuleAutoInstallable() {

    override val module: Module = module {

            factory (named("auto_install_string")){
                "这是自动注入的字符串"
            }
    }
}