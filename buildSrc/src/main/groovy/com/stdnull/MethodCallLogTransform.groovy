import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet
import javassist.bytecode.AccessFlag
import org.apache.commons.io.FileUtils
import com.stdnull.logger.LoggerWrapper
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import org.apache.tools.ant.util.StringUtils
import org.gradle.api.Project
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * Created by chen on 2017/11/26.
 */

class MethodCallLogTransform extends Transform{
    Project project
    LoggerWrapper loggerWrapper
    ClassPool classPool = ClassPool.getDefault()
    File logClassMap


    MethodCallLogTransform(Project project, LoggerWrapper loggerWrapper){
        this.project = project
        this.loggerWrapper = loggerWrapper
        this.logClassMap = new File(project.buildDir.absolutePath + File.separator + "log_mapping.txt")
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
        if(project.plugins.hasPlugin("com.android.library")){
            loggerWrapper.error("library plugin")
            return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
        }
//        loggerWrapper.error("application plugin")
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        loggerWrapper.error("method call log transform run")
        classPool.appendSystemPath()
        classPool.insertClassPath(project.android.bootClasspath[0].toString())

        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if(transformInvocation.isIncremental()){
            outputProvider.deleteAll()
        }
        transformInvocation.inputs.each {
            it.jarInputs.each {
                File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, out)
                classPool.appendClassPath(out.path)
            }
            it.directoryInputs.each {
                File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, out)
                classPool.insertClassPath(out.path)
                handleDirectory(out)
                loggerWrapper.error("output is " + out.absolutePath)
            }
        }
    }

    def handleDirectory(File input){
        logClassMap.append ("directory ->"+ input.absolutePath +"\n")
        input.eachFileRecurse {
            if(!it.absolutePath.endsWith(".class")){
                return
            }
            def className = it.absolutePath.substring(input.absolutePath.length()+1).replace("\\",".").replace(".class","")
            CtClass ctClass
            try {
                ctClass = classPool.getCtClass(className)
            }
            catch (Exception e){
                return
            }
            if(shouldIgnore(ctClass)){
                return
            }
            logClassMap.append ("class ->"+ input.absolutePath + " " + className + "\n")
            ctClass.getDeclaredMethods().each {

                if(it.empty){
                    loggerWrapper.error("method empty" + it.name )
                    return
                }
                if((it.modifiers & AccessFlag.NATIVE) != 0){
                    loggerWrapper.error("method native" + it.name )
                    return
                }
                String log = "android.util.Log.i(\"Method_Log\",\"${ctClass.name}->${it.name}\");"
                it.insertBefore(log)
//                loggerWrapper.error("class "+  ctClass.name)
            }
//            ctClass.writeFile()
            ctClass.writeFile(input.absolutePath + File.separator)
        }
    }

    def shouldIgnore(CtClass ctClass){
        if(ctClass.interface || ctClass.isFrozen()){
            return true
        }
        if(!ctClass.declaredMethods || ctClass.declaredMethods.length < 1){
            return true
        }
        return false
    }


    @Override
    boolean isIncremental() {
        return false
    }
}