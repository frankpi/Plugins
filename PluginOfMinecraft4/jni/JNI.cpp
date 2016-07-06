

#include "hookutils.h"

#define VICTIM_LIB "libminecraftpe.so"

static JavaVM *gs_jvm=NULL;
extern void doScreenshotCheat(JNIEnv *env,JavaVM* vm);
JNIEXPORT void JNICALL jniDoScreenshotCheat(JNIEnv *env, JavaVM* vm) {
	doScreenshotCheat(env,gs_jvm);
	/*if(flag==15){
		getscreen(env);
	}*/
}


extern void doProcessCheat(int flag, int arg1, int arg2);
JNIEXPORT void JNICALL jniDoProcessCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
	doProcessCheat(flag, arg1, arg2);
}

extern int getGameCurrentMode();
JNIEXPORT int JNICALL jniGetGameCurrentMode(JNIEnv *env, jclass clazz) {
	getGameCurrentMode();
}

extern int IsInGame();
JNIEXPORT int JNICALL jniIsInGame(JNIEnv *env, jclass clazz) {
	IsInGame();
}

extern int getMyWorldSeed();
JNIEXPORT int JNICALL jniGetMyWorldSeed(JNIEnv *env, jclass clazz) {
	getMyWorldSeed();
}


extern void doAddItemCheat(int flag, int arg1, int arg2,int arg3);
JNIEXPORT void JNICALL jniDoAddItemCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2,jint arg3) {
	doAddItemCheat(flag, arg1, arg2,arg3);
}

extern void doRoleCheat(int flag, int arg1, int arg2);
JNIEXPORT void JNICALL jniDoRoleCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
	doRoleCheat(flag, arg1, arg2);
}



extern void doGameCheat(int flag, int arg1, int arg2);
JNIEXPORT void JNICALL jniDoGameCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
	doGameCheat(flag, arg1, arg2);
}


extern void doCheatPostXYZ(float flag, float arg1, float arg2);
JNIEXPORT void JNICALL jniDoCheatPostXYZ(JNIEnv *env, jclass clazz, jfloat arg0, jfloat arg1, jfloat arg2) {
	doCheatPostXYZ(arg0, arg1, arg2);
}

extern float doCallbackGetX();
JNIEXPORT float  JNICALL jniDoCallbackGetX(JNIEnv *env, jclass clazz) {
	return doCallbackGetX();
}

extern float doCallbackGetY();
JNIEXPORT float  JNICALL jniDoCallbackGetY(JNIEnv *env, jclass clazz) {
	return doCallbackGetY();
}

extern float doCallbackGetZ();
JNIEXPORT float  JNICALL jniDoCallbackGetZ(JNIEnv *env, jclass clazz) {
	return doCallbackGetZ();
}

dlopen_callback gCallback;

static const char *LoadingLibrary(const char *filename){
	return filename;
}

static void LibraryLoaded(const char *filename, void *handle){
	if(strstr(filename, VICTIM_LIB) && handle){
		unregistDlopen(gCallback);
		soinfo *info = (soinfo*)handle;
		hook_address(info->base);
		hook_symbols(info);
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
	}
}

static const JNINativeMethod gHookMethods[] = {
	{ "nativeDoScreenshotCheat", "()V", (void*)jniDoScreenshotCheat },
	{ "nativeDoCheat", "(III)V", (void*)jniDoProcessCheat },
	{ "nativeDoAddItemCheat", "(IIII)V", (void*)jniDoAddItemCheat },
	{ "nativeDoRoleCheat", "(III)V", (void*)jniDoRoleCheat },
	{ "nativeDoGameCheat", "(III)V", (void*)jniDoGameCheat },
	{ "nativeDoCallbackGetX", "()F", (void*)jniDoCallbackGetX },
	{ "nativeDoCallbackGetY", "()F", (void*)jniDoCallbackGetY },
	{ "nativeDoCallbackGetZ", "()F", (void*)jniDoCallbackGetZ },
	{ "nativeDoCheatPostXYZ", "(FFF)V", (void*)jniDoCheatPostXYZ },

	{ "nativeGetGameCurrentMode", "()I", (void*)jniGetGameCurrentMode },
	{ "nativeIsInGame", "()I", (void*)jniIsInGame },
	{ "nativeGetMyWorldSeed", "()I", (void*)jniGetMyWorldSeed },

};

extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved){

	#ifdef NDK_DEBUG
	LogD("<%s> libA8 %s %s", __FUNCTION__, __DATE__, __TIME__);
	#endif
	gs_jvm=vm;
	JNIEnv* env = NULL;

	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}

	jclass clazz;

	clazz = env->FindClass("com/gameassist/plugin/nativeutils/NativeUtils");
	if (clazz == NULL) {
		return -1;
	}

	if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
		return -1;
	}
	hookPrepare();
	return JNI_VERSION_1_4;
}
