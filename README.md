gradle-scr-plugin
=================

Generates the OSGI-INF/serviceComponent.xml based on the Apache scr annotations


How To
======
Before using the gradle-scr-plugin, the plugin itself must be available in your local repository so that it is accessible from other scripts.
To do this pull out the gradle-scr-plugin source code and run gradle install.

    git clone https://github.com/janvolck/gradle-scr-plugin.git
    cd gradle-scr-plugin
    gradle install

Now that thet gradle-scr-plugin is installed in your local repository you can use the plugin by adding a buildscript dependency
of the gradle-scr-plugin to your build.grade script

e.g.:

    buildscript {
          dependencies {
              classpath 'be.jlrhome.gradle.scr:scrPlugin:0.0.+'
          }
      }

Once the buildscript is added, apply the scr plugin:

e.g.:

    apply plugin: 'scr'


Troubleshooting
================
* If the gradle-scr-plugin throws an exception like: Could not create an instance of type be.jlrhome.gradle.scr.ScrPluginExtension_Decorated.
  Then make sure you also apply the java plugin :

```groovy
    apply plugin: 'java'
```

* Could not resolve all dependencies for configuration ':classpath'. Could not find any version that matches be.jlrhome.gradle.scr:scrPlugin:0.0.+.

The gradle scrPlugin is not available on a public repository. Before using the scrPlugin ensure that the scrPlugin is installed in you local repository.



Simple Example
==============

    buildscript {
        dependencies {
            classpath 'be.jlrhome.gradle.scr:scrPlugin:0.0.+'
        }
    }

    apply plugin: 'java'
    apply plugin: 'osgi'
    apply plugin: 'scr'
    apply plugin: 'groovy'
    apply plugin: 'maven'

    sourceCompatibility = 1.6
    targetCompatibility = 1.6

    version = '0.0.1-SNAPSHOT'
    group = 'be.jlrhome.gradle.scr'
    description = 'Gradle scr example'

    dependencies {
        compile 'org.osgi:org.osgi.compendium:5.0.0'
    // or
        compile 'org.osgi:org.osgi.compendium:4.3.+'
    // or
        compile 'org.apache.felix:org.apache.felix.scr.annotations:1.9.+'
    }
