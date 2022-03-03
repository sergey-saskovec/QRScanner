package com.codescannerqr.generator.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.view.activity.FreeTrialActivity;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.view.fragments.bottomNavigate.HistoryBottomFragment;
import com.codescannerqr.generator.view.fragments.bottomNavigate.ScannerQRFragment;
import com.codescannerqr.generator.view.fragments.bottomNavigate.SettingsBottomFragment;
import com.codescannerqr.generator.view.fragments.editGeneratedCodes.GeneratedFragment;
import com.codescannerqr.generator.view.fragments.editGeneratedCodes.StyleEditFragment;
import com.codescannerqr.generator.view.fragments.generateCodes.CellPhoneFragment;
import com.codescannerqr.generator.view.fragments.generateCodes.ContactFragment;
import com.codescannerqr.generator.view.fragments.generateCodes.MyCardFragment;
import com.codescannerqr.generator.view.fragments.generateCodes.SMSFragment;
import com.codescannerqr.generator.view.fragments.styleCodes.StylePreviewFragment;

import java.util.Locale;
import java.util.Objects;

public class ViewDialog {

    private static Fragment fragment;

    public void showDialogFilePermission(Activity activity, Fragment fragment, String stringFr) {
        ViewDialog.fragment = fragment;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_file_permission);

        TextView buttonAllowFilePermission = dialog.findViewById(R.id.buttonAllowFilePermission);

