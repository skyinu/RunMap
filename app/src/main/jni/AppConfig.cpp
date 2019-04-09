//
// Created by chen on 2017/7/16.
//
#include "com_stdnull_runmap_common_AppConfig.h"
/*
 * Class:     com_stdnull_runmap_common_AppConfig
 * Method:    getWeixinAppId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_stdnull_runmap_common_AppConfig_getWeixinAppId
  (JNIEnv *env, jclass){
  char* tmpstr = "wx80d09d1ee6a69b27";
  jstring restr = env->NewStringUTF(tmpstr);
    return restr;
  }

/*
 * Class:     com_stdnull_runmap_common_AppConfig
 * Method:    getDbBackupBaseUrl
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_stdnull_runmap_common_AppConfig_getDbBackupBaseUrl
  (JNIEnv *env, jclass){
    char* tmpstr = "http://192.168.18.12:8888";
    jstring restr = env->NewStringUTF(tmpstr);
    return restr;
  }

/*
 * Class:     com_stdnull_runmap_common_AppConfig
 * Method:    runCheck
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_stdnull_runmap_common_AppConfig_runCheck
  (JNIEnv *env, jclass){
  jboolean tRet = false;
  char* tmpstr = NULL;
  tRet = tmpstr[3] > 1;
  return  tRet;
 }
