plugins {
    java
    id("net.minecraftforge.gradle") version "6.0.47"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.hantu"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

minecraft {
    mappings("official", "1.20.1")

    runs {
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                create("ralp") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft("net.minecraftforge:forge:1.20.1-47.3.12")
}

