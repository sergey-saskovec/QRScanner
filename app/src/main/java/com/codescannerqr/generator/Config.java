package com.codescannerqr.generator;

import android.graphics.Color;

import com.codescannerqr.generator.model.ColorGradinet;
import com.codescannerqr.generator.model.InAppNewModel;
import com.codescannerqr.generator.model.SectionList;
import com.codescannerqr.generator.model.SectionSettingsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {
    //////////////////////EDITABLE///////////////////////
    public final static String privacyPolicyUrl = "https://generator.codescannerqr.com/privacy.html";
    public final static String termsUrl = "https://generator.codescannerqr.com/terms.html";
    public final static String emailFeedback = "viktoria.sklyar@gmail.com";

    //ADS ID's
    public static boolean premium = false;
    public static String bannerID = "ca-app-pub-3940256099942544/6300978111";
    public static String appOpenID = "ca-app-pub-3940256099942544/3419835294";
    public static String interID = "ca-app-pub-3940256099942544/1033173712";
    public static String nativeID = "ca-app-pub-3940256099942544/2247696110";

    public final static String productID1 = "monthly_noads";
    public final static String productID2 = "year_noads";

    /*public static String appHudKey = "app_heMVWrskcukZnZAwchMHtHmAHAVQ3r";
    public static String tenjinAPIKey = "XFZX7VQXDLALZ41CV5Y1ZJMQEYJWSNYQ";*/
    //////////////////////EDITABLE///////////////////////

    public static int weightQR = 400;
    public static boolean settingsVibration = false;
    public static boolean settingsSound = false;
    public static boolean settingsAutoCopy = false;
    public static boolean settingsAutoSearch = false;
    public static boolean settingsSaveHistory = true;

    public final static String BASE_URL_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";
    public static String packageMaps = "com.google.android.apps.maps";
    public static final List<SectionList> listSectionOneItems = Collections.unmodifiableList(
            new ArrayList<SectionList>() {{
                add(new SectionList(R.string.clipboard, R.drawable.ic_gen_clipboard, R.color.color1));
                add(new SectionList(R.string.website, R.drawable.ic_gen_website, R.color.color2));
                add(new SectionList(R.string.wifi, R.drawable.ic_gen_wifi, R.color.color3));
                add(new SectionList(R.string.text, R.drawable.ic_gen_text, R.color.color4));
                add(new SectionList(R.string.contact, R.drawable.ic_gen_contact, R.color.color5));
                add(new SectionList(R.string.cellphone, R.drawable.ic_gen_cellphone, R.color.color6));
                add(new SectionList(R.string.email, R.drawable.ic_gen_email, R.color.color7));
                add(new SectionList(R.string.sms, R.drawable.ic_gen_sms, R.color.color8));
                add(new SectionList(R.string.my_card, R.drawable.ic_gen_mycard, R.color.color9));
                add(new SectionList(R.string.geo, R.drawable.ic_gen_geo, R.color.color10));
                add(new SectionList(R.string.calendar, R.drawable.ic_gen_calendar, R.color.color11));
                add(new SectionList(R.string.product_code, R.drawable.ic_gen_product_code, R.color.color12));
            }});
    public static final List<SectionList> listSectionOneItems2 = Collections.unmodifiableList(
            new ArrayList<SectionList>() {{
                add(new SectionList(R.string.facebook, R.drawable.ic_gen_facebook, R.color.light_gray));
                add(new SectionList(R.string.youtube, R.drawable.ic_gen_youtube, R.color.light_gray));
                add(new SectionList(R.string.whatsapp, R.drawable.ic_gen_whatsapp, R.color.light_gray));
                add(new SectionList(R.string.paypal, R.drawable.ic_gen_paypal, R.color.light_gray));
                add(new SectionList(R.string.twitter, R.drawable.ic_gen_twitter, R.color.light_gray));
                add(new SectionList(R.string.instagram, R.drawable.ic_gen_instagram, R.color.light_gray));
                add(new SectionList(R.string.spotify, R.drawable.ic_gen_spotify, R.color.light_gray));
                add(new SectionList(R.string.tiktok, R.drawable.ic_gen_tiktok, R.color.light_gray));
                add(new SectionList(R.string.viber, R.drawable.ic_gen_viber, R.color.light_gray));
            }});

    public static final List<Integer> listLogo = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(R.drawable.ic_close_logo);
                add(R.drawable.ic_gen_facebook);
                add(R.drawable.ic_gen_youtube);
                add(R.drawable.ic_gen_whatsapp);
                add(R.drawable.ic_gen_paypal);
                add(R.drawable.ic_gen_twitter);
                add(R.drawable.ic_gen_instagram);
                add(R.drawable.ic_gen_spotify);
                add(R.drawable.ic_gen_tiktok);
                add(R.drawable.ic_gen_viber);
            }});

    public static final List<Integer> listBorder = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(R.drawable.ic_close_logo);
                add(R.drawable.ic_border_qr_1);
                add(R.drawable.ic_border_qr_2);
                add(R.drawable.ic_border_qr_3);
                add(R.drawable.ic_border_qr_4);
                add(R.drawable.ic_border_qr_5);
            }});

    public static final List<ColorGradinet> listColorGradientStyle = Collections.unmodifiableList(
            new ArrayList<ColorGradinet>() {{
                add(new ColorGradinet(0, 0));
                add(new ColorGradinet(Color.parseColor("#FFA800"), Color.parseColor("#FFA800")));
                add(new ColorGradinet(Color.parseColor("#0500FF"), Color.parseColor("#FF0000")));
                add(new ColorGradinet(Color.parseColor("#05FF00"), Color.parseColor("#0085FF")));
                add(new ColorGradinet(Color.parseColor("#FFA800"), Color.parseColor("#FF0000")));
                add(new ColorGradinet(Color.parseColor("#05FF00"), Color.parseColor("#FF0000")));
            }});

    public static final List<Integer> listNameHistoryFragment = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(R.string.scan);
                add(R.string.create);
                add(R.string.bookmark);
            }});


    public static List<InAppNewModel> listInAppNewFragment = new ArrayList<>();

    public static final List<Integer> listInAppNewFragment2 = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(R.string.unlimited_scans_available);
                add(R.string.customize_your_QR_codes);
                add(R.string.save_history_to_csv_file);
                add(R.string.without_ADS);
                add(R.string.all_latest_features);
                add(R.string.priority_support);
            }});


    public static final List<SectionSettingsList> listSectionOneItemsSettings = Collections.unmodifiableList(
            new ArrayList<SectionSettingsList>() {{
                add(new SectionSettingsList(
                        R.string.language,
                        R.drawable.ic_settings_location,
                        false,
                        true,
                        false));
            }});

    public static final List<SectionSettingsList> listSectionTwoItemsSettings = Collections.unmodifiableList(
            new ArrayList<SectionSettingsList>() {{
                add(new SectionSettingsList(
                        R.string.vibration,
                        R.drawable.ic_settings_vibration,
                        Config.settingsVibration,
                        false,
                        true));
                add(new SectionSettingsList(
                        R.string.sound,
                        R.drawable.ic_settings_sound,
                        Config.settingsSound,
                        false,
                        true));
                add(new SectionSettingsList(
                        R.string.auto_copy,
                        R.drawable.ic_settings_copy,
                        Config.settingsAutoCopy,
                        false,
                        true));
                add(new SectionSettingsList(
                        R.string.auto_search,
                        R.drawable.ic_settings_search,
                        Config.settingsAutoSearch,
                        false,
                        true));
                add(new SectionSettingsList(
                        R.string.save_history,
                        R.drawable.ic_settings_save_history,
                        Config.settingsSaveHistory,
                        false,
                        true));
            }});

    public static final List<SectionSettingsList> listSectionThreeItemsSettings = Collections.unmodifiableList(
            new ArrayList<SectionSettingsList>() {{
                add(new SectionSettingsList(
                        R.string.restore_purchase,
                        R.drawable.ic_settings_restore_purchase,
                        false,
                        false,
                        false));
                add(new SectionSettingsList(
                        R.string.privacy_policy,
                        R.drawable.ic_settings_privacy_policy,
                        false,
                        false,
                        false));
                add(new SectionSettingsList(
                        R.string.share_friends,
                        R.drawable.ic_settings_share,
                        false,
                        false,
                        false));
                add(new SectionSettingsList(
                        R.string.rate_us,
                        R.drawable.ic_settings_rate_us,
                        false,
                        false,
                        false));
            }});



}
