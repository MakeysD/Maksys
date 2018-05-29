package com.example.duan.chao2.DCZ_util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by DELL on 2017/9/20.
 */

public class AsterPassword extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }
        public char charAt(int index) {
            return '*';
        }
        public int length() {
            return mSource.length();
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
