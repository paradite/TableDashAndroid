package thack.ac.tabledash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to provide helper methods for building alert dialogs
 * Created by paradite on 17/8/14.
 */
public class AlertDialogHelper {
    public static final String TAG = "AlertDialogHelper";
    /**
     * Method to get a EditText with hint
     * @param context   Context
     * @param hint      Hint to be displayed
     * @param maxLength Max input allowed for the EditText
     * @return EditText
     */
    public static EditText getEditTextWithHint(Context context, String hint, int maxLength) {
        final EditText mEditText = new EditText(context);
        mEditText.setHint(hint);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        return mEditText;
    }

    /**
     * Method to get a EditText with text
     * @param context   Context
     * @param text      Text to be displayed initially
     * @param maxLength Max input allowed for the EditText
     * @return EditText
     */
    public static EditText getEditTextWithText(Context context, String text, int maxLength) {
        final EditText mEditText = new EditText(context);
        mEditText.setText(text);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        return mEditText;
    }

    /**
     * Set the view of dialog for check in
     * @param alert     AlertDialog
     */
    public static void setCheckInDialog(final Activity activity, final AlertDialog.Builder alert, LinearLayout ll) {


    }

    /**
     * Set up the view with one message
     * @param context   Context
     * @param alert     AlertDialog
     * @param message   Message
     */
    public static void setDialogViewMessage(Context context, AlertDialog.Builder alert, String message){
        alert.setMessage(message);
    }

    /**
     * Set up the view with two messages
     * @param context   Context
     * @param alert     AlertDialog
     * @param message1  Message 1
     * @param message2  Message 2
     */
    public static void setDialogViewMessage(Context context, AlertDialog.Builder alert, String message1, String message2){
//        Log.e("setDialogViewMessage", "setDialogViewMessage");
        LinearLayout ll=new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(20, 10, 20, 10);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(layoutParams);
        TextView messageView1 = new TextView(context);
        TextView messageView2 = new TextView(context);
        TextView messageView3 = new TextView(context);
        messageView1.setLayoutParams(layoutParams);
        messageView2.setLayoutParams(layoutParams);
        messageView3.setLayoutParams(layoutParams);
        messageView1.setText(message1);
        messageView2.setText(message2);
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        messageView3.setText("Card Safe Version " + version);
        ll.addView(messageView1);
        ll.addView(messageView2);
        ll.addView(messageView3);
        alert.setView(ll);

    }

}
