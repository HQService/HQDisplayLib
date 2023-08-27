import org.gradle.kotlin.dsl.extra

plugins {
    `maven-publish`
}

file(rootProject.gradle.rootProject.projectDir.path + "/credentials.gradle.kts").let {
    if (it.exists()) {
        apply(it.path)
    }
}

publishing {
    publications {
        create<MavenPublication>("nexus") {
            groupId = project.extra["projectGroup"]!!.toString()
            artifactId = "${project.rootProject.name.lowercase()}-${project.project.name.lowercase()}"
            version = project.extra["projectVersion"]!!.toString()

            from(components["java"])

            pom {
                name.set(extra["projectName"]?.toString())
                url.set(extra["projectUrl"]?.toString())
            }
        }
    }
    repositories {
        maven("https://maven.hqservice.kr/repository/maven-snapshots/") {
            credentials {
                if (extra.has("nexusUsername") && extra.has("nexusPassword")) {
                    this.username = extra["nexusUsername"]!!.toString()
                    this.password = extra["nexusPassword"]!!.toString()
                }
            }
        }
    }
}