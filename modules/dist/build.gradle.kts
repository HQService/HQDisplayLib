import resourcegenerator.bukkit.excludedRuntimeDependencies

plugins {
    id("hq.shared")
    id("hq.shadow")
    id("hq.resource-generator")
}

bukkitResourceGenerator {
    main = "kr.hqservice."
    name = ""
    apiVersion = "1.13"
    libraries = excludedRuntimeDependencies()
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(framework.core)
    runtimeOnly(project(":modules:core"))
    runtimeOnly(project(":modules:api"))
}