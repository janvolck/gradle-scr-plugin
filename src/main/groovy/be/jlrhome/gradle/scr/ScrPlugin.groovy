package be.jlrhome.gradle.scr

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.SourceDirectorySet

class ScrPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create("scr", ScrPluginExtension, project)
        project.task('generateScrDescriptor', type: GenerateDescriptorTask).dependsOn(project.compileJava)
        project.jar.dependsOn 'generateScrDescriptor'
    }
}

class ScrPluginExtension {

    def File outputDirectory
    def boolean generateAccessors = true
    def boolean strictMode = false
    def boolean scanClasses = false
    def SourceDirectorySet sources
    def String specVersion = null
    def Map<String, String> properties = Collections.emptyMap()

    ScrPluginExtension(Project project) {
        outputDirectory = new File(project.buildDir, "scr-plugin-generated")
        sources = project.sourceSets.main.java
    }
}