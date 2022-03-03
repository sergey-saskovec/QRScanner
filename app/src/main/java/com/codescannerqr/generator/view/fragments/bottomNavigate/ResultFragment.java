package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.ButtonsRecyclerViewAdapter;
import com.codescannerqr.generator.adapter.ResultAdapter;
import com.codescannerqr.generator.databinding.FragmentResultBinding;
import com.codescannerqr.generator.helpers.AdSizeHelpers;
import com.codescannerqr.generator.helpers.DateTimeHelpers;
import com.codescannerqr.generator.helpers.EmailHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.MyClipboardManager;
import com.codescannerqr.generator.helpers.ResultRecyclerHelper;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.model.ResultList;
import com.codescannerqr.generator.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private List<ResultList> listsResultField;
    private String nameFragment;
    private String defText;
    private String argsQR;
    private int iconFragment;
    public static boolean aBoolean = false;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.result));
        ((MainActivity) requireActivity()).visibleButtonBack(true, true);
        ((MainActivity) requireActivity()).toolBarVisible(true);
        listsResultField = new ArrayList<>();
        if (getArguments() != null) {
            listsResultField = (List<ResultList>) getArguments().getSerializable("keyFields");
            nameFragment = getArguments().getString("nameFragment");
            defText = getArguments().getString("defaultText");
            argsQR = getArguments().getString("argsQR");
            iconFragment = getArguments().getInt("iconFragment");
            setView();
        }
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setView() {

        assert getArguments() != null;
        if (getArguments().getString("fragmentAfter") != null &&
                getArguments().getString("fragmentAfter").equals("Scanner") &&
                Config.settingsAutoSearch){
            searchButton();
        }

        binding.imageViewIconFragmentResult.setImageResource(iconFragment);
        binding.textViewNameFragmentResult.setText(nameFragment);

        binding.textViewTimeResult.setText(DateTimeHelpers.getDateTimeNow("HH:mm") + " " +
                DateTimeHelpers.getDateTimeNow("dd.MM.yyyy"));

        if (listsResultField.size() > 0) {
            binding.recyclerViewFieldResult.setVisibility(View.VISIBLE);
            binding.defaultTextResult.setVisibility(View.GONE);
            ResultAdapter resultAdapter = new ResultAdapter(listsResultField);
            binding.recyclerViewFieldResult.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerViewFieldResult.setAdapter(resultAdapter);
        } else {
            if (!defText.equals("")){
                binding.defaultTextResult.setText(defText);
            }
            else{
                binding.defaultTextResult.setText(argsQR);
            }

            binding.recyclerViewFieldResult.setVisibility(View.GONE);
            binding.defaultTextResult.setVisibility(View.VISIBLE);
        }

        //setButtons();
        if (!Config.premium){
            loadBigBanner();
        }

        setButtonsRecycler();
        binding.nestedScrollView.setVerticalScrollBarEnabled(true);
        binding.nestedScrollView.setScrollBarFadeDuration(0);

    }

    public void loadBigBanner(){
        if (aBoolean){
            AdView adView = new AdView(requireActivity());
            adView.setAdUnitId(Config.bannerID);
            adView.setAdSize(AdSizeHelpers.adSizeCustom(requireActivity()));
            binding.adViewContainerResult.removeAllViews();
            binding.adViewContainerResult.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    private void setButtonsRecycler(){
        ButtonsRecyclerViewAdapter adapter = new ButtonsRecyclerViewAdapter(
                ListHistoryHelpers.createListButtonsResult(nameFragment, requireActivity())
        );
        binding.recyclerViewResultButtons.setLayoutManager(new GridLayoutManager(
                requireActivity(),
                ResultRecyclerHelper.getRowColumns(nameFragment, requireActivity())
        ));
        binding.recyclerViewResultButtons.setAdapter(adapter);

        adapter.setOnItemClickListener((view, nameButtons) -> {
            if (nameButtons.equals(getString(R.string.search))){
                searchButton();
            }
            else if (nameButtons.equals(getString(R.string.copy))){
                copyButton();
            }
            else if (nameButtons.equals(getString(R.string.share))){
                shareButton();
            }
            else if (nameButtons.equals(getString(R.string.sms))){
                smsButton();
            }
            else if (nameButtons.equals(getString(R.string.connect))){
                connectWifi();
            }
            else if (nameButtons.equals(getString(R.string.email))){
                emailButton();
            }
            else if (nameButtons.equals(getString(R.string.add))){
                if (nameFragment.equals(getString(R.string.calendar))){
                    addEventButton();
                }
                else {
                    addContactButton();
                }
            }
            else if (nameButtons.equals(getString(R.string.dial))){
                dialButton();
            }
            else if (nameButtons.equals(getString(R.string.maps))){
                mapsButton();
            }
        });
    }

    private void addEventButton() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, listsResultField.get(0).getValue());
        intent.putExtra(CalendarContract.Events.DESCRIPTION,listsResultField.get(1).getValue());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, listsResultField.get(2).getValue());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                DateTimeHelpers.convertDateToLong(listsResultField.get(3).getValue(),
                        "HH:mm.dd.MM.yyyy"));
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                DateTimeHelpers.convertDateToLong(listsResultField.get(4).getValue(),
                        "HH:mm.dd.MM.yyyy"));
        startActivity(intent);
    }

    private void mapsButton() {
        Uri gmmIntentUri = Uri.parse("geo:" + listsResultField.get(0).getValue() +
                "," + listsResultField.get(1).getValue());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(Config.packageMaps);

        startActivity(mapIntent);

    }

    private void dialButton() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        if (nameFragment.equals(getString(R.string.cellphone))) {
            callIntent.setData(Uri.parse("tel:" + listsResultField.get(0).getValue()));
        } else if (nameFragment.equals(getString(R.string.contact)) ||
                nameFragment.equals(getString(R.string.my_card))) {
            callIntent.setData(Uri.parse("tel:" + listsResultField.get(1).getValue()));
        }
        startActivity(callIntent);
    }

    private void addContactButton() {

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        if (nameFragment.equals(getString(R.string.email))) {
            intent
                    .putExtra(ContactsContract.Intents.Insert.EMAIL, listsResultField.get(0).getValue())
                    .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_HOME);

        } else if (nameFragment.equals(getString(R.string.contact))) {
            intent
                    .putExtra(ContactsContract.Intents.Insert.EMAIL, listsResultField.get(2).getValue())
                    .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .putExtra(ContactsContract.Intents.Insert.PHONE, listsResultField.get(1).getValue())
                    .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .putExtra(ContactsContract.Intents.Insert.NAME, listsResultField.get(0).getValue());
        }
        else if (nameFragment.equals(getString(R.string.cellphone))){
            intent
                    .putExtra(ContactsContract.Intents.Insert.PHONE, listsResultField.get(0).getValue())
                    .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        }
        else if (nameFragment.equals(getString(R.string.my_card))){
            intent
                    .putExtra(ContactsContract.Intents.Insert.EMAIL, listsResultField.get(2).getValue())
                    .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .putExtra(ContactsContract.Intents.Insert.PHONE, listsResultField.get(1).getValue())
                    .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .putExtra(ContactsContract.Intents.Insert.NAME, listsResultField.get(0).getValue())
                    .putExtra(ContactsContract.Intents.Insert.POSTAL, listsResultField.get(3).getValue())
                    .putExtra(ContactsContract.Intents.Insert.COMPANY, listsResultField.get(4).getValue());
        }
        startActivity(intent);


    }

    private void emailButton() {
        if (nameFragment.equals(getString(R.string.email))) {
            EmailHelpers.sendEmail(
                    listsResultField.get(0).getValue(),
                    listsResultField.get(1).getValue(),
                    listsResultField.get(2).getValue(),
                    requireActivity()
            );
        }
        else {
            EmailHelpers.sendEmail(
                    "",
                    "",
                    argsQR,
                    requireActivity()
            );
        }
    }

    private void smsButton() {

        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        if (nameFragment.equals(getString(R.string.sms))) {
            intent.putExtra("sms_body", listsResultField.get(2).getValue());
            intent.putExtra("address", listsResultField.get(0).getValue());
        } else {
            intent.putExtra("sms_body", argsQR);
        }
        startActivity(intent);

    }

    private void searchButton() {
        String escapedQuery = "";
        try {
            escapedQuery = URLEncoder.encode(defText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("https://www.google.com/search?q=" + escapedQuery);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void shareButton() {

        ShareCompat.IntentBuilder intentBuilder = new ShareCompat.IntentBuilder(requireActivity());
        intentBuilder.setType("message/rfc822")
                .setText(argsQR)
                .setChooserTitle("chooserTitle")
                .startChooser();

    }

    private void copyButton() {
        MyClipboardManager.saveToClipboard(argsQR, requireActivity());
        Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    private void connectWifi() {

        ViewDialog dialogNotPermissionContact = new ViewDialog();
        dialogNotPermissionContact.showDialogConnectWifi2(
                getActivity(),
                listsResultField.get(0).getValue(),
                listsResultField.get(2).getValue()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}