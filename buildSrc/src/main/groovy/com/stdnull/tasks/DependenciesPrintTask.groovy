package com.stdnull

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * Created by chen on 2017/8/19.
 */
class DependenciesPrintTask extends DefaultTask{
    String group = "simplify"

    @TaskAction
    def printDepencies(){
        if(!project.tasks.assemble.didWork) {
            printAllDepencies(project)
            return
        }
        project.tasks.assemble{
            doLast {
                println "after assemble-dependency is "
                printAllDepencies(project)
            }
        }
    }

    def printAllDepencies(Project curProject){
        printDepencies(curProject)
        curProject.childProjects.each {
            printAllDepencies(it)
        }
    }

    def printDepencies(Project item){
        def configurations = item.configurations
        configurations.getNames().each {
            DependencySet dependencies = configurations.getByName(it).dependencies
            if(!dependencies){
                return
            }
            logger.error "project ${item.name}'s dependency: ${it}'s dependency list is "
            dependencies.each {
                logger.error "type is " + it
                FileTree files = it.properties.get("files")
                if(files) {
                    files.files.each {
                        logger.error "- ${it.name}"
                    }
                }
                else if(it instanceof ProjectDependency){
                    logger.error "- ${((ProjectDependency)it).dependencyProject.name}"
                }
                else{
                    logger.error "- $it.group : $it.name : $it.version"
                }

            }
        }
    }
}
