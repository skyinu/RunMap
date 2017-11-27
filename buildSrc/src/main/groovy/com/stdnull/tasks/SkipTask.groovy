package com.stdnull

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by chen on 2017/8/17.
 */
class SkipTask extends DefaultTask{
    String group = "simplify"

    @TaskAction
    def skip(){
        if(getProject().tasks.getByName("assembleDebug")){
            disableTaskByName("Release")
        }
        else{
            disableTaskByName("Debug")
        }
    }

    def disableTaskByName(String type){
        getProject().rootProject.allprojects.each {
            it.tasks.findAll {
                if(type == "Release" && !it.name.contains(":app")){
                    return false
                }
                it.name.contains(type)
            }.each {
                it.enabled = false
            }
        }
    }
}
