import org.jetbrains.changelog.Changelog

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)
fun projectProperties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    // kotlin("jvm") version "2.0.0"
    id("java")
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij.platform") version "2.1.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.2.1"
}

val pluginId = projectProperties("pluginId")
val pluginName = projectProperties("pluginName")
val pluginDescription = projectProperties("pluginDescription")
val pluginGroup = projectProperties("pluginGroup")
val pluginVersion = projectProperties("pluginVersion")
val pluginSinceBuild = projectProperties("pluginSinceBuild")
val pluginUntilBuild = projectProperties("pluginUntilBuild")
val vendorName = projectProperties("vendorName")
val vendorEmail = projectProperties("vendorEmail")
val pluginUrl = projectProperties("pluginUrl")
val platformVersion = projectProperties("platformVersion")
val platformPlugins = properties("platformPlugins").map { it.split(',') }
group = "org.mustabelmo"
version = "1.0.0"

// Configure project's dependencies
repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        intellijDependencies()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    pluginConfiguration {
        id = pluginId
        name = pluginName
        version = pluginVersion
        changeNotes.set(provider {
            changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML)
        })
        description = pluginDescription

        ideaVersion {
            sinceBuild.set(pluginSinceBuild)
//            untilBuild.set(pluginUntilBuild)
        }


        vendor {
            name.set(vendorName)
            email.set(vendorEmail)
            url.set(pluginUrl)
        }
    }

//    pluginVerification {
//        failureLevel = listOf(VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS)
//
//        ides {
//            recommended()
//        }
//    }

    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }
}

dependencies {
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.0")

    intellijPlatform {
        instrumentationTools()
        intellijIdeaCommunity(platformVersion)
        pluginVerifier()
        // bundledPlugins(platformPlugins)
    }
}

// Set the JVM language level used to build the project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version = "1.0.0"
    groups.empty()
    repositoryUrl.set(pluginUrl)
}
tasks {
    test {
        useJUnitPlatform() // Enable JUnit 5 support
        testLogging {
            showStandardStreams = true // Show standard output and error during test execution
        }
    }
    patchPluginXml {
        version = "0.0.0"
        changeNotes = ""
        println("HERE")
    }
}