package com.viet.news.core.config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.viet.news.core.R;

/**
 * fragment操作类
 */
public class FragmentExchangeManager {
    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        replaceFragment(fragmentManager, targetFragment, tag, android.R.id.content);
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, int content) {
        replaceFragment(fragmentManager, targetFragment, targetFragment.getClass().getName(), content);
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        initFragment(fragmentManager, targetFragment, tag, android.R.id.content);
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if (fragment != null) {
            transaction.hide(fragment);
        }
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentWithoutHide(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag, boolean needAnimotion) {
        if (needAnimotion) {
            addFragment(supportFragmentManager, baseDialogFragment, content, tag, R.anim.dialog_push_bottom_in, R.anim.dialog_push_bottom_out, R.anim.dialog_push_top_in, R.anim.dialog_push_top_out);
        } else {
            addFragment(supportFragmentManager, baseDialogFragment, content, tag);
        }
    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag, int animIn, int animOut, int animCloseIn, int animCloseOut) {
        if (animIn < 0) {
            animIn = 0;
        }
        if (animOut < 0) {
            animOut = 0;
        }
        if (animCloseIn < 0) {
            animCloseIn = 0;
        }
        if (animCloseOut < 0) {
            animCloseOut = 0;
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, animCloseIn, animCloseOut);
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if (fragment != null) {
            transaction.hide(fragment);
        }
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        // TODO Auto-generated method stub
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(FragmentManager fragmentManager, String tag) {
        // TODO Auto-generated method stub
        if (fragmentManager != null) {
            try {
                if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                    fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
            if (targetFragment != null) {
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment targetFragment) {
        // TODO Auto-generated method stub
        if (fragmentManager != null) {
            String tag = targetFragment.getTag();
            try {
                if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                    fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            } catch (Exception e) {
                // TODO: handle exception
            }
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(fragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }
    }
}
