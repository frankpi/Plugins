#ifndef __MONO_DEFINES_H__23456478960869__
#define __MONO_DEFINES_H__23456478960869__


#define MONO_ZERO_LEN_ARRAY 0
typedef enum {
	MONO_TYPE_END        = 0x00,    /* End of List */
	MONO_TYPE_VOID       = 0x01,	//"void"
	MONO_TYPE_BOOLEAN    = 0x02,	//"bool"
	MONO_TYPE_CHAR       = 0x03,	//"char"
	MONO_TYPE_I1         = 0x04,	//"sbyte"
	MONO_TYPE_U1         = 0x05,	//"byte"
	MONO_TYPE_I2         = 0x06,	//"int16"
	MONO_TYPE_U2         = 0x07,	//"uint16"
	MONO_TYPE_I4         = 0x08,	//"int"
	MONO_TYPE_U4         = 0x09,	//"uint"
	MONO_TYPE_I8         = 0x0a,	//"long"
	MONO_TYPE_U8         = 0x0b,	//"ulong"
	MONO_TYPE_R4         = 0x0c,	//"single"
	MONO_TYPE_R8         = 0x0d,	//"double"
	MONO_TYPE_STRING     = 0x0e,	//"string"
	MONO_TYPE_PTR        = 0x0f,       /* arg: <type> token */
	MONO_TYPE_BYREF      = 0x10,       /* arg: <type> token */
	MONO_TYPE_VALUETYPE  = 0x11,       /* arg: <type> token */
	MONO_TYPE_CLASS      = 0x12,       /* arg: <type> token */
	MONO_TYPE_VAR	     = 0x13,	   /* number */
	MONO_TYPE_ARRAY      = 0x14,       /* type, rank, boundsCount, bound1, loCount, lo1 */
	MONO_TYPE_GENERICINST= 0x15,	   /* <type> <type-arg-count> <type-1> \x{2026} <type-n> */
	MONO_TYPE_TYPEDBYREF = 0x16,	//"typedbyref"
	MONO_TYPE_I          = 0x18,
	MONO_TYPE_U          = 0x19,
	MONO_TYPE_FNPTR      = 0x1b,	      /* arg: full method signature */
	MONO_TYPE_OBJECT     = 0x1c,	//"object"
	MONO_TYPE_SZARRAY    = 0x1d,       /* 0-based one-dim-array */
	MONO_TYPE_MVAR	     = 0x1e,       /* number */
	MONO_TYPE_CMOD_REQD  = 0x1f,       /* arg: typedef or typeref token */
	MONO_TYPE_CMOD_OPT   = 0x20,       /* optional arg: typedef or typref token */
	MONO_TYPE_INTERNAL   = 0x21,       /* CLR internal type */

	MONO_TYPE_MODIFIER   = 0x40,       /* Or with the following types */
	MONO_TYPE_SENTINEL   = 0x41,       /* Sentinel for varargs method signature */
	MONO_TYPE_PINNED     = 0x45,       /* Local var that points to pinned object */

	MONO_TYPE_ENUM       = 0x55        /* an enumeration */
} MonoTypeEnum;

static const char *MONO_TYPE_NAME[] = {
	"",	// = 0x00
	"void",	// = 0x01
	"bool",//    = 0x02,	//"bool"
	"char", //       = 0x03,	//"char"
	"sbyte", //         = 0x04,	//"sbyte"
	"byte",//         = 0x05,	//"byte"
	"int16",//         = 0x06,	//"int16"
	"uint16",//         = 0x07,	//"uint16"
	"int",//         = 0x08,	//"int"
	"uint",//         = 0x09,	//"uint"
	"long",//         = 0x0a,	//"long"
	"ulong",//         = 0x0b,	//"ulong"
	"single",//         = 0x0c,	//"single"
	"double",//         = 0x0d,	//"double"
	"string",//     = 0x0e,	//"string"
	"pointer",//        = 0x0f,       /* arg: <type> token */
	"ref",//      = 0x10,       /* arg: <type> token */
	"valuetype",//  = 0x11,       /* arg: <type> token */
	"class",//      = 0x12,       /* arg: <type> token */
	"var",//	     = 0x13,	   /* number */
	"array",//      = 0x14,       /* type, rank, boundsCount, bound1, loCount, lo1 */
	"GENERICINST",//= 0x15,	   /* <type> <type-arg-count> <type-1> \x{2026} <type-n> */
	"typedbyref",// = 0x16,	//"typedbyref"
	"0x17",
	"TYPE_I",//          = 0x18,
	"TYPE_U",//          = 0x19,
	"0x1a"
	"funcptr",//      = 0x1b,	      /* arg: full method signature */
	"object",//     = 0x1c,	//"object"
	"szarray",//    = 0x1d,       /* 0-based one-dim-array */
	"mvar",//	     = 0x1e,       /* number */
	"CMOD_REQD",//  = 0x1f,       /* arg: typedef or typeref token */
	"CMOD_OPT",//   = 0x20,       /* optional arg: typedef or typref token */
	"internal",//   = 0x21,       /* CLR internal type */
};

