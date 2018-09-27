//
// Created by Tony on 2018/5/21.
//

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <string>
#include <cstring>
#include <sys/ptrace.h>
#include "sha256.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "security", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "security", __VA_ARGS__))

const char *RELEASE_SIGN = "30820255308201bea00302010202045461d0c1300d06092a864886f70d0101050500306f310b300906035504061302636e3111300f060355040813085368616e674861693111300f060355040713085368616e6748616931123010060355040a13095a58496e736967687431123010060355040b13095a58496e736967687431123010060355040313094161726f6e2e4c6975301e170d3134313131313039303235375a170d3339313130353039303235375a306f310b300906035504061302636e3111300f060355040813085368616e674861693111300f060355040713085368616e6748616931123010060355040a13095a58496e736967687431123010060355040b13095a58496e736967687431123010060355040313094161726f6e2e4c697530819f300d06092a864886f70d010101050003818d00308189028181008099a3c80816147c921b59f20159bb688aac1e6d730cc177a5454bfe46635201ad2097456ac539ee093e1fe0e867ce63e14c48f15e8854fc5d79e42f27dee4e8f2852c384cdaad394ac70809c908cca7f8fc32763969360b06261a9bc8eb2080fed91d73137696d0b629cd9e8e775925a6cd6a7cec416b5260bc024d80b4f4690203010001300d06092a864886f70d01010505000381810035d9b7a787090c699f43a65363cd0f6ab33486181e7b793f4aab5b9679073a5d2dddb9579a08739be06652e87dab48858d55793aabe6a7a92d1df337a133854ff9fe1a76db05a532aebca2441803c2f3cf60a1276c3dd9235d08ee9fdd95e997caf1517be9a34392c873a8aa5ec1ca6ad02b0d21f4dfefc0c13ee351baaf0105";

const char *SALT_CN_RELEASE = "df6e7e9a333d4a37bdf9c2d539dd0780"; // 国内正式
const char *SALT_CN_TEST = "96a538b436014231881f951233a36fe5"; // 国内测式
const char *SALT_RELEASE = "b392a6141d154ac0ac77e59ba6e3377d"; // 国外正式
const char *SALT_TEST = "96a538b436014231881f951233a36fe5"; // 国外测式

extern "C"
JNIEXPORT jstring JNICALL
Java_com_viet_news_core_utils_EncryptUtils_encryptPayPassword(JNIEnv *env, jobject,
                                                              jstring old_string) {

    const char *str = env->GetStringUTFChars(old_string, 0);

    std::string output = sha256(str);

    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(output.c_str());

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_viet_news_core_utils_EncryptUtils_encryptPayPasswordWithSalt(JNIEnv *env, jobject,
                                                                      jstring old_string,
                                                                      jint type) {

    const char *str = env->GetStringUTFChars(old_string, 0);
    const char *salt;

    switch (type) {
        case 1:
            salt = SALT_CN_RELEASE;
            break;
        case 2:
            salt = SALT_CN_TEST;
            break;
        case 3:
            salt = SALT_RELEASE;
            break;
        case 4:
            salt = SALT_TEST;
            break;
    }

    //拼接两个字符串
    std::string const &input = std::string(str) + std::string(salt);

    std::string output = sha256(input);

    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(output.c_str());

}

static int verifySign(JNIEnv *env);

jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }
    if (verifySign(env) == JNI_OK) {
        return JNI_VERSION_1_4;
    }
    LOGE("签名不一致!");
    return JNI_ERR;
}

static jobject getApplication(JNIEnv *env) {
    jobject application = NULL;
    jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz != NULL) {
        jmethodID currentApplication = env->GetStaticMethodID(
                activity_thread_clz, "currentApplication", "()Landroid/app/Application;");
        if (currentApplication != NULL) {
            application = env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        env->DeleteLocalRef(activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }

    return application;
}

static int verifySign(JNIEnv *env) {
    // Application object
    jobject application = getApplication(env);
    if (application == NULL) {
        return JNI_ERR;
    }
    // Context(ContextWrapper) class
    jclass context_clz = env->GetObjectClass(application);
    // getPackageManager()
    jmethodID getPackageManager = env->GetMethodID(context_clz, "getPackageManager",
                                                   "()Landroid/content/pm/PackageManager;");
    // android.content.pm.PackageManager object
    jobject package_manager = env->CallObjectMethod(application, getPackageManager);
    // PackageManager class
    jclass package_manager_clz = env->GetObjectClass(package_manager);
    // getPackageInfo()
    jmethodID getPackageInfo = env->GetMethodID(package_manager_clz, "getPackageInfo",
                                                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // context.getPackageName()
    jmethodID getPackageName = env->GetMethodID(context_clz, "getPackageName",
                                                "()Ljava/lang/String;");
    // call getPackageName() and cast from jobject to jstring
    jstring package_name = (jstring) (env->CallObjectMethod(application, getPackageName));
    // PackageInfo object
    jobject package_info = env->CallObjectMethod(package_manager, getPackageInfo, package_name, 64);
    // class PackageInfo
    jclass package_info_clz = env->GetObjectClass(package_info);
    // field signatures
    jfieldID signatures_field = env->GetFieldID(package_info_clz, "signatures",
                                                "[Landroid/content/pm/Signature;");
    jobject signatures = env->GetObjectField(package_info, signatures_field);
    jobjectArray signatures_array = (jobjectArray) signatures;
    jobject signature0 = env->GetObjectArrayElement(signatures_array, 0);
    jclass signature_clz = env->GetObjectClass(signature0);

    jmethodID toCharsString = env->GetMethodID(signature_clz, "toCharsString",
                                               "()Ljava/lang/String;");
    // call toCharsString()
    jstring signature_str = (jstring) (env->CallObjectMethod(signature0, toCharsString));

    // release
    env->DeleteLocalRef(application);
    env->DeleteLocalRef(context_clz);
    env->DeleteLocalRef(package_manager);
    env->DeleteLocalRef(package_manager_clz);
    env->DeleteLocalRef(package_name);
    env->DeleteLocalRef(package_info);
    env->DeleteLocalRef(package_info_clz);
    env->DeleteLocalRef(signatures);
    env->DeleteLocalRef(signature0);
    env->DeleteLocalRef(signature_clz);

    const char *sign = env->GetStringUTFChars(signature_str, NULL);
    if (sign == NULL) {
        LOGE("分配内存失败");
        return JNI_ERR;
    }

    //LOGI("应用中读取到的签名为：%s", sign);
    //LOGI("native中预置的签名为：%s", RELEASE_SIGN);
    int result = strcmp(sign, RELEASE_SIGN);
    // 使用之后要释放这段内存
    env->ReleaseStringUTFChars(signature_str, sign);
    env->DeleteLocalRef(signature_str);
    if (result == 0) { // 签名一致
        return JNI_OK;
    }

    return JNI_ERR;
}

jstring Java_com_viet_news_core_utils_EncryptUtils_nativeCheck(JNIEnv *env, jobject) {

    ptrace(PTRACE_TRACEME, 0, 0, 0); // 实现反动态调试
    return env->NewStringUTF("Security str from native.");
}