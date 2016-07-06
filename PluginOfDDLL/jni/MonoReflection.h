#ifndef __MONO_REFLECTION_H__458973947544523__
#define __MONO_REFLECTION_H__458973947544523__

#include "hookutils.h"
#include "monoDef.h"
#include <hash_map>


#define HEXNAMELEN 2048

using namespace std;

class MonoReflection{

private:
	MonoClass *monoClassPtr;
	hash_map<const char*, void*> fieldHash;
	hash_map<const char*, void*> methodHash;

public:
	MonoReflection():monoClassPtr(0){}

	bool init(void *image, const char *clzName, const char *ns = NULL){
		if(monoClassPtr || !image){
			return monoClassPtr != NULL;
		}
		monoClassPtr = mono_class_from_name(image, ns?ns:"", clzName);


#ifdef DEBUG
		if(!monoClassPtr){
			LogD("<%s> can't find class(%s) pointer",__FUNCTION__, clzName);
		}else{
			dump(NULL, true);
		}
#endif
		return monoClassPtr != NULL;
	}

	void addField(const char *fieldName){
		if(!monoClassPtr)
			return;
		void *field = mono_class_get_field_from_name(monoClassPtr,fieldName);
		fieldHash[fieldName] = field;
#ifdef DEBUG
		if(!field)
			LogD("<%s> can't find field(%s) pointer",__FUNCTION__, fieldName);
#endif
	}

	void addMethod(const char *methodName, int paramCnt, int *params = NULL){
		if(!monoClassPtr)
			return;
		void *method = findMethod(methodName, paramCnt,params);
		methodHash[methodName] = method;
#ifdef DEBUG
		if(!method)
			LogD("<%s> can't find method(%s) pointer",__FUNCTION__, methodName);
#endif
	}

	void *getFieldBoxedValue(void *domain, const char *fieldName, void *monoClassInstance = NULL){
		if(!domain || !monoClassPtr)
			return NULL;
		MonoClassField *field = (MonoClassField *)fieldHash[fieldName];
		if(!field){
#ifdef DEBUG
			LogD("<%s> No %s's pointer",__FUNCTION__, fieldName);
#endif
			return NULL;
		}
		MonoObject *value = NULL;
		if(mono_field_get_value_object){
			value = (MonoObject *)mono_field_get_value_object(domain, field, monoClassInstance);
		}else if(monoClassInstance){
			mono_field_get_value (monoClassInstance, field, &value);
		}else{
			void *vtable = mono_class_vtable(domain, field->parent);
			mono_field_static_get_value (vtable, field, &value);
		}
#ifdef DEBUG
		if(!value)
			LogD("<%s> can't get field(%s)'s value",__FUNCTION__, fieldName);
#endif
		return (void *)value;
	}

	int getStaticFieldIntValue(void *domain,const char *fieldName){
		int ret = 0;
		if(!monoClassPtr)
			return ret;
		MonoClassField *f = (MonoClassField*)fieldHash[fieldName];
		if( f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			void *vtable = mono_class_vtable(domain, f->parent);
			mono_field_static_get_value (vtable, f, &ret);
		}
		return ret;
	}

	void getFieldValue(const char *fieldName, void *value, void *monoClassInstance = NULL){
		if(!monoClassPtr && monoClassInstance){
			monoClassPtr = ((MonoObject *)monoClassInstance)->vtable->klass;
			if(monoClassPtr){
				addField(fieldName);
			}
		}
		if(!monoClassPtr)
			return;
		void *field = fieldHash[fieldName];
		if(!field){
#ifdef DEBUG
			LogD("<%s> No %s's pointer",__FUNCTION__, fieldName);
#endif
			return NULL;
		}
		mono_field_get_value(monoClassInstance, field, value);
	}