typedef struct _MonoVTable MonoVTable;
typedef struct _MonoObject MonoObject;
typedef struct _MonoClass MonoClass;
typedef struct _MonoString MonoString;
typedef struct _MonoProperty MonoProperty;
typedef struct _MonoMethod MonoMethod;
typedef struct _MonoMethodSignatureSmall MonoMethodSignatureSmall;
typedef struct _MonoType MonoType;
typedef struct _MonoEvent MonoEvent;
typedef struct _MonoClassField MonoClassField;
typedef struct _MonoMethodSignature MonoMethodSignature;
typedef struct _MonoMethodHeader MonoMethodHeader;

typedef struct {
	unsigned int required : 1;
	unsigned int token    : 31;
} MonoCustomMod;


struct _MonoType {
	union {
		void * klass; 	//MonoClass *klass; /* for VALUETYPE and CLASS */
		void * type;  	//MonoType *type;   /* for PTR */
		void * array;	//MonoArrayType *array; /* for ARRAY */
		void * method;	//MonoMethodSignature *method;
		void * generic_param;	//MonoGenericParam *generic_param; /* for VAR and MVAR */
		void * generic_class;	//MonoGenericClass *generic_class; /* for GENERICINST */
	} data;
	unsigned int attrs    : 16; /* param attributes or field flags */
	MonoTypeEnum type     : 8;  //MonoTypeEnum type     : 8;
	unsigned int num_mods : 6;  /* max 64 modifiers follow at the end */
	unsigned int byref    : 1;
	unsigned int pinned   : 1;  /* valid when included in a local var signature */
	MonoCustomMod modifiers [MONO_ZERO_LEN_ARRAY]; /* this may grow */
};

struct _MonoMethodHeader {
	const unsigned char  *code;
	int     code_size;
};

struct _MonoObject {
	MonoVTable *vtable;
	void *synchronisation;//MonoThreadsSync
};

struct _MonoClass {
	MonoClass *element_class;   /// element class for arrays and enum basetype for enums
	MonoClass *cast_class;		/// used for subtype checks
	MonoClass **supertypes;		/// for fast subtype checks
	short     idepth;
	char      rank;				/// array dimension
	int       instance_size; 	/// object instance size
	char      initedFlag;		/// bitflag
	char 	  min_align;		/// bitflag
	char      packing_size;		/// bitflag
	char      delegate;			/// bitflag
	char      interfaces_inited;/// bitflag
	char      exception_type;	/* MONO_EXCEPTION_* */
	MonoClass  *parent;
	MonoClass  *nested_in;
	void *image;
	const char *name;
	const char *name_space;
	int    type_token;
	int    vtable_size; /* number of slots */
	short  interface_count;
	short     interface_id;        /* unique inderface id (for interfaces) */
	short     max_interface_id;
	short     interface_offsets_count;
	MonoClass **interfaces_packed;
	short    *interface_offsets_packed;
	char     *interface_bitmap;
	MonoClass **interfaces;
	union {
		int class_size; /* size of area for static fields */
		int element_size; /* for array types */
		int generic_param_token; /* for generic param types, both var and mvar */
	} sizes;
	int    flags;
	struct {
		int first, count;
	} field, method;
	int ref_info_handle;
	void 	*marshal_info;

	MonoClassField *fields;
	MonoMethod **methods;

	MonoType this_arg;
	MonoType byval_arg;
	void *generic_class;	//MonoGenericClass
	void  *generic_container;//MonoGenericContainer
	void *gc_descr;
	void *runtime_info;	//MonoClassRuntimeInfo
	MonoClass *next_class_cache;// next element in the class_cache hash list (in MonoImage)
	MonoMethod **vtable;// Generic vtable. Initialized by a call to mono_class_setup_vtable ()
	void *ext;//MonoClassExt  Rarely used fields of classes
};

