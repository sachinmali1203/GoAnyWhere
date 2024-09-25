pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // PREFER_SETTINGS will take care of managing all repositories
    repositories {
        google()         // Include Google's Maven repository
        mavenCentral()   // Include Maven Central repository
    }
}

rootProject.name = "GoAnyWhere"
include(":app")
