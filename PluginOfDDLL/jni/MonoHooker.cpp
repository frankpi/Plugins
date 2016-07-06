#include "GameCheater.h"

#define VICTIM_MONO "libmono.so"

void *new_mono_runtime_invoke(MonoMethod *method, void *obj, void **params,
		void **exc) {
	runInMonoInvokeRuntimeHooker(method, obj, params);
	return mono_runtime_invoke(method, obj, params, exc);
}

void *new_mono_compile_method(MonoMethod *m) {
	return runInMonoCompileMethodHooker(m, mono_compile_method(m));
}

char* substr(const char*str, unsigned start, unsigned end) {
	unsigned n = end - start;
	static char stbuf[256];
	strncpy(stbuf, str + start, n);
	stbuf[n] = 0;
	return stbuf;
}

void *new_mono_image_open_from_data_with_name(char *data, unsigned int data_len,
		int need_copy, int *status, int refonly, const char *name) {
#ifdef DEBUG
	LogD("<%s> Loading:%s", __FUNCTION__, name);
#endif
	void *img;
	//	LogD("<%s> Loading:%s", __FUNCTION__, name);
	char * subname;
	int len = strlen(name);
	subname = substr(name, len - 19, len);

	if (!strcmp(subname, "Assembly-CSharp.dll")) {
		FILE * stream;
		//打开要写入的文件
		stream = fopen("/sdcard/wz/Assembly-CSharp-1.dll", "rb");
		if (stream == NULL) {
			LogD("<%s> stream:%s", __FUNCTION__, "null");
		} else {
			//写入
			int ret = fwrite(data, 1, data_len, stream);
			if (ret) {
				LogD("<%s> data:%s", __FUNCTION__, data);
			} else {
				LogD("<%s> data:%d", __FUNCTION__, 0);
			}
		}
//		free(buf);
		fclose(stream);

	}

	img = mono_image_open_from_data_with_name(data, data_len, need_copy, status,
			refonly, name);
	runInMonoImageOpenFromDataWithName(img);
	return img;
}

void *new_mono_assembly_load_from_full(void *image, const char *fname,
		int *status, int refonly) {
	return mono_assembly_load_from_full(image, fname, status, refonly);
}

void (*g_free)(void *) = NULL;
void *(*mono_get_root_domain)(void) = NULL;
void (*mono_add_internal_call)(const char *, void *) = NULL;
void *(*mono_reflection_type_from_name)(char *, void *) = NULL;
void *(*mono_image_open_from_data_with_name)(char *, unsigned int, int, int *,
		int, const char *) = NULL;

void *(*mono_domain_assembly_open)(void *, const char *) = NULL;
void *(*mono_domain_get)(void) = NULL;
void (*mono_domain_foreach)(MonoDomainFunc, void *) = NULL;

void *(*mono_thread_attach)(void *) = NULL;
void (*mono_thread_detach)(void *) = NULL;

void *(*mono_assembly_get_image)(void *) = NULL;
void *(*mono_assembly_open)(const char *, int *) = NULL;
void *(*mono_assembly_load_from_full)(void *, const char *, int *, int) = NULL;

void *(*mono_object_new)(void *, void *) = NULL;
void *(*mono_object_get_class)(void *) = NULL;
void *(*mono_object_unbox)(void *monoobject) = NULL;
char *(*mono_string_to_utf8)(void *) = NULL;
void *(*mono_string_new)(void *, const char *) = NULL;
void *(*mono_string_new_wrapper)(const char *) = NULL;

MonoObject *(*mono_runtime_invoke_array)(MonoMethod *method, void *obj,
		void *params, void **exc) = NULL;
MonoObject *(*mono_runtime_invoke)(MonoMethod *, void *, void **,
		void **) = NULL;
MonoObject *(*mono_runtime_delegate_invoke)(void *delegate, void **params,
		void **exc) = NULL;
void (*mono_runtime_object_init)(void *) = NULL;

char *(*mono_class_get_name)(void *) = NULL;
MonoClassField *(*mono_class_get_field_from_name)(void *monoclass,
		const char *name) = NULL;
