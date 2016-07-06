
APP_STL :=stlport_static

APP_CPPFLAGS += -fpermissive  #此项有效时表示宽松的编译形式，比如没有用到的代码中有错误也可以通过编译；

ifeq ($(NDK_DEBUG), 1)
APP_CPPFLAGS += -DNDK_DEBUG
else
APP_CPPFLAGS += -fvisibility=hidden -O3
endif

APP_ABI := armeabi #armeabi-v7a

LOCAL_ARM_MODE := arm

APP_PLATFORM=android-5