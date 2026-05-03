import dev.scaffoldit.hytale.wire.HytaleManifest

rootProject.name = "Verdays_Worldgen_Additions"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.+"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        mavenLocal()
        maven {
            url = uri("https://cursemaven.com")
        }
    }

    dependencies {
        implementation("curse.maven:renode-1531866:8028565")
    }

    manifest {
        Group = "Verday"
        Name = "Worldgen_Additions"
        Main = "io.github.itsverday.worldgenadditions.WorldgenAdditionsPlugin"
        ServerVersion = "2026.03.26-89796e57b"
        Version = extra["project.version"] as String
        Description = "Adds custom Worldgen v2 Nodes"
        Authors = listOf(HytaleManifest.Author("Verday", null, null))
        OptionalDependencies = mapOf(Pair("Verday:Renode", "0.5.0"))
        Website = "https://github.com/ItsVerday/Hytale_Worldgen_Additions/"
    }
}