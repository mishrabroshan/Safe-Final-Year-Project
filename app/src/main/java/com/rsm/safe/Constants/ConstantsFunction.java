package com.rsm.safe.Constants;

import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConstantsFunction {

    public static void setTransitionDuration(Window window){
        window.getSharedElementEnterTransition().setDuration(ConstantsVariables.TRANSITIONDURATION);
        window.getSharedElementExitTransition().setDuration(ConstantsVariables.TRANSITIONDURATION).setInterpolator(new DecelerateInterpolator());
    }

    public static void removeFade(Window window){
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        window.setExitTransition(fade);
        window.setEnterTransition(fade);
    }

    public static void startActivity(Context PackageContext, Class activity){
        Intent intent = new Intent(PackageContext, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageContext.startActivity(intent);
    }

    public static boolean whereToGo(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
            return false;

        if (currentUser.getPhoneNumber() == null || currentUser.getPhotoUrl() == null)
            return false;

        return true;
    }
}
