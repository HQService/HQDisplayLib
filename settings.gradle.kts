rootProject.name = "HQDisplayLib"

dependencyResolutionManagement {
    repositories {
        maven("https://maven.hqservice.kr/repository/maven-public")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    versionCatalogs {
        create("libs") {
            library("spigot-api", "org.spigotmc:spigot-api:${getProperty("spigotVersion")}")
            library("paper-api", "io.papermc.paper:paper-api:${getProperty("spigotVersion")}")

        }
        create("framework") {
            library("core", "kr.hqservice:hqframework-bukkit-core:2.0.1-SNAPSHOT")
            library("command", "kr.hqservice:hqframework-bukkit-command:2.0.1-SNAPSHOT")
            library("nms", "kr.hqservice:hqframework-bukkit-nms:2.0.1-SNAPSHOT")
            library("inventory", "kr.hqservice:hqframework-bukkit-inventory:2.0.1-SNAPSHOT")
            library("database", "kr.hqservice:hqframework-bukkit-database:2.0.1-SNAPSHOT")
            library("scheduler", "kr.hqservice:hqframework-bukkit-scheduler:2.0.1-SNAPSHOT")
        }
    }
}

includeBuild("build-logic")
includeAll("modules")

fun includeAll(modulesDir: String) {
    file("${rootProject.projectDir.path}/${modulesDir.replace(":", "/")}/").listFiles()?.forEach { modulePath ->
        include("${modulesDir.replace("/", ":")}:${modulePath.name}")
    }
}

fun getProperty(key: String): String {
    return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
}
