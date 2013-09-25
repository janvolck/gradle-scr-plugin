package be.jlrhome.gradle.scr

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.apache.felix.scrplugin.SCRDescriptorGenerator
import org.apache.felix.scrplugin.Options
import org.apache.felix.scrplugin.Project
import org.apache.felix.scrplugin.SpecVersion
import org.apache.felix.scrplugin.Source
import org.codehaus.plexus.util.FileUtils
import org.codehaus.plexus.util.StringUtils

class GenerateDescriptorTask extends DefaultTask {

    Project createProject(){

        def scrProject = new Project()
        def dependenciesAsUrl = new ArrayList<URL>()
        def sources = new ArrayList<Source>()

        project.configurations.compile.resolvedConfiguration.getResolvedArtifacts().each { artifact ->
            def f = artifact.getFile()
            dependenciesAsUrl.add(f.toURI().toURL())
            project.logger.info("dependency add: {}", f)
        }
        dependenciesAsUrl.add(new File("build/classes/main").toURI().toURL())

        project.sourceSets.main.java.each{File f ->

            def className = f.getAbsolutePath()

            // strip the source directory from the classname
            project.sourceSets.main.java.getSrcDirTrees().each{ t ->
                if(className.startsWith(t.getDir().getAbsolutePath())){
                    className = className.substring(t.getDir().getAbsolutePath().length()+1)
                }
            }

            // strip extension
            className = StringUtils.stripEnd(className, ".java");

            // change path seperators to .
            className = StringUtils.replace(className, '\\', '/');
            className = StringUtils.replace(className, '/', '.');

            project.logger.debug("Source [{}, {}]",className, f.toString())

            def s = [ getClassName: { return className }, getFile: {return f} ] as Source
            sources.add(s);
        }


        scrProject.setClassLoader(new URLClassLoader((URL[])dependenciesAsUrl.toArray(), this.getClass().getClassLoader()))
        scrProject.setDependencies(dependenciesAsUrl)
        scrProject.setSources(sources)
        scrProject.setClassesDirectory('build/classes/main')

        return scrProject
    }

    Options createOptions() {

        def scrOptions = new Options()
        scrOptions.setOutputDirectory(new File("build/scr"))
        scrOptions.setGenerateAccessors(true)
        scrOptions.setStrictMode(false)
        scrOptions.setProperties(new HashMap<String, String>())
        scrOptions.setSpecVersion(SpecVersion.fromName("1.2"))

        return scrOptions
    }

    @TaskAction
    def generateScrDescriptor() {

        SCRDescriptorGenerator scrGenerator = new SCRDescriptorGenerator(new GradleLog(project.logger))
        scrGenerator.setOptions(createOptions())
        scrGenerator.setProject(createProject())

        scrGenerator.execute()
    }
}