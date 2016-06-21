#ifndef __HOOK_UTILS_H__982364234523__  
#define __HOOK_UTILS_H__982364234523__  


#include <jni.h>
#include <android/log.h>
#include <dirent.h>
#include <stdlib.h>
#include <stdarg.h>
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <dlfcn.h>
#include <elf.h>


#define LOGD __android_log_print
#define LogD(fmt, ...)    {LOGD(ANDROID_LOG_DEBUG, "gameassist", fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}

#define LIBC "/system/lib/libc.so"

typedef const char *(*OnLoadingLibrary)(const char *filename);
typedef void (*OnLibraryLoaded)(const char *filename, void *handle);

typedef struct {
	OnLoadingLibrary onLoadingLibrary;
	OnLibraryLoaded onLibraryLoaded;
} dlopen_callback;

typedef void (*wrapper_inline_hook)(void *symbol, void *replace, void **result);
typedef void (*wrapper_add_dlopen_callback)(dlopen_callback callback);
typedef void (*wrapper_remove_dlopen_callback)(dlopen_callback callback);
typedef unsigned int (*wrapper_plt_hook)(const char *soname, const char *symbol, const char *victim, unsigned int new_symbol);
typedef void *(*wrapper_find_loaded_library)(const char *name);

static wrapper_plt_hook plt_hook = NULL;
static wrapper_inline_hook inline_hook = NULL; 
static wrapper_add_dlopen_callback add_dlopen_callback = NULL;
static wrapper_remove_dlopen_callback remove_dlopen_callback = NULL;
static wrapper_find_loaded_library find_loaded_library = NULL;

inline void* findLoadedLib(const char *soname){
	if(find_loaded_library == NULL){
		find_loaded_library = (wrapper_find_loaded_library)dlsym((void *)-1, "find_loaded_library_so");
	}
	return find_loaded_library(soname);
}

inline unsigned int pltHook(const char *soname, const char *symbol, const char *victim, unsigned int new_symbol){
	if(plt_hook == NULL){
		plt_hook = (wrapper_plt_hook)dlsym((void *)-1, "plt_hook");
	}
	plt_hook(soname, symbol, victim, new_symbol);
}

inline void inlineHookAddress(void *address, void *replace, void **result){
	if(inline_hook == NULL){
		inline_hook = (wrapper_inline_hook)dlsym((void *)-1, "inline_hook");
	}
	inline_hook(address, replace, result);
}

inline void *getAddress(void *handle, const char *symbol){
	return dlsym(handle, symbol);
}

inline void inlineHookSymbol(void *sohandle, const char *symbol, void *replace, void **result){
	void *address = dlsym(sohandle, symbol);
	if(address){
		if(inline_hook == NULL){
			inline_hook = (wrapper_inline_hook)dlsym((void *)-1, "inline_hook");
		}
		inline_hook(address, replace, result);
	}
}

inline void registDlopen(dlopen_callback callback){
	if(add_dlopen_callback == NULL){
		add_dlopen_callback = (wrapper_add_dlopen_callback)dlsym((void *)-1, "add_dlopen_callback");
	}
	add_dlopen_callback(callback);
}

inline void unregistDlopen(dlopen_callback callback){
	if(remove_dlopen_callback == NULL){
		remove_dlopen_callback = (wrapper_remove_dlopen_callback)dlsym((void *)-1, "remove_dlopen_callback");
	}
	remove_dlopen_callback(callback);
}


#define DISAMBLE_CNT 5
typedef struct _hook_address{
	int address;
	void *new_func;
	void **old_func;
	int disamble[DISAMBLE_CNT];
}HOOK_Address;

typedef struct _find_address{
	int address;
	void **func;
}FIND_ADDRESS;

typedef struct _hook_symbol{
	const char *symbol;
	void *new_func;
	void **old_func;
}HOOK_SYMBOL;

typedef struct _find_symbol{
	const char *symbol;
	void **func;
}FIND_SYMBOL;

#undef ANDROID_SH_LINKER
#define ANDROID_ARM_LINKER
#define SOINFO_NAME_LEN 128
typedef struct soinfo {
    const char name[SOINFO_NAME_LEN];
    Elf32_Phdr *phdr;
    int phnum;
    unsigned entry;
    unsigned base;
    unsigned size;

    int unused;  // DO NOT USE, maintained for compatibility.

    unsigned *dynamic;

    unsigned wrprotect_start;
    unsigned wrprotect_end;

    struct soinfo *next;
    unsigned flags;

    const char *strtab;
    Elf32_Sym *symtab;

    unsigned nbucket;
    unsigned nchain;
    unsigned *bucket;
    unsigned *chain;

    unsigned *plt_got;

    Elf32_Rel *plt_rel;
    unsigned plt_rel_count;

    Elf32_Rel *rel;
    unsigned rel_count;

#ifdef ANDROID_SH_LINKER
    Elf32_Rela *plt_rela;
    unsigned plt_rela_count;

    Elf32_Rela *rela;
    unsigned rela_count;
#endif /* ANDROID_SH_LINKER */

    unsigned *preinit_array;
    unsigned preinit_array_count;

    unsigned *init_array;
    unsigned init_array_count;
    unsigned *fini_array;
    unsigned fini_array_count;

    void (*init_func)(void);
    void (*fini_func)(void);

#ifdef ANDROID_ARM_LINKER
    /* ARM EABI section used for stack unwinding. */
    unsigned *ARM_exidx;
    unsigned ARM_exidx_count;
#endif

    unsigned refcount;
//    struct link_map linkmap;
} soinfo;

void hook_address(int baseAddr);
void hook_symbols(soinfo *soinfo);
void hookPrepare();
#endif   

