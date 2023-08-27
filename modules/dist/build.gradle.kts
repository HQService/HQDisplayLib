import resourcegenerator.bukkit.excludedRuntimeDependencies

plugins {
    id("hq.shared")
    id("hq.shadow")
    id("hq.resource-generator")
}

bukkitResourceGenerator {
    main = "kr.hqservice.display.HQDisplayLibMain"
    name = "HQDisplayLib"
    apiVersion = "1.19"
    libraries = excludedRuntimeDependencies()
    depend = listOf("HQFramework")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(framework.core)
    runtimeOnly(project(":modules:nms"))
}