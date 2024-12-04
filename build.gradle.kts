
plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}


// https://github.com/gradle-nexus/publish-plugin/?tab=readme-ov-file#publishing-to-maven-central-via-sonatype-ossrh
nexusPublishing {
    repositories {
        // the user token is set outside the project
        // https://central.sonatype.org/publish/generate-token/
        sonatype()
    }
}
