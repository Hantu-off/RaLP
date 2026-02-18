plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.hantu"
version = rootProject.property("ralpVersion")!!

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:${rootProject.property("spigotApiVersion")}")
    implementation("org.json:json:20240303")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("net.kyori:adventure-api:4.15.0")
    implementation("net.kyori:adventure-text-minimessage:4.15.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("RaLP-Spigot-${version}-1.17.1-1.21.jar")
        relocate("org.mindrot.jbcrypt", "ru.hantu.ralp.libs.jbcrypt")
        relocate("net.kyori", "ru.hantu.ralp.libs.kyori")
    }
}