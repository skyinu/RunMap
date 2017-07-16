# 指示源文件位置,my-dir返回当前Android.mk所在目录
LOCAL_PATH := $(call my-dir)
# 清除临时变量
include $(CLEAR_VARS)
# 声明模块名(库名)
LOCAL_MODULE := config
# 声明依赖源文件
LOCAL_SRC_FILES := AppConfig.cpp
# 构建库
include $(BUILD_SHARED_LIBRARY)