        buttonAllowFilePermission.setOnClickListener(v -> {
            dialog.dismiss();
            switch (stringFr) {
                case "generated":
                    getGeneratedFr().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "generated"
                    );
                    break;
                case "generated2":
                    getGeneratedFr().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "generated2"
                    );
                    break;
                case "style":
                    getStyleEditFr().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                    break;
                case "stylePreview":
                    getStylePreviewFr().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                case "scanner":
                    getScannerQrFragment().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                    break;
                case "history_bottom":
                    getHistoryBottomFragment().requestFilePermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                    break;
            }

        });

        dialog.show();

    }

    public void showDialogCameraPermission(MainActivity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_camera_permission);

        TextView buttonAllowFilePermission = dialog.findViewById(R.id.buttonAllowCameraPermission);

        buttonAllowFilePermission.setOnClickListener(v -> {
            dialog.dismiss();
            activity.requestCameraPermission(Manifest.permission.CAMERA);
        });

        dialog.show();

    }

    public void showDialogContactPermission(Activity activity, Fragment fragment, String nameFr) {
        ViewDialog.fragment = fragment;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_contact_permission);

        TextView buttonAllowFilePermission = dialog.findViewById(R.id.buttonAllowContactPermission);

        buttonAllowFilePermission.setOnClickListener(v -> {
            dialog.dismiss();
            switch (nameFr) {
                case "contact":
                    getContactFr().requestContactPermission(Manifest.permission.READ_CONTACTS);
                    break;
                case "cellphone":
                    getCellPhoneFr().requestContactPermission(Manifest.permission.READ_CONTACTS);
                    break;
                case "sms":
                    getSMSFr().requestContactPermission(Manifest.permission.READ_CONTACTS);
                    break;
                case "mycard":
                    getMyCardFr().requestContactPermission(Manifest.permission.READ_CONTACTS);
                    break;
            }

        });

        dialog.show();

    }

    public void showDialogConnectWifi2(Activity activity, String SSID, String password) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_connect_wifi2);

        EditText editTextSSID = dialog.findViewById(R.id.editTextSSIDConnectWifi);
        editTextSSID.setText(SSID);
        TextInputEditText editTextPassword = dialog.findViewById(R.id.editTextPasswordConnectWifi);
        editTextPassword.setText(password);
        TextInputLayout textInputLayoutPasswordConnectWifi =
                dialog.findViewById(R.id.textInputLayoutPasswordConnectWifi);
        TextView textViewCancelConnectWifi = dialog.findViewById(R.id.textViewCancelConnectWifi);
        TextView textViewConnectWifi = dialog.findViewById(R.id.textViewConnectWifi);

        textViewConnectWifi.setOnClickListener(v ->
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        textViewCancelConnectWifi.setOnClickListener(v -> dialog.dismiss());

        textInputLayoutPasswordConnectWifi.setEndIconOnClickListener(v -> {
            MyClipboardManager.saveToClipboard(
                    Objects.requireNonNull(editTextPassword.getText()).toString(),
                    activity
            );
            Toast.makeText(activity, activity.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
        });

        dialog.show();

    }

    public static void showDialogRemoveAds(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_remove_ads);

        TextView buttonAllowRemoveADS = dialog.findViewById(R.id.buttonAllowRemoveADS);

        buttonAllowRemoveADS.setOnClickListener(v -> {
            if (!Config.premium) {
                activity.startActivity(new Intent(activity, FreeTrialActivity.class));
            } else {
                Toast.makeText(activity, activity.getString(R.string.no_ads), Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        });

        dialog.show();

    }

    public static void showDialogRateUs(Activity activity, Fragment fragment1, String fr) {
        if (!fr.equals("childClick")) {
            fragment = fragment1;
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rate_us);

        TextView buttonCancelRateUS = dialog.findViewById(R.id.textViewCancelRateUs);
        TextView buttonRateUS = dialog.findViewById(R.id.textViewRateUs);
        RatingBar ratingBar = dialog.findViewById(R.id.rating);
        ratingBar.setStepSize(1.0f);
        ratingBar.setRating(5);

        buttonCancelRateUS.setOnClickListener(v -> {
            dialog.dismiss();
            checkMethod(fr, activity);
        });
        buttonRateUS.setOnClickListener(v -> {
            dialog.dismiss();
            if (ratingBar.getRating() < 5) {
                EmailHelpers.sendEmail(
                        Config.emailFeedback,
                        "Feedback",
                        "",
                        activity
                );
                checkMethod(fr, activity);
            } else {
                checkMethod(fr, activity);
                IntentHelpers.shareIntent(activity,
                        Config.BASE_URL_GOOGLE_PLAY + activity.getPackageName());
            }

        });

        dialog.show();

    }

    private static void checkMethod(String fr, Activity activity) {
        if (fr.equals("generated")) {
            activity.finish();
        } else if (fr.equals("style")) {
            getStyleEditFr().checkPerm();
        }
    }

    public void showDialogDiscard(GenerateCodeActivity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_discard);

        TextView buttonCancelDiscard = dialog.findViewById(R.id.textViewCancelDiscard);
        TextView buttonDiscard = dialog.findViewById(R.id.textViewDiscard);

        buttonCancelDiscard.setOnClickListener(v -> dialog.dismiss());
        buttonDiscard.setOnClickListener(v -> {
            activity.navControllerGenerate.popBackStack();
            dialog.dismiss();
        });

        dialog.show();

    }

    public void showDialogLanguage(Activity activity, SettingsBottomFragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose an language");
        String[] languages = {
                "ru",
                "en",
                "pt",
                "es",
                "fr",
                "de"};

        builder.setItems(getNewListLanguage(languages), (dialog, which) -> clickLanguage(
                languages[which],
                fragment));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String[] getNewListLanguage(String[] languages) {
        String[] stringsLang = new String[languages.length];
        for (int i = 0; i < languages.length; i++) {
            stringsLang[i] = getLanguage(languages[i]);
        }
        return stringsLang;
    }

    private String getLanguage(String s) {
        Locale locale = new Locale(s);
        String lang = locale.getLanguage();
        return locale.getDisplayLanguage();

    }

    private void clickLanguage(String codeLang, SettingsBottomFragment fragment) {
        fragment.updateViews(codeLang);
    }

    private CellPhoneFragment getCellPhoneFr() {
        return (CellPhoneFragment) fragment;
    }

    private ContactFragment getContactFr() {
        return (ContactFragment) fragment;
    }

    private SMSFragment getSMSFr() {
        return (SMSFragment) fragment;
    }

    private MyCardFragment getMyCardFr() {
        return (MyCardFragment) fragment;
    }

    private GeneratedFragment getGeneratedFr() {
        return (GeneratedFragment) fragment;
    }

    private static StyleEditFragment getStyleEditFr() {
        return (StyleEditFragment) fragment;
    }

    private StylePreviewFragment getStylePreviewFr() {
        return (StylePreviewFragment) fragment;
    }

    private ScannerQRFragment getScannerQrFragment() {
        return (ScannerQRFragment) fragment;
    }

    private HistoryBottomFragment getHistoryBottomFragment() {
        return (HistoryBottomFragment) fragment;
    }


}
