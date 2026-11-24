pluginManagement {
    repositories {
        maven("https://maven.minecraftforge.net") {
            name = "Forge"
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "RaLP"

include("Spigot-1.17.1-26.x")
include("Paper-1.17.1-26.x")
include("Forge-1.20")