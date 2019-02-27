# javapackager

Introduced with Java 8, and that's the only valid versions, since in Java 11 it's already retired.

I'll try to use Oracle Java 8.

https://linuxconfig.org/how-to-install-java-on-ubuntu-18-10-cosmic-cuttlefish-linux

    $ sudo add-apt-repository ppa:webupd8team/java
    $ sudo apt install openjdk-8-jdk
    $ sudo apt install oracle-java8-set-default
    $ java -version
    java version "1.8.0_201"
    Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
    Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)

https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javapackager.html

    $ javapackager
      Usage: javapackager -command [-options]
      
      where command is one of: 
        -createjar
                The packager produces jar archive according to other parameters. 
        -deploy 
                The packager generates the jnlp and html files according to other
                parameters.
        -createbss
                Converts css file into binary form 
        -signJar
                Signs jar file(s) with a provided certificate.
        -makeall
                Performs compilation, createjar and deploy steps as one call with 
                most arguments predefined. The sources must be located in "src"
                folder, the resulting files (jar, jnlp, html) are put in "dist"
                folder. This command may be configured only in a minimal way and is
                as automated as possible.
      
      Options for createjar command include: 
        -appclass <application class>
                qualified name of the application class to be executed.
        -preloader <preloader class>
                qualified name of the preloader class to be executed.
        -paramfile <file>
                properties file with default named application parameters.
        -argument arg
                An unnamed argument to be put in <fx:argument> element in the JNLP
                file.
        -classpath <files>
                list of dependent jar file names.
        -manifestAttrs <manifest attributes>
                List of additional manifest attributes. Syntax: "name1=value1,
                name2=value2,name3=value3.
        -noembedlauncher 
                If present, the packager will not add the JavaFX launcher classes
                to the jarfile.
        -nocss2bin
                The packager won't convert CSS files to binary form before copying
                to jar. 
        -runtimeversion <version> 
                version of the required JavaFX Runtime.
        -outdir <dir>
                name of the directory to generate output file to.
        -outfile <filename>
                The name (without the extension) of the resulting file.
        -srcdir <dir>
                Base dir of the files to pack.
        -srcfiles <files>
                List of files in srcdir. If omitted, all files in srcdir (which
                is a mandatory argument in this case) will be packed.
      
      Options for deploy command include:
        -title <title>
                title of the application.
        -vendor <vendor>
                vendor of the application.
        -description <description>
                description of the application.
        -appclass <application class>
                qualified name of the application class to be executed.
        -preloader <preloader class>
                qualified name of the preloader class to be executed.
        -paramfile <file>
                properties file with default named application parameters.
        -htmlparamfile <file>
                properties file with parameters for the resulting applet.
        -width <width>
                width of the application.
        -height <height>
                height of the application.
        -native <type>
                generate self-contained application bundles (if possible).
                If type is specified then only bundle of this type is created.
                List of supported types includes: installer, image, exe, msi, dmg, rpm, deb.
        -name <name>
                name of the application.
        -embedjnlp
                If present, the jnlp file will be embedded in the html document.
        -embedCertificates
                If present, the certificates will be embedded in the jnlp file.
        -allpermissions
                If present, the application will require all security permissions 
                in the jnlp file.
        -updatemode <updatemode>
                sets the update mode for the jnlp file.
        -isExtension
                if present, the srcfiles are treated as extensions.
        -callbacks
                specifies user callback methods in generated HTML. The format is
                "name1:value1,name2:value2,..."
        -templateInFilename
                name of the html template file. Placeholders are in form of
                #XXXX.YYYY(APPID)#
        -templateOutFilename
                name of the html file to write the filled-in template to.
        -templateId
                Application ID of the application for template processing.
        -argument arg
                An unnamed argument to be put in <fx:argument> element in the JNLP
                file.
        -outdir <dir>
                name of the directory to generate output file to.
        -outfile <filename>
                The name (without the extension) of the resulting file.
        -srcdir <dir>
                Base dir of the files to pack.
        -srcfiles <files>
                List of files in srcdir. If omitted, all files in srcdir (which
                is a mandatory argument in this case) will be used.
      
      Options for createbss command include:
        -outdir <dir>
                name of the directory to generate output file to.
        -srcdir <dir>
                Base dir of the files to pack.
        -srcfiles <files>
                List of files in srcdir. If omitted, all files in srcdir (which
                is a mandatory argument in this case) will be used.
      
      Options for signJar command include:
        -keyStore <file>
                Keystore filename.
        -alias 
                Alias for the key.
        -storePass
                Password to check integrity of the keystore or unlock the keystore.
        -keyPass
                Password for recovering the key.
        -storeType
                Keystore type, the default value is "jks".
        -outdir <dir>
                name of the directory to generate output file(s) to.
        -srcdir <dir>
                Base dir of the files to signed.
        -srcfiles <files>
                List of files in srcdir. If omitted, all files in srcdir (which
                is a mandatory argument in this case) will be signed.
      
      Options for makeAll command include:
        -appclass <application class>
                qualified name of the application class to be executed.
        -preloader <preloader class>
                qualified name of the preloader class to be executed.
        -classpath <files>
                list of dependent jar file names.
        -name <name>
                name of the application.
        -width <width>
                width of the application.
        -height <height>
                height of the application.
        -v      enable verbose output.
      
      Sample usages:
      --------------
      javapackager -createjar -appclass package.ClassName  
        -srcdir classes -outdir out -outfile outjar -v
                Packages the content of the classes directory to outjar.jar,
                sets the application class to package.ClassName.
      javapackager -deploy -outdir outdir -outfile outfile -width 34 -height 43 
        -name AppName -appclass package.ClassName -v -srcdir compiled
                Generates outfile.jnlp and corresponding outfile.html files in 
                outdir for aplication AppName that is started by package.ClassName
                class and has dimensions of 34x43.
      javapackager -makeall -appclass brickbreaker.Main -name BrickBreaker
        -width 600 -height 600
                This command does all the packaging work including compilation: 
                compile, createjar, deploy.
                
For this project:

Generates OUT.jar containing my classes:

    $ javapackager -createjar -appclass cat.inspiracio.calculator.Calculator -srcdir target/scala-2.12/classes -outdir target -outfile OUT -v -classpath target/universal/stage/lib/org.scala-lang.scala-library-2.12.8.jar -name ComplexCalculator

with MANIFEST.MF:

    Manifest-Version: 1.0
    Permissions: sandbox
    JavaFX-Version: 8.0
    Class-Path: target/universal/stage/lib/org.scala-lang.scala-library-2.12.8.jar
    Created-By: JavaFX Packager
    Main-Class: cat.inspiracio.calculator.Calculator

But that's no good: the scala runtime is outside.

Makes target/bundle/*.deb:
    
    $ javapackager -deploy -appclass cat.inspiracio.calculator.Calculator -native deb -outdir target -outfile BLA -srcdir target -srcfiles OUT.jar -v

Does not work.
Contains a JRE but doesn't find Calculator main class. Doesn't seem to contain scala runtime.