package com.stdnull


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.FileCollectionDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.ResolvableDependencies

/**
 * Created by chen on 2017/8/15.
 */
class SimplifyPluginMain implements Plugin<Project> {
    Project mProjectContext
    static final String CUSTOM_DEPENDENCE = "mc"

    @Override
    void apply(Project project) {
        this.mProjectContext = project
        println "Hello simplify, project is " + project.name
        Configuration mcConfiguration = project.configurations.create(CUSTOM_DEPENDENCE)
        project.gradle.addListener(new McDependenceResolveListener(mProjectContext, mcConfiguration))

        addNativeSoCopyTask()
        addSkipTask()
        addPrintDenpenciesTask()
    }

    def addNativeSoCopyTask() {
        def nativeSoCopyTask = mProjectContext.task("NativeSoCopyTask", type: NativeSoCopyTask)
        def preBuildTask = mProjectContext.getTasks().getByName("preBuild")
        preBuildTask.dependsOn(nativeSoCopyTask)
    }

    def addSkipTask() {
        if(!mProjectContext.hasProperty("skip.enable") || !properties.get("skip.enable")){
            println "skip task is disable"
            return
        }
        println "skip task is enable"
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