struct _MonoString {
	MonoObject object;
	int32_t length;
	char chars [MONO_ZERO_LEN_ARRAY];	//mono_unichar2 chars[0]
};

struct _MonoProperty {
	void *parent;	//MonoClass *parent;
	const char *name;
	MonoMethod *get;		//MonoMethod *get;
	MonoMethod *set;		//MonoMethod *set;
	unsigned int attrs;	//guint32 attrs;
};

struct _MonoVTable {
	MonoClass  *klass;
	void *gc_descr;
	void *domain;  /* each object/vtable belongs to exactly one domain */
    void*    type; /* System.Type type for klass */
	char     *interface_bitmap;
	short     max_interface_id;
	char      rank;
	unsigned int remote          : 1; /* class is remotely activated */
	unsigned int initialized     : 1; /* cctor has been run */
	unsigned int init_failed     : 1; /* cctor execution failed */
	unsigned int has_static_fields : 1; /* pointer to the data stored at the end of the vtable array */
	unsigned int gc_bits         : 4; /* Those bits are reserved for the usaged of the GC */

	int     imt_collisions_bitmap;
	void *runtime_generic_context;
	void *vtable[MONO_ZERO_LEN_ARRAY];
};

struct _MonoMethod {
	short flags;  /* method flags guint16 */
	short iflags; /* method implementation flags guint16*/
	int token;			//guint32
	void *klass;		//MonoClass
	void *signature;	//MonoMethodSignature
	/* name is useful mostly for debugging */
	const char *name;
	/* this is used by the inlining algorithm */
	unsigned int inline_info:1;
	unsigned int inline_failure:1;
	unsigned int wrapper_type:5;
	unsigned int string_ctor:1;
	unsigned int save_lmf:1;
	unsigned int dynamic:1; /* created & destroyed during runtime */
	unsigned int sre_method:1; /* created at runtime using Reflection.Emit */
	unsigned int is_generic:1; /* whenever this is a generic method definition */
	unsigned int is_inflated:1; /* whether we're a MonoMethodInflated */
	unsigned int skip_visibility:1; /* whenever to skip JIT visibility checks */
	unsigned int verification_success:1; /* whether this method has been verified successfully.*/
	/* TODO we MUST get rid of this field, it's an ugly hack nobody is proud of. */
	unsigned int is_mb_open : 1;		/* This is the fully open instantiation of a generic method_builder. Worse than is_tb_open, but it's temporary */
	signed int slot : 16;
};

struct _MonoMethodSignatureSmall {
	MonoType *ret;	//MonoType     *ret;
	unsigned char       param_count;
	char          sentinelpos;
	unsigned int  generic_param_count : 5;
	unsigned int  call_convention     : 6;
	unsigned int  hasthis             : 1;
	unsigned int  explicit_this       : 1;
	unsigned int  pinvoke             : 1;
	unsigned int  is_inflated         : 1;
	unsigned int  has_type_parameters : 1;
	MonoType     *params [MONO_ZERO_LEN_ARRAY];	//MonoType
};


struct _MonoMethodSignature {
	MonoType *ret;	//MonoType     *ret;
	unsigned short       param_count;
	short        sentinelpos;
	unsigned int  generic_param_count : 16;
	unsigned int  call_convention     : 6;
	unsigned int  hasthis             : 1;
	unsigned int  explicit_this       : 1;
	unsigned int  pinvoke             : 1;
	unsigned int  is_inflated         : 1;
	unsigned int  has_type_parameters : 1;
	MonoType     *params [MONO_ZERO_LEN_ARRAY];	//MonoType
};


struct _MonoClassField {
	MonoType *type;
	const char *name;
	MonoClass  *parent;
	int    offset;
};


struct _MonoEvent {
	void *parent;
	const char *name;
	MonoMethod *add;
	MonoMethod *remove;
	MonoMethod *raise;
};

typedef void (*MonoDomainFunc) (void *domain, void *user_data);


