//
//  MethodSwizzle.m
//
//  Copyright (c) 2006 Tildesoft. All rights reserved.
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
//  DEALINGS IN THE SOFTWARE.

// Implementation of Method Swizzling, inspired by
// http://www.cocoadev.com/index.pl?MethodSwizzling

// solves the inherited method problem

// 2007-9-10:	modified by Jon Gotow (gotow@stclairsoft.com) to use Obj-C 2.0 
//				api when it's available.

#import "MethodSwizzle.h"
#import <stdlib.h>
#import <string.h>
#import <objc/objc-runtime.h>
#import <objc/objc-class.h>
#import <uuid/uuid.h>

static BOOL _PerformSwizzle(Class klass, SEL origSel, SEL altSel, BOOL forInstance);

// ------------------------------------------------------------------------------------
#if MAC_OS_X_VERSION_MIN_REQUIRED < 1050
	// If the Obj-C 2.0 API calls are weak linked, these routines call the new runtime 
	// API if it's avaialable or implement it if it's not.
	static BOOL _class_addMethod(Class cls, SEL name, IMP imp, const char *types);
	static void _method_exchangeImplementations(Method origMethod, Method altMethod);
	static SEL _method_getName(Method m);
	static IMP _method_getImplementation(Method m);
	static const char *_method_getTypeEncoding(Method m);
#else
	// If we're guaranteed to be running on 10.5 or higher, just map the names to the
	// real API calls, since we know they'll be available.
	#define _class_addMethod				class_addMethod
	#define _method_exchangeImplementations	method_exchangeImplementations
	#define _method_getName					method_getName
	#define _method_getImplementation		method_getImplementation
	#define _method_getTypeEncoding			method_getTypeEncoding
#endif

// ------------------------------------------------------------------------------------
BOOL ClassMethodSwizzle(Class klass, SEL origSel, SEL altSel) {
	return _PerformSwizzle(klass, origSel, altSel, NO);
}

// ------------------------------------------------------------------------------------
BOOL MethodSwizzle(Class klass, SEL origSel, SEL altSel) {
	return _PerformSwizzle(klass, origSel, altSel, YES);
}

// ------------------------------------------------------------------------------------
// if the origSel isn't present in the class, pull it up from where it exists
// then do the swizzle
BOOL _PerformSwizzle(Class klass, SEL origSel, SEL altSel, BOOL forInstance) {
    // First, make sure the class isn't nil
	if (klass != nil) {
		Method origMethod = NULL, altMethod = NULL;
		int i;
		
		// Next, look for the methods
		unsigned method_count = 0;
		Class iterKlass = (forInstance ? klass : klass->isa);
#if defined(OBJC_API_VERSION) && OBJC_API_VERSION >= 2
		Method* method_list = class_copyMethodList(iterKlass, &method_count);
#else
		void *iterator = NULL;
		struct objc_method_list *mlist = class_nextMethodList(iterKlass, &iterator);
		while (mlist != NULL) {		
			Method *method_list = (Method *)mlist->method_list;
			method_count = mlist->method_count;
#endif
			for (i = 0; i < method_count; ++i) {
				if (_method_getName(method_list[i]) == origSel) {
					origMethod = method_list[i];
					break;
				}
				if (_method_getName(method_list[i]) == altSel) {
					altMethod = method_list[i];
					break;
				}
			}
#if defined(OBJC_API_VERSION) && OBJC_API_VERSION >= 2
			free(method_list);
#else
			mlist = class_nextMethodList(iterKlass, &iterator);
		}
#endif
		if (origMethod == NULL || altMethod == NULL) {
			// one or both methods are not in the immediate class
			// try searching the entire hierarchy
			// remember, iterKlass is the class we care about - klass || klass->isa
			// class_getInstanceMethod on a metaclass is the same as class_getClassMethod on the real class
			BOOL pullOrig = NO, pullAlt = NO;
			if (origMethod == NULL) {
				origMethod = class_getInstanceMethod(iterKlass, origSel);
				pullOrig = YES;
			}
			if (altMethod == NULL) {
				altMethod = class_getInstanceMethod(iterKlass, altSel);
				pullAlt = YES;
			}
			
			// die now if one of the methods doesn't exist anywhere in the hierarchy
			// this way we won't make any changes to the class if we can't finish
			if (origMethod == NULL || altMethod == NULL) {
				return NO;
			}
			
			// pull up one or both of the methods as needed
			if (pullOrig) {
				_class_addMethod(iterKlass, 
								_method_getName(origMethod), 
								_method_getImplementation(origMethod),
								_method_getTypeEncoding(origMethod));
				origMethod = class_getInstanceMethod(iterKlass, origSel);
			}
			if (pullAlt) {
				_class_addMethod(iterKlass, 
								_method_getName(altMethod), 
								_method_getImplementation(altMethod),
								_method_getTypeEncoding(altMethod));
				altMethod = class_getInstanceMethod(iterKlass, altSel);
			}
		}
		
		// now swizzle
		_method_exchangeImplementations(origMethod, altMethod);

		return YES;
	}
	return NO;
}


#if MAC_OS_X_VERSION_MIN_REQUIRED < 1050
// ------------------------------------------------------------------------------------
// Implement routines that were added in OBJ_C 2.0
// ------------------------------------------------------------------------------------
static BOOL _class_addMethod(Class cls, SEL name, IMP imp, const char *types) {
	if(class_addMethod)
		return class_addMethod(cls, name, imp, types);
	else {
		struct objc_method_list *mlist = malloc(sizeof(struct objc_method_list));
		mlist->obsolete = NULL;
		mlist->method_count = 1;
		mlist->method_list[0].method_name = name;
		mlist->method_list[0].method_types = (char *)types;
		mlist->method_list[0].method_imp = imp;
		class_addMethods(cls, mlist);
		return TRUE;
	}
}

// ------------------------------------------------------------------------------------
static void _method_exchangeImplementations(Method origMethod, Method altMethod) {
	if(method_exchangeImplementations)
		method_exchangeImplementations(origMethod, altMethod);
	else {
		IMP temp = origMethod->method_imp;
		origMethod->method_imp = altMethod->method_imp;
		altMethod->method_imp = temp;
	}
}

// ------------------------------------------------------------------------------------
static SEL _method_getName(Method m) {
	if(method_getName)
		return method_getName(m);
	else
		return m->method_name;
}

// ------------------------------------------------------------------------------------
static IMP _method_getImplementation(Method m) {
	if(method_getImplementation)
		return method_getImplementation(m);
	else
		return m->method_imp;
}

// ------------------------------------------------------------------------------------
static const char *_method_getTypeEncoding(Method m) {
	if(method_getTypeEncoding)
		return method_getTypeEncoding(m);
	else
		return m->method_types;
}

#endif