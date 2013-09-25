package be.jlrhome.gradle.scr

import org.gradle.api.Project
import org.gradle.api.Plugin

class ScrPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.task('generateScrDescriptor', type: GenerateDescriptorTask).dependsOn(project.compileJava)
        project.jar.dependsOn 'generateScrDescriptor'
    }
}
