package com.stdnull

import com.stdnull.logger.LoggerWrapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration

/**
 * Created by chen on 2017/8/15.
 */
class SimplifyPluginMain implements Plugin<Project> {
    Project mProjectContext
    SimplifyExtension simplifyExtension
    LoggerWrapper loggerWrapper
    static final String CUSTOM_DEPENDENCE = "mc"

    @Override
    void apply(Project project) {
        this.mProjectContext = project
        project.extensions.create("simplify", SimplifyExtension)
        loggerWrapper = new LoggerWrapper(project.logger)
        loggerWrapper.info("Hello simplify, project is ${project.name}")
        Configuration mcConfiguration = project.configurations.create(CUSTOM_DEPENDENCE)
        project.gradle.addListener(new McDependenceResolveListener(mProjectContext, mcConfiguration))
        project.android.registerTransform(new MethodCallLogTransform(project, loggerWrapper))
        addNativeSoCopyTask()
        addSkipTask()
        addPrintDenpenciesTask()
        project.afterEvaluate {
            hookProguardResult()
            initExtension()
            configuration()
        }
    }

    def initExtension(){
        simplifyExtension = mProjectContext.extensions.findByName("simplify") as SimplifyExtension
    }

    def configuration(){
        if(!simplifyExtension?.skipEnable){
            def skipTask = mProjectContext.tasks.findByName("SkipTask")
            skipTask?.enabled = false
        }
    }

    def hookProguardResult(){
        def extractProguardRelease = mProjectContext.getTasks().findByName("transformClassesAndResourcesWithProguardForRelease")
        def extractProguardDebug = mProjectContext.getTasks().findByName("transformClassesAndResourcesWithProguardForDebug")

        extractProguardRelease?.doLast {
            copyMapping(it)
        }
        extractProguardDebug?.doLast {
            copyMapping(it)
        }
    }

    def copyMapping(Task task){
        def buildType = "release"
        if(task.name.toLowerCase().contains("debug")){
            buildType = "debug"
        }
        def mapFilePath = "${mProjectContext.buildDir.absolutePath}${File.separator}outputs" +
                "${File.separator}mapping${File.separator}${buildType}${File.separator}mapping.txt"
        def mapFile = new File(mapFilePath)
        if(!mapFile.exists()){
            mProjectContext.logger.error "${task.name} proguard mapping file ${mapFilePath} is not exist"
            return
        }
        def destName = "mapping${buildType}${Utils.getAppVersionName(mProjectContext)}.txt"
        def destPath = "${mProjectContext.rootProject.rootDir.absolutePath}${File.separator}readme${File.separator}$destName"
        File destFile = new File(destPath)
        destFile.createNewFile()
        destFile.withOutputStream { os ->
            mapFile.withInputStream { ins ->
                os << ins
            }
        }
    }

    def addNativeSoCopyTask() {
        def nativeSoCopyTask = mProjectContext.task("NativeSoCopyTask", type: NativeSoCopyTask)
        def preBuildTask = mProjectContext.getTasks().getByName("preBuild")
        preBuildTask.dependsOn(nativeSoCopyTask)
    }

    def addSkipTask() {
        def skipTask = mProjectContext.task("SkipTask", type: SkipTask)
        def preBuildTask = mProjectContext.getTasks().getByName("preBuild")
        preBuildTask.dependsOn(skipTask)
    }

    def addPrintDenpenciesTask() {
        def dependenciesPrintTask = mProjectContext.task("DependenciesPrintTask", type: DependenciesPrintTask)
        def preBuildTask = mProjectContext.getTasks().getByName("preBuild")
        preBuildTask.dependsOn(dependenciesPrintTask)
    }
}
