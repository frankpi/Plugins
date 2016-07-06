#include "hookutils.h"
#include "GameCheater.h"


void JNICALL jniProcessCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
	doUserCheatAction(flag,arg1,arg2);
}

dlopen_callback gCallback;

static const char *LoadingLibrary(const char *filename){
	return filename;
}

static void LibraryLoaded(const char *filename, void *handle){
	onLibHookedByMonoHooker(filename, (soinfo*)handle);
}

static const JNINativeMethod gHookMethods[] = {
	{ "nativeProcessCheat", "(III)V", (void*)jniProcessCheat },
};

extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	LogD("<%s> %s %s running ... ", __FUNCTION__, __DATE__, __TIME__);
	JNIEnv* env = NULL;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}
	jclass clazz = env->FindClass("com/gameassist/plugin/mono/NativeUtils");
	if (clazz == NULL) {
		return -1;
	}
	if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
		return -1;
	}

	gCallback.onLoadingLibrary = LoadingLibrary;
	gCallback.onLibraryLoaded = LibraryLoaded;
	registDlopen(gCallback);
	return JNI_VERSION_1_4;
}






