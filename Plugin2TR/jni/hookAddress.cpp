#include "hookutils.h"
#include <sys/prctl.h>


const static HOOK_Address gHookAddress[] = {

};

const static FIND_ADDRESS gFindAddress[] = {
};


void hook_address(int baseAddr){

	#ifdef NDK_DEBUG
	LogD("<%s> %s %s  baseAddr = 0x%x ", __FUNCTION__, __DATE__,__TIME__, baseAddr);
	#endif

	prctl(PR_SET_DUMPABLE, 1, NULL,NULL,NULL);
	for(unsigned int i = 0; i < sizeof(gFindAddress)/sizeof(gFindAddress[0]); i++){
		FIND_ADDRESS hook = gFindAddress[i];
		*(hook.func) = (void *)(baseAddr + hook.address);
		#ifdef NDK_DEBUG
		LogD("<%s> address 0x%x found at 0x%x", __FUNCTION__, (int)hook.address,(int) *(hook.func));
		#endif
		}

	for(unsigned int i = 0; i < sizeof(gHookAddress)/sizeof(gHookAddress[0]); i++){
		HOOK_Address hook = gHookAddress[i];

		if( *hook.old_func != NULL){
			#ifdef NDK_DEBUG
			LogD("<%s> address %x already hooked %x", __FUNCTION__, hook.address, (int)*(hook.old_func));
			#endif
			continue;
		}
		
//		if(memcmp( (void *)(baseAddr + hook.address), (void *)hook.disamble, DISAMBLE_CNT * sizeof(int)) != 0 ){
//			#ifdef NDK_DEBUG
//			LogD("<%s> address %x content fail", __FUNCTION__, (int)hook.address);
//			#endif
//			continue;
//		}

		inlineHookAddress( (void *)(baseAddr + hook.address), hook.new_func, hook.old_func);
		#ifdef NDK_DEBUG
		LogD("<%s> address %x hooked to new 0x%x, (old 0x%x)", __FUNCTION__, (int)hook.address,(int) hook.new_func, (int)*hook.old_func);
		#endif
	}
}
