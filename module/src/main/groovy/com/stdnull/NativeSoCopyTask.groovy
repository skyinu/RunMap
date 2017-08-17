package com.stdnull

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by chen on 2017/8/15.
 */
class NativeSoCopyTask extends DefaultTask {
    String group = "simplify";
    @TaskAction
    def copy() {
        println "start copy native so library"
        def sourceNativeLibDir = getProject().rootProject.rootDir.absolutePath + File.separator + "app" +
                File.separator + "src" + File.separator + "main" + File.separator + "libs" +
                File.separator + "armeabi-v7a"
        def destNativeLibDir = getProject().rootProject.rootDir.absolutePath + File.separator + "app" +
                File.separator + "libs" + File.separator + "armeabi-v7a"

        if (checkIfNeedCopylibs("libconfig.so", sourceNativeLibDir, destNativeLibDir)) {
            copyLibs("libconfig.so", sourceNativeLibDir, destNativeLibDir)
            println "copy succeed"
        } else {
            try {
                executeNativeBuild {
                    copyLibs("libconfig.so", sourceNativeLibDir, destNativeLibDir)
                }
            } catch (Exception ex) {
                println "copy failed " + ex
            }
        }
    }

    def executeNativeBuild(Closure callback) {
        String sourceNativeLibDir = getProject().rootProject.rootDir.absolutePath + File.separator + "app" +
                File.separator + "src" + File.separator + "main" + File.separator + "jni"
        String cmdDir = getProject().rootProject.rootDir.absolutePath + File.separator + "gradle"+
                File.separator + "command" + File.separator
        def cmd = ""
        println "cmdDir is "+cmdDir
        if(System.getProperty("os.name").contains("Windows")){
            cmd = "cmd /c " + cmdDir + "sh-ndk-build.bat " + sourceNativeLibDir + " " + Utils.findNdkLocation(getProject())
        }
        else{
            cmd += "./" + cmdDir + "sh-ndk-build-sh " + sourceNativeLibDir + " " + Utils.findNdkLocation(getProject())
        }
        println "cmd is " + cmd
        cmd.execute().waitFor()
        callback()

    }

    def copyLibs(libName, sourceDir, destDir) {
        def sourceFile = new File(sourceDir + File.separator + libName);
        def destFile = new File(destDir + File.separator + libName);
        destFile.withOutputStream { os ->
            sourceFile.withInputStream { ins ->
                os << ins
            }
        }
    }

    def checkIfNeedCopylibs(libName, sourceDir, destDir) {
        def sourceFile = new File(sourceDir + File.separator + libName)
        def destFile = new File(destDir + File.separator + libName)
        println sourceFile.absolutePath + " vs " + destFile.absolutePath
        if(!sourceFile.exists()){
            return false
        }
        if (!destFile.exists()) {
            return true
        }
        return sourceFile.lastModified() > destFile.lastModified()
    }
}
