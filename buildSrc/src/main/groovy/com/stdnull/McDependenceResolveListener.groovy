package com.stdnull

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.ResolvableDependencies

/**
 * Created by chen on 2017/8/22.
 */
class McDependenceResolveListener implements DependencyResolutionListener{
    Project project
    Configuration mc
    McDependenceResolveListener(Project project, Configuration mc){
        this.project = project
        this.mc = mc
    }
    @Override
    void beforeResolve(ResolvableDependencies resolvableDependencies) {
        mc.dependencies.collect {
            if (it instanceof ProjectDependency) {
                def moduleName = ((ProjectDependency) it).dependencyProject.name
                Configuration implementation = project.configurations.getByName("implementation")
                project.logger.error("-" + it.group + " " + moduleName)
                implementation.dependencies.add(project.dependencies.create(project.rootProject.childProjects.get(moduleName)))
            } else {
                project.logger.error("-" + it.group + ":" + it.name + ":" + it.version)
                Configuration implementation = project.configurations.getByName("implementation")
                implementation.dependencies.add(project.dependencies.create(it.group + ":" + it.name + ":" + it.version))
            }
        }
        project.gradle.removeListener(this)
    }

    @Override
    void afterResolve(ResolvableDependencies resolvableDependencies) {

    }
}