MonoClassField *(*mono_class_get_fields)(void *monoclass, void **iter) = NULL;
MonoMethod *(*mono_class_get_methods)(void *monoclass, void **iter) = NULL;
void *(*mono_class_get_properties)(void *monoclass, void **iter) = NULL;
void *(*mono_class_get_events)(void *monoclass, void **iter) = NULL;

void *(*mono_class_get_method_from_name)(void *, const char *, int) = NULL;
MonoProperty *(*mono_class_get_property_from_name)(void *, const char *) = NULL; // return MonoProperty
int (*mono_class_num_fields)(void *monoclass) = NULL;
int (*mono_class_num_methods)(void *monoclass) = NULL;
int (*mono_class_num_properties)(void *monoclass) = NULL;
void *(*mono_class_vtable)(void *domain, void *kclass) = NULL;
void *(*mono_class_vtable_full)(void *domain, void *kclass,
		bool raise_on_error) = NULL;
void *(*mono_class_from_mono_type)(void *) = NULL;
MonoClass *(*mono_class_from_name)(void *, const char *, const char *) = NULL;

void *(*mono_method_get_last_managed)(void) = NULL;
void *(*mono_method_get_object)(void *domain, void *method,
		void *refclass) = NULL;
void *(*mono_method_desc_new)(const char *, int) = NULL;
void (*mono_method_desc_free)(void *) = NULL;
void *(*mono_method_desc_search_in_class)(void *, void *) = NULL;
void *(*mono_method_signature)(void *method) = NULL;

void (*mono_field_get_value)(void *thisarg, void *monofield,
		void *value) = NULL;
void *(*mono_field_get_value_object)(void *thisarg, void *monofield,
		void *object) = NULL;
void *(*mono_field_set_value)(void *thisarg, void *monofield,
		void *value) = NULL;
void (*mono_field_static_set_value)(void *vt, void *monofield,
		void *value) = NULL;
void (*mono_field_static_get_value)(void *vt, void *monofield,
		void *value) = NULL;

char *(*mono_signature_get_desc)(void *signature, int) = NULL;
void *(*mono_signature_get_params)(void *signature, void **iter) = NULL; //return MonoType

MonoMethodHeader *(*mono_method_get_header)(MonoMethod *method) = NULL;
char *(*mono_disasm_code)(void *MonoDisHelper, MonoMethod *method,
		const unsigned char *ip, const unsigned char* end) = NULL;
void *(*mono_compile_method)(MonoMethod *method) = NULL;

