/// Copyright (c) 2022 anatawa12 and other contributors
/// This file is part of *All Config Syncer, released under MIT License
/// See LICENSE at https://github.com/freedom-mod-development-community/asta-all-config-syncer for more details

plugins {
    id("net.minecraftforge.gradle")
}

version = property("modVersion")!!
group = property("modGroup")!!
base.archivesName.set(property("modBaseName")!!.toString())

val mcpChannel: String by extra
val mcpVersion: String by extra

minecraft.mappings(mcpChannel, mcpVersion)

sourceSets.main.get().resources.srcDir("src/generated/resources")

val shade by configurations.creating
configurations.implementation.get().extendsFrom(shade)

repositories {
    mavenCentral()
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.12.2-14.23.5.2855")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

val processResources by tasks.getting(Copy::class) {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", project.version)

    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    // replace stuff in mcmod.info, nothing else
    from(sourceSets.main.get().resources.srcDirs) {
        include("mcmod.info")

        // replace version and mcversion
        expand(mapOf(
            "version" to project.version,
            "mcversion" to "1.12.2"
        ))
    }

    // copy everything else, thats not the mcmod.info
    from(sourceSets.main.get().resources.srcDirs) {
        exclude("mcmod.info")
    }

    // copy license file
    from("LICENSE")
}

// workaround for userdev bug
val copyResourceToClasses by tasks.creating(Copy::class) {
    tasks.classes.get().dependsOn(this)
    dependsOn(tasks.processResources)
    onlyIf { gradle.taskGraph.hasTask(tasks.getByName("prepareRuns")) }

    into("$buildDir/classes/java/main")
    from(tasks.processResources.get().destinationDir)
}

val coremod = "xyz.fmdc.astaAllConfigSyncer.DebugCoreMod"

fun net.minecraftforge.gradle.common.util.RunConfig.commonConfigure() {
    args("--noCoreSearch")

    property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")
    property("fml.coreMods.load", coremod)
    property("legacy.debugClassLoading", "true")
    //property("legacy.debugClassLoadingSave", "true")
}

val runClient = minecraft.runs.create("client") {
    workingDirectory(project.file("run"))
    commonConfigure()
}

val runServer = minecraft.runs.create("server") {
    workingDirectory(project.file("run/server"))
    commonConfigure()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes(mapOf(
            //"FMLCorePlugin" to coremod,
            "FMLCorePluginContainsFMLMod" to "*",
        ))
    }
}

tasks.jar.get().finalizedBy("reobfJar")

tasks.test {
    useJUnitPlatform()
}
