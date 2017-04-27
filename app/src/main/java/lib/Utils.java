package lib;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * Created by Caio Gaspar on 17/11/2015.
 */
public class Utils {

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() >= 8) {
            return true;
        }
        return false;
    }

    public static AlertDialog alert(Activity activity, String message, String alerta) {
        if (alerta.contentEquals("")) {
            alerta = "Alerta";
        }
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message).setTitle(alerta);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        return dialog;
    }

}
