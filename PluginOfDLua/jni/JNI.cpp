

#include "hookutils.h"

  #define VICTIM_LIB "libulua.so"
                                      //libAndroidGame.so

//extern void doProcessCheat(int flag, int arg1, int arg2);
//JNIEXPORT void JNICALL jniDoProcessCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
//	doProcessCheat(flag, arg1, arg2);
//}

dlopen_callback gCallback;

static const char *LoadingLibrary(const char *filename){

	return filename;
}


int my_stat(const char *filename,struct stat* st) {
   // LogD("<%s> filename=%s", __FUNCTION__,filename);
//	if(strstr(filename,"com.gamevil.kritikamobile.android.google.global.normal"))
	//{
		 LogD("<%s> filename=%s", __FUNCTION__,filename);
		//filename="file does not exits";
//	}
	//filename="file does not exits";
	int ret = stat(filename,st);
	// #ifdef NDK_DEBUG
	//if(strstr(filename, "libGangstar4.so") != 0){
	//LogD("<%s> %s", __FUNCTION__,filename);
	//}
	// #endif
	return ret;
}

static void LibraryLoaded(const char *filename, void *handle){
 //LogD("<%s> filename=%s ", __FUNCTION__,filename);
	if((strstr(filename, VICTIM_LIB) && handle)){
		unregistDlopen(gCallback);
		soinfo *info = (soinfo*)handle;
		hook_address(info->base);
		hook_symbols(info);
	//LogD("<%s> filename=%s base=%d ", __FUNCTION__,filename,info->base);

	}
	return;
}

void hookPrepare(){
	void *handle = findLoadedLib(VICTIM_LIB);
	if(handle){
		soinfo *info = (soinfo*)handle;
		hook_address(info->base);
		hook_symbols(info);
	}else{
		gCallback.onLoadingLibrary = LoadingLibrary;
		gCallback.onLibraryLoaded = LibraryLoaded;
		registDlopen(gCallback);
	 //LogD("<%s> r0=%s", __FUNCTION__,"CaLLback:+++++++++++++++++++++++++");
	}
}

//static const JNINativeMethod gHookMethods[] = {
//	{ "nativeDoCheat", "(III)V", (void*)jniDoProcessCheat },
//};

extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved){

	#ifdef NDK_DEBUG
	LogD("<%s> libA8 %s %s", __FUNCTION__, __DATE__, __TIME__);
	#endif

	JNIEnv* env = NULL;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}

	/**
	 * JNI_OnLoad 函数相当于main函数。当System.loadLibrary加载so类库文件时自动执行。
	 * */

//	jclass clazz;
//
//	clazz = env->FindClass("com/gameassist/plugin/nativeutils/NativeUtils");
//	if (clazz == NULL) {
//		return -1;
//	}

//	if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
//		return -1;
//	}
	hookPrepare();
	return JNI_VERSION_1_4;
}
