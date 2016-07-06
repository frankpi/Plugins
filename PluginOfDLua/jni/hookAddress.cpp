#include "hookutils.h"
typedef int32_t (*imp_Curl_setopt)(void *session, int32_t option, va_list arg);
/*
 #define CURLOPT_URL				10002
#define CURLOPT_SSL_VERIFYPEER 6
#define CURLOPT_SSL_VERIFYHOST	8*/
 #define CURLOPT_URL				10002
 #define CURLOPT_SSL_VERIFYPEER	64
 #define CURLOPT_SSL_VERIFYHOST	81

static JavaVM *sVM;
static imp_Curl_setopt sCurl_setopt = NULL;

static int32_t my_curl_easy_setopt(void *session, int32_t option, ...) {
	va_list arg;
	va_start(arg, option);
	int32_t result = sCurl_setopt(session, option, arg);
	va_end(arg);
	return result;
}

static int32_t my_Curl_setopt(void *session, int32_t option, va_list arg) {
LogD("<%s> %s %s   option=%d", __FUNCTION__, __DATE__,__TIME__, option);
	va_list arg_copy;
	va_copy(arg_copy, arg);

	if (option == CURLOPT_URL) {
		char *url = va_arg(arg_copy, char *);
		my_curl_easy_setopt(session, CURLOPT_SSL_VERIFYHOST, 0);
		my_curl_easy_setopt(session, CURLOPT_SSL_VERIFYPEER, 0);
	} else if (option == CURLOPT_SSL_VERIFYHOST) {
		int value = va_arg(arg_copy, int);
		return 0;
	} else if (option == CURLOPT_SSL_VERIFYPEER) {
		int value = va_arg(arg_copy, int);
		return 0;
	}
	return sCurl_setopt(session, option, arg);
}
int (*old_my_luaL_loadbuffer1)(void *L, const char *buff, size_t sz, const char *name) = NULL;
int my_luaL_loadbuffer1(void *L, const char *buff, size_t sz, const char *name) {
	LogD("<%s> name=%s", __FUNCTION__, name);
	return old_my_luaL_loadbuffer1(L,buff,sz,name);
}
const static HOOK_Address gHookAddress[] = {


 //{ 0x30EE64, (void *)&my_luaL_loadbuffer1, (void **)&old_my_luaL_loadbuffer1,{0,0,0,0,0}},
 /*{ 0x2B479, (void *)&GetPackageHash, (void **)&old_GetPackageHash,{0,0,0,0,0}},
 { 0x1E399, (void *)&CheckPackage, (void **)&old_CheckPackage,{0,0,0,0,0}},
 { 0x1E4CD, (void *)&CheckPackages, (void **)&old_CheckPackages,{0,0,0,0,0}},

 { 0x25E89,(void *)&IsAlreadyScanFile,(void **)&old_IsAlreadyScanFile,{0,0,0,0,0}},
 */
};

void hook_address(int baseAddr){

    #ifdef NDK_DEBUG
	LogD("<%s> %s %s  baseAddr = 0x%x ", __FUNCTION__, __DATE__,__TIME__, baseAddr);
	 #endif

	for(unsigned int i = 0; i < sizeof(gHookAddress)/sizeof(gHookAddress[0]); i++){
		HOOK_Address hook = gHookAddress[i];

		if( *hook.old_func != NULL){
	 #ifdef NDK_DEBUG
			LogD("<%s> address %x already hooked %x", __FUNCTION__, hook.address, (int)*hook.old_func);
		 	#endif
			continue;
		}
		
//		if(memcmp( (void *)(baseAddr + hook.address), (void *)hook.disamble, DISAMBLE_CNT * sizeof(int)) != 0 ){
		//	#ifdef NDK_DEBUG
//			LogD("<%s> address %x content fail", __FUNCTION__, (int)hook.address);
		//	#endif
//			continue;
//		}

		inlineHookAddress( (void *)(baseAddr + hook.address), hook.new_func, hook.old_func);
 #ifdef NDK_DEBUG
		LogD("<%s> address %x hooked to new 0x%x, (old 0x%x)", __FUNCTION__, (int)hook.address,(int) hook.new_func, (int)*hook.old_func);
 	#endif
	}
}
