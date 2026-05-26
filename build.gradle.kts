/**
 * NOTE: This is entirely optional and basics can be done in `settings.gradle.kts`
 */

rootProject.version = extra["project.version"] as String

repositories {
    // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
}

dependencies {
    // Any external dependency you also want to include
}