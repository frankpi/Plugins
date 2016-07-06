#include "MonoReflection.h"
#include "GameCheater.h"
#include <queue>
using namespace std;
static MonoReflection GameManager, HUDManager;
void runInMonoImageOpenFromDataWithName(void *image) {

}

static queue<MonoCmd> cmdlist;
static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
void doUserCheatAction(int flag, int arg1, int arg2) {
	MonoCmd cmd;
	cmd.flag = flag;
	cmd.arg1 = arg1;
	cmd.arg2 = arg2;
	pthread_mutex_lock(&lock);
	cmdlist.push(cmd);
	pthread_mutex_unlock(&lock);
}
void* runInMonoCompileMethodHooker(MonoMethod *m, void *nativeFuncPtr) {
//	MonoReflection::dumpMethod(m);
	return nativeFuncPtr;
}
static bool lockTime = false, lockCombo = false;
void runInMonoInvokeRuntimeHooker(MonoMethod *m, void *obj, void **params) {
	if (strcmp("Update", m->name))
		return;
	if (strcmp("GameManager", mono_class_get_name(m->klass)))
		return;
	queue<MonoCmd> tmp;
	pthread_mutex_lock(&lock);
	for (int i = 0; i < cmdlist.size(); i++) {
		tmp.push(cmdlist.front());
		cmdlist.pop();
	}
	pthread_mutex_unlock(&lock);
	void * domain = mono_get_root_domain();
	if (!domain)
		return;
	for (int j = 0; j < tmp.size(); j++) {
		MonoCmd cmd = tmp.front();
		switch (cmd.flag) {
		case 1:

			break;
		case 2:

			break;
		case 4:
			break;
		case 5:
			break;
		}
		tmp.pop();
	}
}
