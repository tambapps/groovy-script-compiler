# Groovy script compiler

This app allows you to compile a Groovy script (along with it's dependencies) into an executable jar, an appimage or an executable native binary.

It will compile your script into a `.class` file, fetch the dependencies (including groovy) and build
an executable jar. With this jar, it can convert it into an appimage using [jpackage](https://docs.oracle.com/en/java/javase/14/docs/specs/man/jpackage.html) or into a 
native binary executable using [native-image](https://www.graalvm.org/reference-manual/native-image/).

## How to use
Provide an input file to convert it into an executable jar 
(jar with all the required dependencies), an appimage, or a native binary executable. 

Note that if your script use classes from Groovy subprojects (e.g. JsonSlurper from groovy-json) 
you'll have to tell the program, so that the classes are included in the generated output. If it uses
other non-groovy libraries, you can also provide the dependencies jars with the `-a` argument.

```text
Usage: <main class> [-dh] [-j=<jpackageFile>] [-m=<mainClass>]
                    [-n=<nativeImageFile>] [-o=<outputDir>] [-t=<outputType>]
                    [-v=<version>] [-a=<additionalJars>[,
                    <additionalJars>...]]... [-s=<subProjects>[,
                    <subProjects>...]]... inputFile
      inputFile             The script file to compile (can be an executable
                              jar instead for appimage and native-binary)
  -a, --additional-jars=<additionalJars>[,<additionalJars>...]
                            Comma-separated list of Additional jars. E.g. if
                              your script use a non Groovy library, it would be
                              the jar of the library
  -d, --debug               Display more logs
  -h, --help                display usage
  -j, --jpackage-path=<jpackageFile>
                            Path of jpackage. Only useful for 'appimage' type
  -m, --main-class=<mainClass>
                            Main class. Defaults to the input file's name
  -n, --native-image-path=<nativeImageFile>
                            Path of native-image (graalvm). Only useful for
                              'native-binary' type
  -o, --output-dir=<outputDir>
                            Folder in which to put output file. Defaults to
                              current directory
  -s, --groovy-subprojects=<subProjects>[,<subProjects>...]
                            Comma-separated list of Groovy subprojects to
                              include in the jar
  -t, --type=<outputType>   The type of output to create
                              Default: JAR
  -v, --version=<version>   Groovy version to use
                              Default: 3.0.9
```

for example

```shell
java -jar groovyjar.jar -o=native-binary -s=json,templates HelloWorld.groovy
```

### Generate a JAR
This is the default output type. You just have to provide the input script and the compilation can begin
The generated JAR is executable from a JVM, you don't even need to have Groovy installed for that.

### Generate an appimage
You'll need to a have JDK 14+ installed. This tool will try to find one
automatically but if it doesn't succeed, you can still provide the path of your jpackage binary with the `-j` argument

### Generate a native-image (native binary executable)
You'll need to have graalvm + native-image installed.

Note that there are a some restrictions for you script to work in binary executable. For example, you can't
use Groovy dynamic feature, as the compilation needs to be static. Also, you shouldn't do much Java reflection.

If the app doesn't find `native-image`, you can provide the path of it with the `-n` argument.

### @Grab dependencies

If your script contains `@Grab` annotations, it will not include them when compiling the script. The grabbed
dependencies will be fetched and included in the jar instead, so that your script runs faster at runtime.

Note that the grab annotation needs to be like in the below example
```groovy
@Grab('org.springframework:spring-orm:3.2.5.RELEASE')
```

not the one with `group=..., module=..., version=...`.