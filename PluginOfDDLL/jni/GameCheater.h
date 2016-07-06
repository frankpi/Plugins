#ifndef __GAME_CHEATER_DEFINES_H__2345568932475__
#define __GAME_CHEATER_DEFINES_H__2345568932475__

#include "MonoReflection.h"

void runInMonoImageOpenFromDataWithName(void *image);

void *runInMonoCompileMethodHooker(MonoMethod *m, void *nativeFuncPtr);

void runInMonoInvokeRuntimeHooker(MonoMethod *m, void *obj, void **params);

void doUserCheatAction(int flag, int arg1, int arg2);

void onLibHookedByMonoHooker(const char *sofilename, soinfo *soinfo);

typedef struct _MonoCmd{
	int flag;
	int arg1;
	int arg2;
}MonoCmd;

#endif
