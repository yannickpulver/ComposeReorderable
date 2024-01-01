import org.jetbrains.compose.ComposeBuildConfig.composeVersion

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("maven-publish")
    id("signing")
}

group = "io.github.yannickpulver.composereorderable"
version = "0.9.8"

kotlin {
    jvm()
    macosArm64()
    macosX64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation("org.jetbrains.compose.ui:ui-util:${composeVersion}")
            }
        }
    }
}

val javadocJar = tasks.register("javadocJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        repositories {
            maven {
                name="oss"
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                credentials {
                    username = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") ?: extra.properties.getOrDefault("ossrhUsername", "") as String
                    password = System.getenv("ORG_GRADLE_PROJECT_mavenCentralPassword") ?: extra.properties.getOrDefault("ossrhPassword", "") as String
                }
            }
        }
    }
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("ComposeReorderable")
                description.set("Reorderable Compose LazyList")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }
                url.set("https://github.com/yannickpulver/ComposeReorderable")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/yannickpulver/ComposeReorderable/issues")
                }
                scm {
                    connection.set("https://github.com/yannickpulver/ComposeReorderable.git")
                    url.set("https://github.com/yannickpulver/ComposeReorderable")
                }
                developers {
                    developer {
                        name.set("Andre Claßen")
                        email.set("andreclassen1337@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
