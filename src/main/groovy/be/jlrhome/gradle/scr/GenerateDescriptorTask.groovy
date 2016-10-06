package be.jlrhome.gradle.scr

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.apache.felix.scrplugin.SCRDescriptorGenerator
import org.apache.felix.scrplugin.Options
import org.apache.felix.scrplugin.Project
import org.apache.felix.scrplugin.SpecVersion
import org.apache.felix.scrplugin.Source
import org.apache.felix.scrplugin.Result

class GenerateDescriptorTask extends DefaultTask {

    Project createProject(){

        def scrProject = new Project()
        def dependenciesAsUrl = new ArrayList<URL>()
        def dependenciesAsFile = new ArrayList<File>()
        def sources = new ArrayList<Source>()

        project.configurations.compileClasspath.resolvedConfiguration.getResolvedArtifacts().each { artifact ->
            def f = artifact.getFile()
            dependenciesAsFile.add(f)
            dependenciesAsUrl.add(f.toURI().toURL())
            project.logger.info("dependency add: {}", f)
        }

        dependenciesAsFile.add(project.sourceSets.main.output.classesDir)
        dependenciesAsUrl.add(project.sourceSets.main.output.classesDir.toURI().toURL())
        project.scr.sources.each{File f ->

            def className = f.getAbsolutePath()
            project.logger.debug("Process {}",className)

            // strip the source directory from the classname
            project.scr.sources.getSrcDirTrees().each{ t ->
                if(className.startsWith(t.getDir().getAbsolutePath())){
                    className = className.substring(t.getDir().getAbsolutePath().length()+1)
                    project.logger.debug(" --> Remove source directory new ClassName : {}",className)
                }
            }

            // strip extension
            if(className.endsWith(".java")){
                className = className.substring(0, className.length()-".java".length())
                project.logger.debug(" --> Remove extension new ClassName : {}",className)
            }

            // change path seperators to .
            className = className.replace('\\', '/');
            className = className.replace('/', '.');
            project.logger.debug(" --> Change path seperators new ClassName : {}",className)

            project.logger.debug("Source [{}, {}]",className, f.toString())

            def s = [ getClassName: { return className }, getFile: {return f} ] as Source
            sources.add(s);
        }


        scrProject.setClassLoader(new URLClassLoader((URL[])dependenciesAsUrl.toArray(), this.getClass().getClassLoader()))
        scrProject.setDependencies(dependenciesAsFile)
        scrProject.setSources(sources)
        scrProject.setClassesDirectory(project.sourceSets.main.output.classesDir.getAbsolutePath())

        return scrProject
    }

    Options createOptions() {

        def scrOptions = new Options()
        scrOptions.setOutputDirectory(project.scr.outputDirectory)
        scrOptions.setGenerateAccessors(project.scr.generateAccessors)
        scrOptions.setStrictMode(project.scr.strictMode)
        scrOptions.setProperties(project.scr.properties)
        scrOptions.setSpecVersion(SpecVersion.fromName(project.scr.specVersion))

        return scrOptions
    }

    def updateManifest(Result scrResult, Options scrOptions) {

        project.logger.debug("Processing SCR result")
        def osgiInfDir = new File(scrOptions.getOutputDirectory(), "OSGI-INF")
        if(osgiInfDir.exists()) {
            project.logger.debug(" --> OSGI-INF found at : {}", osgiInfDir)

            final Set<String> serviceFiles = new HashSet<String>();

            // keep existing Service-Component tag in manifest
            String sc = project.jar.manifest.instructions.get("Service-Component")
            if(sc != null){
                project.logger.debug(" --> Service-Component found in manifest with value {}", sc)
                final StringTokenizer st = new StringTokenizer(sc, ",")
                while ( st.hasMoreTokens() ) {
                    final String token = st.nextToken();
                    serviceFiles.add(token.trim());
                }
            }

            project.fileTree(dir: project.file(osgiInfDir), includes:['*.xml']).each{ f ->

                project.logger.debug(" --> Adding Service-Component xml {}", f.getName())
                serviceFiles.add("OSGI-INF/" + f.getName())
            }

            boolean first = true
            StringBuilder sb = new StringBuilder()
            serviceFiles.each{ service ->
                if(!first) {
                    sb.append(", ")
                } else {
                    first = false;
                }

                sb.append(service)
            }

            project.logger.debug(" --> Updating Service-Component value with {}", sb.toString())
            project.jar.manifest.instruction("Service-Component", sb.toString())
            project.jar.from(project.fileTree(dir: scrOptions.getOutputDirectory(), includes:["OSGI-INF/*"]))
        }
    }

    @TaskAction
    def generateScrDescriptor() {

        Options scrOptions = createOptions()
        Project scrProject = createProject()

        SCRDescriptorGenerator scrGenerator = new SCRDescriptorGenerator(new GradleLog(project.logger))
        scrGenerator.setOptions(scrOptions)
        scrGenerator.setProject(scrProject)

        Result result = scrGenerator.execute()
        updateManifest(result, scrOptions)
    }
}