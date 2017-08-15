package com.stdnull

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by chen on 2017/8/15.
 */
class SimplifyPluginMain implements Plugin<Project> {
    Project mProjectContext
    @Override
    void apply(Project project) {
        this.mProjectContext = project
        println "Hello simplify"
        addNativeSoCopyTask()
    }

    def addNativeSoCopyTask(){
        def nativeSoCopyTask = mProjectContext.task("NativeSoCopyTask", type:NativeSoCopyTask)
        def preBuildTask = mProjectContext.getTasks().getByName("preBuild")
        preBuildTask.dependsOn(nativeSoCopyTask)
    }
}