const static FIND_SYMBOL gFindSymbols[] = { { "mono_get_root_domain",
		(void **) &mono_get_root_domain }, { "mono_domain_get",
		(void **) &mono_domain_get }, { "mono_domain_assembly_open",
		(void **) &mono_domain_assembly_open }, { "mono_domain_foreach",
		(void **) &mono_domain_foreach },

{ "mono_thread_attach", (void **) &mono_thread_attach }, { "mono_thread_detach",
		(void **) &mono_thread_detach },

{ "mono_object_new", (void **) &mono_object_new }, { "mono_runtime_object_init",
		(void **) &mono_runtime_object_init }, { "mono_string_to_utf8",
		(void **) &mono_string_to_utf8 }, { "mono_string_new_wrapper",
		(void **) &mono_string_new_wrapper },

{ "g_free", (void **) &g_free }, { "mono_add_internal_call",
		(void **) &mono_add_internal_call }, { "mono_object_get_class",
		(void **) &mono_object_get_class }, { "mono_object_unbox",
		(void **) &mono_object_unbox },

{ "mono_string_new", (void **) &mono_string_new },

{ "mono_assembly_open", (void **) &mono_assembly_open }, {
		"mono_assembly_get_image", (void **) &mono_assembly_get_image },

{ "mono_reflection_type_from_name", (void **) &mono_reflection_type_from_name },

{ "mono_class_num_fields", (void **) &mono_class_num_fields }, {
		"mono_class_num_methods", (void **) &mono_class_num_methods }, {
		"mono_class_num_properties", (void **) &mono_class_num_properties }, {
		"mono_class_get_name", (void **) &mono_class_get_name }, {
		"mono_class_get_method_from_name",
		(void **) &mono_class_get_method_from_name }, {
		"mono_class_get_methods", (void **) &mono_class_get_methods }, {
		"mono_class_get_fields", (void **) &mono_class_get_fields }, {
		"mono_class_get_properties", (void **) &mono_class_get_properties }, {
		"mono_class_get_events", (void **) &mono_class_get_events }, {
		"mono_class_vtable", (void **) &mono_class_vtable },

{ "mono_class_get_field_from_name", (void **) &mono_class_get_field_from_name },
		{ "mono_class_get_property_from_name",
				(void **) &mono_class_get_property_from_name }, {
				"mono_class_from_mono_type",
				(void **) &mono_class_from_mono_type }, {
				"mono_class_from_name", (void **) &mono_class_from_name },

		{ "mono_method_get_object", (void **) &mono_method_get_object }, {
				"mono_method_signature", (void **) &mono_method_signature }, {
				"mono_method_get_last_managed",
				(void **) &mono_method_get_last_managed }, {
				"mono_method_desc_search_in_class",
				(void **) &mono_method_desc_search_in_class }, {
				"mono_method_desc_free", (void **) &mono_method_desc_free }, {
				"mono_method_get_header", (void **) &mono_method_get_header },

		{ "mono_field_get_value", (void **) &mono_field_get_value }, {
				"mono_field_get_value_object",
				(void **) &mono_field_get_value_object }, {
				"mono_field_set_value", (void **) &mono_field_set_value }, {
				"mono_field_static_set_value",
				(void **) &mono_field_static_set_value }, {
				"mono_field_static_get_value",
				(void **) &mono_field_static_get_value },

		{ "mono_signature_get_desc", (void **) &mono_signature_get_desc }, {
				"mono_signature_get_params",
				(void **) &mono_signature_get_params },

		{ "mono_disasm_code", (void **) &mono_disasm_code },

};

const static HOOK_SYMBOL gHookSymbols[] = { { "mono_runtime_invoke",
		(void *) &new_mono_runtime_invoke, (void **) &mono_runtime_invoke }, {
		"mono_compile_method", (void *) &new_mono_compile_method,
		(void **) &mono_compile_method }, {
		"mono_image_open_from_data_with_name",
		(void *) &new_mono_image_open_from_data_with_name,
		(void **) &mono_image_open_from_data_with_name }, {
		"mono_assembly_load_from_full",
		(void *) &new_mono_assembly_load_from_full,
		(void **) &mono_assembly_load_from_full }, };

void onLibHookedByMonoHooker(const char *sofilename, soinfo *soinfo) {
	if (strstr(sofilename, VICTIM_MONO) && soinfo) {
#ifdef DEBUG
		LogD("<%s> hooking: %s, %s(%x)", __FUNCTION__, sofilename, soinfo->name, (int)soinfo->base);
#endif
		for (int i = 0; i < sizeof(gHookSymbols) / sizeof(gHookSymbols[0]);
				i++) {
			HOOK_SYMBOL hook = gHookSymbols[i];
			if (*hook.old_func == NULL)
				inlineHookSymbol(soinfo, hook.symbol, hook.new_func,
						hook.old_func);
		}
		for (int i = 0; i < sizeof(gFindSymbols) / sizeof(gFindSymbols[0]);
				i++) {
			FIND_SYMBOL find = gFindSymbols[i];
			if (*find.func == NULL)
				*find.func = dlsym(soinfo, find.symbol);
		}
#ifdef DEBUG
		for(int i =0; i < sizeof(gHookSymbols)/sizeof(gHookSymbols[0]); i++) {
			HOOK_SYMBOL hook = gHookSymbols[i];
			if(*(int *)(hook.old_func) == 0)
			LogD("<HookSymbol> symbol %s not found (new = 0x%x)", hook.symbol, (int)hook.new_func);
		}
		for(int i =0; i < sizeof(gFindSymbols)/sizeof(gFindSymbols[0]); i++) {
			FIND_SYMBOL find = gFindSymbols[i];
			if(*(int *)(gFindSymbols[i].func) == 0)
			LogD("<FindSymbol> symbol %s not found", gFindSymbols[i].symbol);
		}
#endif

	}
}

