tasks.register("shadowJar") {
    dependsOn(":Spigot-1.17.1-26.x:shadowJar")
    dependsOn(":Paper-1.17.1-26.x:shadowJar")
    dependsOn(":Forge-1.20.x:build")
}