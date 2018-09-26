//
// Created by Tony on 2018/5/22.
//
#include <jni.h>

#ifndef MAGICWALLET_ANDROID_NATIVE_CHECK_H
#define MAGICWALLET_ANDROID_NATIVE_CHECK_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_com_viet_news_core_utils_EncryptUtils_nativeCheck(JNIEnv *env, jobject);

#ifdef __cplusplus
}
#endif

#endif
