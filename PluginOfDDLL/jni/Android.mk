LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ddll
LOCAL_SRC_FILES := main.cpp GameCheater.cpp MonoHooker.cpp
LOCAL_LDLIBS +=  -llog -ldl -lc
LOCAL_MODULE_CLASS := SHARED_LIBRARIES

include $(BUILD_SHARED_LIBRARY)