#define FIELD_ATTRIBUTE_FIELD_ACCESS_MASK     0x0007
#define FIELD_ATTRIBUTE_COMPILER_CONTROLLED   0x0000
#define FIELD_ATTRIBUTE_PRIVATE               0x0001
#define FIELD_ATTRIBUTE_FAM_AND_ASSEM         0x0002
#define FIELD_ATTRIBUTE_ASSEMBLY              0x0003
#define FIELD_ATTRIBUTE_FAMILY                0x0004
#define FIELD_ATTRIBUTE_FAM_OR_ASSEM          0x0005
#define FIELD_ATTRIBUTE_PUBLIC                0x0006

#define FIELD_ATTRIBUTE_STATIC                0x0010
#define FIELD_ATTRIBUTE_INIT_ONLY             0x0020
#define FIELD_ATTRIBUTE_LITERAL               0x0040
#define FIELD_ATTRIBUTE_NOT_SERIALIZED        0x0080
#define FIELD_ATTRIBUTE_SPECIAL_NAME          0x0200
#define FIELD_ATTRIBUTE_PINVOKE_IMPL          0x2000

/* For runtime use only */
#define FIELD_ATTRIBUTE_RESERVED_MASK         0x9500
#define FIELD_ATTRIBUTE_RT_SPECIAL_NAME       0x0400
#define FIELD_ATTRIBUTE_HAS_FIELD_MARSHAL     0x1000
#define FIELD_ATTRIBUTE_HAS_DEFAULT           0x8000
#define FIELD_ATTRIBUTE_HAS_FIELD_RVA         0x0100


#define METHOD_IMPL_ATTRIBUTE_CODE_TYPE_MASK       0x0003
#define METHOD_IMPL_ATTRIBUTE_IL                   0x0000
#define METHOD_IMPL_ATTRIBUTE_NATIVE               0x0001
#define METHOD_IMPL_ATTRIBUTE_OPTIL                0x0002
#define METHOD_IMPL_ATTRIBUTE_RUNTIME              0x0003

#define METHOD_IMPL_ATTRIBUTE_MANAGED_MASK         0x0004
#define METHOD_IMPL_ATTRIBUTE_UNMANAGED            0x0004
#define METHOD_IMPL_ATTRIBUTE_MANAGED              0x0000

#define METHOD_IMPL_ATTRIBUTE_FORWARD_REF          0x0010
#define METHOD_IMPL_ATTRIBUTE_PRESERVE_SIG         0x0080
#define METHOD_IMPL_ATTRIBUTE_INTERNAL_CALL        0x1000
#define METHOD_IMPL_ATTRIBUTE_SYNCHRONIZED         0x0020
#define METHOD_IMPL_ATTRIBUTE_NOINLINING           0x0008
#define METHOD_IMPL_ATTRIBUTE_NOOPTIMIZATION       0x0040
#define METHOD_IMPL_ATTRIBUTE_MAX_METHOD_IMPL_VAL  0xffff
#define METHOD_IMPL_ATTRIBUTE_AGGRESSIVE_INLINING  0x0100

#define METHOD_ATTRIBUTE_MEMBER_ACCESS_MASK        0x0007
#define METHOD_ATTRIBUTE_COMPILER_CONTROLLED       0x0000
#define METHOD_ATTRIBUTE_PRIVATE                   0x0001
#define METHOD_ATTRIBUTE_FAM_AND_ASSEM             0x0002
#define METHOD_ATTRIBUTE_ASSEM                     0x0003
#define METHOD_ATTRIBUTE_FAMILY                    0x0004
#define METHOD_ATTRIBUTE_FAM_OR_ASSEM              0x0005
#define METHOD_ATTRIBUTE_PUBLIC                    0x0006

#define METHOD_ATTRIBUTE_STATIC                    0x0010
#define METHOD_ATTRIBUTE_FINAL                     0x0020
#define METHOD_ATTRIBUTE_VIRTUAL                   0x0040
#define METHOD_ATTRIBUTE_HIDE_BY_SIG               0x0080

#define METHOD_ATTRIBUTE_VTABLE_LAYOUT_MASK        0x0100
#define METHOD_ATTRIBUTE_REUSE_SLOT                0x0000
#define METHOD_ATTRIBUTE_NEW_SLOT                  0x0100

#define METHOD_ATTRIBUTE_STRICT                    0x0200
#define METHOD_ATTRIBUTE_ABSTRACT                  0x0400
#define METHOD_ATTRIBUTE_SPECIAL_NAME              0x0800