	void setFieldValue(void *domain, const char *fieldName, void *arg, void *monoClassInstance = NULL){
		if(!monoClassPtr && monoClassInstance){
			monoClassPtr = ((MonoObject *)monoClassInstance)->vtable->klass;
			if(monoClassPtr){
				addField(fieldName);
			}
		}
		if(!domain || !monoClassPtr)
			return;

		MonoClassField *field = (MonoClassField*)fieldHash[fieldName];
		if(!field){
#ifdef DEBUG
			LogD("<%s> No %s's pointer",__FUNCTION__, fieldName);
#endif
			return;
		}
		if(field->type->attrs & FIELD_ATTRIBUTE_STATIC){
			MonoVTable *vt = (MonoVTable*)mono_class_vtable(domain, field->parent);
			if(mono_field_static_set_value){
				mono_field_static_set_value(vt, field, arg);
			}else{
				#ifdef DEBUG
				LogD("<%s> vtsize=%d, fieldoffset=%d (%d)",__FUNCTION__, vt->klass->vtable_size, field->offset, vt->initialized);
				#endif
				if(vt->initialized && field->offset >= 0 && vt->has_static_fields){
					void *addr =(char *)vt->vtable[vt->klass->vtable_size] + field->offset;
					int *p = (int*)addr;
					*p = arg ? *(int*)arg : 0;
					#ifdef DEBUG
					LogD("<%s> addr 0x%x == %d ",__FUNCTION__, (int)addr, *(int *)addr);
					#endif
				}
			}
		}else{
			mono_field_set_value(monoClassInstance, field, arg);
		}
	}

	MonoObject *invokeMethod(const char *methodName, void **arg = NULL, void *monoClassInstance = NULL){
		if(!monoClassPtr)
			return NULL;
		void *method = methodHash[methodName];
		if(!method){
#ifdef DEBUG
			LogD("<%s> No %s's pointer",__FUNCTION__, methodName);
#endif
			return NULL;
		}
		return mono_runtime_invoke((MonoMethod *)method, monoClassInstance, arg, NULL);
	}

	MonoMethod *findMethod(const char *methodName,int paramCnt, int *params = NULL){
		if(!monoClassPtr)
			return NULL;
		MonoMethod * method = NULL;
		if(mono_class_get_methods){
			MonoMethod * m;void *iter = NULL;
			int found = 1;
			while ((m = mono_class_get_methods (monoClassPtr, &iter))){
				void *sig = mono_method_signature(m);
				MonoMethodSignature *s = (MonoMethodSignature *)sig;
				if(paramCnt == s->param_count && strcmp(methodName, ((MonoMethod *)m)->name) == 0 ){
					found = 1;
					if(params != NULL){
						for(int i = 0; i < paramCnt; i++){
							MonoType *param = s->params[i+1];
							if(params[i] != param->type ){
								LogD("<%s> param %d error (%d != %d)",__FUNCTION__,  i, params[i] , param->type);
								found = 0;
								break;
							}
						}
					}
					if(found){
						method = m;
						break;
					}
				}
			}
		}
		return method;
	}

	void dump(void *domain = NULL, bool isDumpMethod = true){
		dumpMonoClass(monoClassPtr, NULL, domain, isDumpMethod);
	}

	static void dumpMonoObject(MonoObject *object, void *domain = NULL){
		dumpMonoClass(object->vtable->klass, object, domain);
	}

