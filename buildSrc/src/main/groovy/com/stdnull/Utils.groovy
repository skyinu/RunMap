package com.stdnull

import org.gradle.api.Project

/**
 * Created by chen on 2017/8/17.
 */
class Utils {
    static def findNdkLocation(Project project) {
        def sdkDir = null
        def localProperties = project.rootProject.file("local.properties")
        if (localProperties) {
            Properties properties = new Properties()
            localProperties.withInputStream { instr ->
                properties.load(instr)
            }
            sdkDir = properties.getProperty('sdk.dir')
            if (!sdkDir) {
                throw new RuntimeException("No sdk.dir")
            }
        } else {
            sdkDir = System.getProperty("ANDROID_HOME")
        }
        if (sdkDir == null) {
            throw new RuntimeException(
                    "SDK location not found. Define location with sdk.dir in the local.properties file or with an ANDROID_HOME environment variable.")
        }
        return sdkDir + File.separator + "ndk-bundle"
    }

    static def getAppVersionName(Project project){
        def android = project.extensions.findByName("android")
        return android?.defaultConfig?.versionName
    }

    static def getAppVersionCode(Project project){
        def android = project.extensions.findByName("android")
        return android?.defaultConfig?.versionCode
    }
}
