package com.stdnull

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet
import javassist.ClassPath
import javassist.CtMethod
import javassist.bytecode.AccessFlag
import org.apache.commons.io.FileUtils
import com.stdnull.logger.LoggerWrapper
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Created by chen on 2017/11/26.
 */

class MethodCallLogTransform extends Transform {
    Project project
    LoggerWrapper loggerWrapper
    ClassPool classPool
    List<String> classPathList
    File logClassMap


    MethodCallLogTransform(Project project, LoggerWrapper loggerWrapper) {
        this.project = project
        this.loggerWrapper = loggerWrapper
        this.logClassMap = new File(project.buildDir.absolutePath +
                File.separator + "log_mapping.txt")
        if(logClassMap.exists()){
            logClassMap.delete()
            logClassMap.createNewFile()
        }
        project.afterEvaluate {
            handleClassPath()
        }
    }

    def handleClassPath(){
        classPathList = new ArrayList<>()
        if(!project.plugins.hasPlugin("com.android.library")){
            return
        }
        project.android.libraryVariants.all {variant ->
            JavaCompile javaCompile = variant.getJavaCompiler()
            javaCompile.classpath.each {
                classPathList.add(it.path)
            }
        }
    }

    @Override
    String getName() {
        return "methodCallLog"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return ImmutableSet.of(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        if (project.plugins.hasPlugin("com.android.library")) {
            return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
        }
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        classPool = new ClassPool()
        classPool.appendSystemPath()
        loggerWrapper.error("method call log transform run")
        classPool.insertClassPath(project.android.bootClasspath[0].toString())
        List<ClassPath> classPaths = new ArrayList<>()
        classPathList.each {
            try {
                classPaths.add(classPool.insertClassPath(it))
            }
            catch (Exception e){
                loggerWrapper.error(e.toString())
            }
        }

        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        outputProvider.deleteAll()
        transformInvocation.inputs.each {
            it.jarInputs.each {
                File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, out)
                classPaths.add(classPool.appendClassPath(out.path))
            }
            it.directoryInputs.each {
                File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, out)
                classPaths.add(classPool.insertClassPath(out.path))
                handleDirectory(out)
            }
        }
        classPaths.each {
            classPool.removeClassPath(it)
        }
        classPool = null
    }

    def handleDirectory(File input) {
        input.eachFileRecurse {
            if (!it.absolutePath.endsWith(".class")) {
                return
            }
            def className = it.absolutePath.substring(input.absolutePath.length() + 1).replace(File.separator, ".").replace(".class", "")
            CtClass ctClass = classPool.getOrNull(className)
            if (shouldIgnoreClass(ctClass)) {
                return
            }
            logClassMap.append("class ->" + input.absolutePath + " " + className + "\n")
            ctClass.getDeclaredMethods().each {
                if (shouldIgnoreMethod(it)) {
                    return
                }
                String log = "android.util.Log.i(\"Method_Log\",\"${ctClass.name}->${it.name}\");"
                it.insertBefore(log)
            }
            ctClass.writeFile(input.absolutePath + File.separator)
        }
    }

    def shouldIgnoreClass(CtClass ctClass) {
        if(ctClass == null){
            return true
        }
        if (ctClass.interface || ctClass.isFrozen()) {
            return true
        }
        if (!ctClass.declaredMethods || ctClass.declaredMethods.length < 1) {
            return true
        }
        return false
    }

    def shouldIgnoreMethod(CtMethod ctMethod) {
        if (ctMethod.empty) {
            return true
        }
        if ((ctMethod.modifiers & AccessFlag.NATIVE) != 0) {
            return true
        }
        return false
    }

    @Override
    boolean isIncremental() {
        return false
    }
}