	static void dumpMonoClass(void *clzPtr, void *obj = NULL, void *domain = NULL, bool isDumpMethod = true){
		if(!clzPtr)
			return;
		MonoClass *c = (MonoClass *)clzPtr;
		LogD("<%s> %s",__FUNCTION__, mono_class_get_name(c));
		if(mono_class_get_fields){
			MonoClassField *f = NULL; void *iter = NULL;
			while ((f = (MonoClassField *)mono_class_get_fields (c, &iter))){
				dumpField(f, obj, domain);
			}
		}

		if(mono_class_get_properties){
			MonoProperty *p = NULL; void *iter = NULL;
			while ((p = (MonoProperty *)mono_class_get_properties (c, &iter))){
				dumpProperty(p);
			}
		}

		if(isDumpMethod){
			if(mono_class_get_methods){
				MonoMethod * m = NULL;void *iter = NULL;
				while ((m = (MonoMethod *)mono_class_get_methods (c, &iter))){
					dumpMethod(m);
				}
			}
		}

		if(c->parent){
			const char *pname = mono_class_get_name(c->parent);
			if(!strcmp("Object",pname) || !strcmp("MonoBehaviour",pname) || !strcmp("Behaviour",pname)|| !strcmp("Component",pname)){
				LogD("<%s> skip common class %s:%s",__FUNCTION__, mono_class_get_name(c),pname);
			}else{
				LogD(" ");
				LogD("<%s> parent class %s:%s",__FUNCTION__, mono_class_get_name(c),pname);
				dumpMonoClass(c->parent, obj);
			}
		}
		if(c->nested_in){
			LogD("<%s> %s->nested_in(%x:%x)",__FUNCTION__, mono_class_get_name(c), (int)c, (int)c->nested_in);
			dumpMonoClass(c->nested_in, obj);
		}
		if(c->cast_class && c->cast_class != c){
			LogD("<%s> %s->cast_class(%x:%x)",__FUNCTION__, mono_class_get_name(c),(int)c,(int)c->cast_class);
			dumpMonoClass(c->cast_class, obj);
		}
		if(c->element_class  && c->element_class != c){
			LogD("<%s> %s->element_class(%x:%x)",__FUNCTION__, mono_class_get_name(c),(int)c,(int)c->element_class);
			dumpMonoClass(c->element_class, obj);
		}
	}

	static void dumpMethod(MonoMethod *m){
		if(m == NULL)
			return;
		char *tmp = (char *)malloc(HEXNAMELEN);
		memset(tmp, 0, HEXNAMELEN);
		dumpMethod(m, tmp);
		LogD("%s", tmp);
		free(tmp);
	}

	static void dumpMethod(MonoMethod *m, char *tmp){
		if(m == NULL || !tmp)
			return;
		sprintf(tmp, "method 0x%x -- ", (int)m);
		getMethodFlagDesc(m->flags, tmp);
		getMethodIFlagDesc(m->iflags, tmp);
		MonoMethodSignature *s = (MonoMethodSignature *)mono_method_signature(m);
		if(s == NULL){
			LogD("<%s> fail, no signature ... ", __FUNCTION__);
			return;
		}
		if(s->param_count > 0){
			getMonoTypeDesc(s->params[0]->type,tmp);
		}
		strcat(tmp, " ");
		if(m->klass){
			strcat(tmp,mono_class_get_name(m->klass));
			strcat(tmp,"::");
		}
		toHex(m->name, tmp);
		strcat(tmp,"(");
		for(int i = 1; i <= s->param_count; i++){
			getMonoTypeDesc(s->params[i]->type,tmp);
			if(i < s->param_count)
				strcat(tmp,",");
		}
		strcat(tmp,");");
	}

	static void dumpMethodCode(MonoMethod *m){
		if(mono_disasm_code && mono_method_get_header){
			MonoMethodHeader *header = mono_method_get_header (m);
			if(header){
				LogD("<%s> Method's header(0x%x) code from 0x%x, size = (%d)0x%x", __FUNCTION__,(int)header,(int)header->code, header->code_size, header->code_size);
				char *result = mono_disasm_code(NULL, m, header->code, header->code + header->code_size);
				LogD("%s", result);
			}
		}
	}

