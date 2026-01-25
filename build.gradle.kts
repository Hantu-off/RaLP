plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.hantu"
version = "0.7.3-1.17.1+"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
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
        archiveFileName.set("RaLP-Spigot-${version}.jar")
        relocate("org.mindrot.jbcrypt", "net.hantu.ralp.libs.jbcrypt")
        relocate("net.kyori", "net.hantu.ralp.libs.kyori")
    }
}
