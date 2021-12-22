#include <jni.h>
#include <string>

#include <cmath>
#include <iostream>

using namespace std;

string decryptKey(string s) {
    for (int i = 0; (i < 100 && s[i] != '\0'); i++)
        s[i] = s[i] - 2;

    return s;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptId(JNIEnv *env, jobject /* this */) {
    string s = "8IdG:fjq|73;n4PaH;;UvsqQu8Veoo3tZifc6saatKy";
    return env->NewStringUTF(decryptKey(s).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptSecret(JNIEnv *env, jobject /* this */) {
    string s = "ac{hKo9DgWCjz4Y3QWsk42hyQ5wPzhq3Suv{MnHEiJy";
    return env->NewStringUTF(decryptKey(s).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptIVKey(JNIEnv *env, jobject /* this */) {
    string s = "3%567cu::9kq'54B";
    return env->NewStringUTF(decryptKey(s).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptSecreteKey(JNIEnv *env, jobject /* this */) {
    string s = "CDEFGH34#GT(678GC,EFGH3(5GTF67B%";
    return env->NewStringUTF(decryptKey(s).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptAlgorithm(JNIEnv *env, jobject /* this */) {
    string s = "CGU";
    return env->NewStringUTF(decryptKey(s).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_kks_nimbletest_util_CustomKeyGenerator_decryptTransformation(JNIEnv *env,
                                                                       jobject /* this */) {
    string s = "CGU1EDE1RMEU7Rcffkpi";
    return env->NewStringUTF(decryptKey(s).c_str());
}