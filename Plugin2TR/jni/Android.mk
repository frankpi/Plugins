LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := pgodmode
LOCAL_SRC_FILES := JNI.cpp hookAddress.cpp hookSymbol.cpp 
LOCAL_LDLIBS +=  -llog -ldl -lc
LOCAL_MODULE_CLASS := SHARED_LIBRARIES

include $(BUILD_SHARED_LIBRARY)