#define METHOD_ATTRIBUTE_PINVOKE_IMPL              0x2000
#define METHOD_ATTRIBUTE_UNMANAGED_EXPORT          0x0008

/*
 * For runtime use only
 */
#define METHOD_ATTRIBUTE_RESERVED_MASK             0xd000
#define METHOD_ATTRIBUTE_RT_SPECIAL_NAME           0x1000
#define METHOD_ATTRIBUTE_HAS_SECURITY              0x4000
#define METHOD_ATTRIBUTE_REQUIRE_SEC_OBJECT        0x8000
extern void            (*g_free)(void *);
extern void           *(*mono_get_root_domain)(void);
extern void            (*mono_add_internal_call)(const char *, void *);
extern void           *(*mono_reflection_type_from_name)(char *, void *);
extern void           *(*mono_image_open_from_data_with_name)(char *, unsigned int, int , int *, int, const char *);

extern void           *(*mono_domain_assembly_open)(void *, const char *);
extern void           *(*mono_domain_get)(void);
extern void            (*mono_domain_foreach)(MonoDomainFunc, void *);

extern void           *(*mono_thread_attach)(void *);
extern void            (*mono_thread_detach)(void *);

extern void           *(*mono_assembly_get_image)(void *);
extern void           *(*mono_assembly_open)(const char *, int *);
extern void           *(*mono_assembly_load_from_full)(void *, const char *, int *, int);

extern void           *(*mono_object_new)(void *, void *);
extern void           *(*mono_object_get_class)(void *);
extern void           *(*mono_object_unbox)(void *monoobject);
extern char           *(*mono_string_to_utf8)(void *);
extern void           *(*mono_string_new)(void *, const char *);
extern void           *(*mono_string_new_wrapper)(const char *);

extern void            (*mono_runtime_object_init)(void *);
extern MonoObject     *(*mono_runtime_invoke)(MonoMethod *, void *, void **, void **);

extern char           *(*mono_class_get_name)(void *);
extern MonoClassField *(*mono_class_get_field_from_name)(void *monoclass, const char *name);
extern MonoClassField *(*mono_class_get_fields)(void *monoclass, void **iter);
extern MonoMethod     *(*mono_class_get_methods)(void *monoclass, void **iter);
extern void           *(*mono_class_get_properties)(void *monoclass, void **iter);
extern void           *(*mono_class_get_events)(void *monoclass, void **iter);

extern void           *(*mono_class_get_method_from_name)(void *, const char *, int);
extern MonoProperty   *(*mono_class_get_property_from_name)(void *, const char *);	// return MonoProperty
extern int             (*mono_class_num_fields)(void *monoclass);
extern int             (*mono_class_num_methods)(void *monoclass);
extern int             (*mono_class_num_properties)(void *monoclass);
extern void           *(*mono_class_vtable)(void *domain, void *kclass);
extern void           *(*mono_class_vtable_full)(void *domain, void *kclass, bool raise_on_error);

extern void           *(*mono_class_from_mono_type)(void *);
extern MonoClass      *(*mono_class_from_name)(void *, const char *, const char *);

extern void *(*mono_method_get_last_managed)(void);
extern void *(*mono_method_get_object)(void *domain, void *method, void *refclass);
extern void *(*mono_method_desc_new)(const char *, int);
extern void  (*mono_method_desc_free)(void *);
extern void *(*mono_method_desc_search_in_class)(void *, void *);
extern void *(*mono_method_signature)(void *method);

extern void  (*mono_field_get_value)(void *thisObj, void *monofield, void *value);
extern void *(*mono_field_get_value_object)(void *domain, void *monofield, void *thisObj);
extern void *(*mono_field_set_value)(void *thisarg, void *monofield, void *value);
extern void  (*mono_field_static_set_value)(void *vt, void *monofield, void *value);
extern void  (*mono_field_static_get_value)(void *vt, void *monofield, void *value);

extern char *(*mono_signature_get_desc)(void *signature, int);
extern void *(*mono_signature_get_params)(void *signature, void **iter);	//return MonoType

extern MonoMethodHeader *(*mono_method_get_header)(MonoMethod *method);
extern char *(*mono_disasm_code) (void *MonoDisHelper, MonoMethod *method, const unsigned char *ip, const unsigned char* end);
extern void *(*mono_compile_method) (MonoMethod *method);
#endif   
