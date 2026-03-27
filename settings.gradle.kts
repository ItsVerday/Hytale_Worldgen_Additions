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
        exclusiveContent {
            forRepository {
                ivy {
                    name = "Modtale"
                    url = uri("https://api.modtale.net/api/v1")
                    patternLayout {
                        artifact("projects/[module]/versions/[revision]/download")
                    }
                    metadataSources {
                        artifact()
                    }
                }
            }
            filter {
                includeGroup("modtale")
            }
        }
    }

    dependencies {
        // compileOnly("modtale:renode:0.4.0@jar")
        implementation("modtale:renode:0.4.0@jar")
    }

    manifest {
        Group = "Verday"
        Name = "Worldgen_Additions"
        Main = "io.github.itsverday.worldgenadditions.WorldgenAdditionsPlugin"
        ServerVersion = "2026.03.26-89796e57b"
    }
}