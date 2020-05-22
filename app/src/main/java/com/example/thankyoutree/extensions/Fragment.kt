package com.example.thankyoutree.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.thankyoutree.R


fun Fragment.add(
    fragment: Fragment,
    resContainer: Int = R.id.content,
    addToBackStack: Boolean = true,
    backStackName: String = fragment.javaClass.canonicalName ?: ""
) {
    fragmentManager?.beginTransaction()?.apply {
        if (addToBackStack) addToBackStack(backStackName)
        add(resContainer, fragment, backStackName)
        commit()
    }
}

fun Fragment.replace(
    fragment: Fragment,
    resContainer: Int = R.id.content,
    addToBackStack: Boolean = true,
    backStackName: String = fragment.javaClass.canonicalName ?: "",
    withStateLoss: Boolean = true
) {
    fragmentManager?.beginTransaction()?.apply {
        if (addToBackStack) addToBackStack(backStackName)
        replace(resContainer, fragment, backStackName)
        if (withStateLoss) commitAllowingStateLoss()
        else commit()
    }
}