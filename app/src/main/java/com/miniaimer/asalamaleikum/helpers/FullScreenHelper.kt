package ccom.miniaimer.asalamaleikum.helpers

import android.app.Activity
import android.view.View

public final class FullScreenHelper {
    companion object{
        @JvmStatic fun setFullScreenOnWindowFocusChanged(activity : Activity, hashFocus : Boolean){
            if(hashFocus){
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            }
        }
    }
}