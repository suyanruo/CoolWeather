package com.zhangjian.coolweather.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {
    public static void setRootFragment(AppCompatActivity context, int resId, Fragment rootF) {
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(resId, rootF, rootF.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void enterNewFragment(AppCompatActivity context, int resId, Fragment currentF, Fragment newF) {
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(currentF);
        transaction.add(resId, newF, newF.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static Fragment getLastFragment(FragmentActivity context) {
        if (context == null) {
            return null;
        }
        List<Fragment> fragments = getRealFragments(context);
        int size = fragments.size();
        if (size > 0) {
            return fragments.get(size - 1);
        }
        return null;
    }

    public static List<Fragment> getRealFragments(FragmentActivity context) {
        List<Fragment> fragments = context.getSupportFragmentManager().getFragments();
        List<Fragment> realFragments = new ArrayList<>();
        if (fragments == null) {
            return realFragments;
        }
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) != null) {
                realFragments.add(fragments.get(i));
            }
        }
        return realFragments;
    }
}
