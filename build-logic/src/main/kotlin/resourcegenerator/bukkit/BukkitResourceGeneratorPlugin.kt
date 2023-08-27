package resourcegenerator.bukkit

import gradle.kotlin.dsl.accessors._299eb1df697de7c950dd59c9ac5408f6.runtimeClasspath
import org.gradle.kotlin.dsl.exclude
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import resourcegenerator.ResourceGeneratorPlugin

@Suppress("unused")
class BukkitResourceGeneratorPlugin : ResourceGeneratorPlugin<BukkitResourceGeneratorProperties>("bukkit") {
    override fun setDefaults(project: Project, properties: BukkitResourceGeneratorProperties) {
        properties.name = properties.name ?: project.name
        properties.version = properties.version ?: project.version.toString()
        properties.description = properties.description ?: project.description
        properties.website = properties.website ?: project.findProperty("projectUrl")?.toString()
        properties.author = properties.author ?: project.findProperty("projectAuthors")?.toString()
    }

    override fun provideResourceGeneratorProperties(project: Project): BukkitResourceGeneratorProperties {
        return BukkitResourceGeneratorProperties(project)
    }
}

fun Project.excludedRuntimeDependencies(vararg dependencyProviders: Provider<MinimalExternalModuleDependency>): List<Provider<MinimalExternalModuleDependency>> {
    for (dependency in dependencyProviders) {
        println("excluded: ${dependency.get().group}, ${dependency.get().name}")
        configurations.runtimeClasspath.get().exclude(dependency.get().group, dependency.get().name)
    }
    return dependencyProviders.toList()
}
