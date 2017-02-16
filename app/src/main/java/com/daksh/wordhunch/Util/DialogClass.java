package com.daksh.wordhunch.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.WindowManager;

public class DialogClass {

    /**
     * A Rectangle shaped progress Dialog box
     */
    public static ProgressDialog dialog;

    /**
     * A square shared Progress Dialog Box
     */
    public static BirdDialog birdDialog;

    /**
     * Show the square shaped dialog box with an animated GIF in the center
     * @param ctx Context of the calling activity
     */
    public static void showBirdDialog(Activity ctx) {
        try {
            Log.e("dialog class showbird", ctx.getClass().getSimpleName());
            if (birdDialog == null) {
                birdDialog = new BirdDialog(ctx);
                birdDialog.setCancelable(false);
            }

            if (!birdDialog.isShowing())
                birdDialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
            try {
                //WindowTokenException is thrown when the context using which the Dialog (static) was created
                //no longer exists and is required to be drawn again. In such a case, due context
                //mismatch, the BadToken exception is thrown. To rectify this, the old instance of Dialog
                //is nullified and re-instantiated.
                birdDialog = null;
                showBirdDialog(ctx);
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
                //No idea how to handle it right now.
                //Description as provided by fabric :
                //Failed to allocate a 44 byte allocation with 2112 free bytes and 2112B until OOM;
                //failed due to fragmentation (required continguous free 4096 bytes for a new buffer where largest contiguous free 0 bytes)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss the square shaped progress dialog box
     * @param ctx Context of the calling activity
     */
    public static void dismissBirdDialog(Activity ctx) {
        try {
            Log.e("dialog class dismisbird", ctx.getClass().getSimpleName());
            if (birdDialog != null)
                birdDialog.dismiss();

            //Nullify Bird dialog to ensure Bird Dialog is reinstantiated when 'show' is called again
            birdDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
