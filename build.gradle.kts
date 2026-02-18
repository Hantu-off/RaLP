tasks.register("shadowJar") {
    dependsOn(":Spigot-1.17.1-1.21:shadowJar")
    dependsOn(":Paper-1.17.1-1.21:shadowJar")
    dependsOn(":Forge-1.20.x:build")
}