	static void dumpField(MonoClassField *f, void *obj, void *domain){
		if(f == NULL)
			return;
		char *tmp = (char *)malloc(HEXNAMELEN);
		memset(tmp, 0, HEXNAMELEN);
		strcat(tmp, "field ");
		getFieldAttrsDesc(f->type->attrs, tmp);
		getMonoTypeDesc(f->type->type, tmp);
		strcat(tmp, " ");
		if(f->parent){
			strcat(tmp,mono_class_get_name(f->parent));
			strcat(tmp,"::");
		}
		toHex(f->name, tmp);
		switch(f->type->type){
		case MONO_TYPE_BOOLEAN:
		case MONO_TYPE_CHAR:
		case MONO_TYPE_I1:case MONO_TYPE_U1:
		case MONO_TYPE_I2:case MONO_TYPE_U2:
		case MONO_TYPE_I4:case MONO_TYPE_U4:
		case MONO_TYPE_I8:case MONO_TYPE_U8:
		case MONO_TYPE_VALUETYPE:
			LogD("%s = %d;", tmp, getFieldIntValue(f, obj, domain));
			goto out;
		case MONO_TYPE_R4:
			LogD("%s = %f;", tmp, getFieldFloatValue(f, obj, domain));
			goto out;
		case MONO_TYPE_R8:
			LogD("%s = %f;", tmp, getFieldDoubleValue(f, obj, domain));
			goto out;
		case MONO_TYPE_CLASS:
			MonoObject *monoObj = getFieldClassValue(f, obj, domain);
			if(monoObj){
				LogD("%s = %s(%x);", tmp, mono_class_get_name( monoObj->vtable->klass), (int)monoObj);
				goto out;
			}
			break;
		}
		LogD("%s", tmp);
	out:
		free(tmp);
	}

	static const char *getStaticFieldAddr(void *domain, MonoClassField *f, char *out){
		if(domain && f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			MonoVTable *vt = (MonoVTable*)mono_class_vtable(domain, f->parent);
			int addr =(int)vt->vtable[vt->klass->vtable_size] + f->offset;
			char tmp[100];
			if (vt->initialized)
				sprintf(tmp, "(%d.%d:%x=%x)", vt->klass->vtable_size,f->offset, addr, *(int *)addr);
			strcat(out, tmp);
		}
	}
	static int getFieldIntValue(MonoClassField *f, void *obj, void *domain){
		int ret = 0;
		if( f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			if(domain){
				MonoVTable *vt = (MonoVTable*)mono_class_vtable(domain, f->parent);
				mono_field_static_get_value (vt, f, &ret);
			}
		}else if(obj){
			mono_field_get_value(obj, f, &ret);
		}
		return ret;
	}

	static float getFieldFloatValue(MonoClassField *f, void *obj, void *domain){
		float ret = 0;
		if( f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			if(domain){
				void *vtable = mono_class_vtable(domain, f->parent);
				mono_field_static_get_value (vtable, f, &ret);
			}
		}else if(obj){
			mono_field_get_value(obj, f, &ret);
		}
		return ret;
	}

	static double getFieldDoubleValue(MonoClassField *f, void *obj, void *domain){
		double ret = 0;
		if( f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			if(domain){
				void *vtable = mono_class_vtable(domain, f->parent);
				mono_field_static_get_value (vtable, f, &ret);
			}
		}else if(obj){
			mono_field_get_value(obj, f, &ret);
		}
		return ret;
	}

	static MonoObject* getFieldClassValue(MonoClassField *f, void *obj, void *domain){
		MonoObject *ret = NULL;
		if( f->type->attrs & FIELD_ATTRIBUTE_STATIC){
			if(domain){
				void *vtable = mono_class_vtable(domain, f->parent);
				mono_field_static_get_value (vtable, f, &ret);
			}
		}else if(obj){
			mono_field_get_value(obj, f, &ret);
		}
		return ret;
	}

	static void dumpProperty(MonoProperty *p){
		if(p == NULL)
			return;
		char *tmp = (char *)malloc(HEXNAMELEN);
		memset(tmp, 0, HEXNAMELEN);
		strcat(tmp, "property ");
		getFieldAttrsDesc(p->attrs, tmp);
		toHex(p->name, tmp);
		LogD("%s", tmp);
		free(tmp);
		dumpMethod(p->get);
		dumpMethod(p->set);
	}

