gradle-scr-plugin
=================

Generates the OSGI-INF/serviceComponent.xml based on the Apache scr annotations


How To
======
To use this plugin add a buildscript dependency of the gradle-scr-plugin to your build.grade script
e.g.:

    buildscript {
          dependencies {
              classpath 'be.jlrhome.gradle.scr:scrPlugin:0.1.+'
          }
      }

Once the buildscript is added, apply the scr plugin:

e.g.:

    apply plugin: 'scr'


Build it yourself
=================
To build the scr plugin yourself and install it in your local repository clone the gradle-scr-plugin repository and run gradle install.

    git clone https://github.com/janvolck/gradle-scr-plugin.git
    cd gradle-scr-plugin
    gradle install

Now the gradle-scr-plugin that you build is installed in your local repository and will be used in your build.gradle scripts.


Troubleshooting
================
* If the gradle-scr-plugin throws an exception like: Could not create an instance of type be.jlrhome.gradle.scr.ScrPluginExtension_Decorated.
  Then make sure you also apply the java plugin :

```groovy
    apply plugin: 'java'
```

Simple Example
==============

    buildscript {
        dependencies {
            classpath 'be.jlrhome.gradle.scr:scrPlugin:0.1.+'
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
