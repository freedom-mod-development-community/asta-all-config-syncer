buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "ossrh-snapshot"
        }
        maven(url = "https://maven.minecraftforge.net/") {
            name = "forge"
        }
    }
    dependencies {
        // use latest version by dependabot. dependabot supports dependencies in settings.gralde
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.27")
    }
}

rootProject.name = "asta-all-config-syncer"