	static void toHex(const char *src, char *out){
		char tmp[4];
		bool ascii = true;
		for(int i = 0; ; i++){
			if(src[i] == 0)
				break;
			if(src[i] < 0x20 || src[i] >0x7f){
				sprintf(tmp, "%02x ", src[i]);
			}else{
				sprintf(tmp, "%c", src[i]);
			}
			strcat(out, tmp);
		}
	}

	static const char *getMonoTypeDesc(int type, char *out){
		if(type <= MONO_TYPE_INTERNAL)
			strcat(out, MONO_TYPE_NAME[type]);
		else if(type == MONO_TYPE_ENUM)
			strcat(out, "ENUM");
		else if(type == MONO_TYPE_PINNED)
			strcat(out, "PINNED");
		else if(type == MONO_TYPE_SENTINEL)
			strcat(out, "SENTINEL");
		else if(type == MONO_TYPE_MODIFIER)
			strcat(out, "MODIFIER");
		else
			strcat(out, "??");
	}

	static void getFieldAttrsDesc(int attrs, char *out){
		if( attrs & FIELD_ATTRIBUTE_PRIVATE )
			strcat(out, "private ");
		if( attrs & FIELD_ATTRIBUTE_PUBLIC )
			strcat(out, "public ");
		if( attrs & FIELD_ATTRIBUTE_STATIC )
			strcat(out, "static ");
		if( attrs & FIELD_ATTRIBUTE_INIT_ONLY )
			strcat(out, "init_only ");
		if( (attrs & FIELD_ATTRIBUTE_NOT_SERIALIZED) == 0 )
			strcat(out, "serialized ");
		if( attrs & FIELD_ATTRIBUTE_LITERAL )
			strcat(out, "literal ");
		if( attrs & FIELD_ATTRIBUTE_SPECIAL_NAME )
			strcat(out, "special name ");
		if( attrs & FIELD_ATTRIBUTE_HAS_DEFAULT )
			strcat(out, "default ");
	}

	static void getMethodFlagDesc(int flags, char *out){
		if( flags & METHOD_ATTRIBUTE_PRIVATE )
			strcat(out, "private ");
		if( flags & METHOD_ATTRIBUTE_PUBLIC )
			strcat(out, "public ");
		if( flags & METHOD_ATTRIBUTE_STATIC )
			strcat(out, "static ");
		if( flags & METHOD_ATTRIBUTE_FINAL )
			strcat(out, "final ");
		if( (flags & METHOD_ATTRIBUTE_VIRTUAL) == 0 )
			strcat(out, "virtual ");
		if( flags & METHOD_ATTRIBUTE_STRICT )
			strcat(out, "strict ");
		if( flags & METHOD_ATTRIBUTE_ABSTRACT )
			strcat(out, "abstract ");
		if( flags & METHOD_ATTRIBUTE_PINVOKE_IMPL )
			strcat(out, "invoke_imp ");
		if( flags & METHOD_ATTRIBUTE_RT_SPECIAL_NAME )
			strcat(out, "rt_specialname ");
		if( flags & METHOD_ATTRIBUTE_HAS_SECURITY )
			strcat(out, "has_security ");
	}

	static void getMethodIFlagDesc(int iflags, char *out){
		if( iflags & METHOD_IMPL_ATTRIBUTE_NATIVE )
			strcat(out, "native ");
		if( iflags & METHOD_IMPL_ATTRIBUTE_OPTIL )
			strcat(out, "opt ");
		if( iflags & METHOD_IMPL_ATTRIBUTE_RUNTIME )
			strcat(out, "runtime ");
		if( (iflags & METHOD_IMPL_ATTRIBUTE_UNMANAGED) == 0 )
			strcat(out, "unmanagered ");
		if( iflags & METHOD_IMPL_ATTRIBUTE_INTERNAL_CALL )
			strcat(out, "icall ");
	}
};

#endif   
