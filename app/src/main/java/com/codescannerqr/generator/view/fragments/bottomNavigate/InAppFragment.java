package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.codescannerqr.generator.App;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.FragmentInAppBinding;
import com.codescannerqr.generator.helpers.IntentHelpers;
import com.codescannerqr.generator.helpers.SharedPreferenceManager;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.presenter.billengNew.TrivialDriveRepository;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.view.activity.SplashScreenActivity;

import org.jetbrains.annotations.NotNull;

public class InAppFragment extends Fragment implements IGeneratedView {

    private FragmentInAppBinding binding;
    private GeneratePresenter generatePresenter;
    private SharedPreferenceManager sharedPreferenceManager;
    private TrivialDriveRepository trivialDriveRepository;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInAppBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext());
        setBorderFreeTrial();

        trivialDriveRepository = ((App) requireActivity().getApplication()).appContainer.
                trivialDriveRepository;

        binding.imageViewFreeTrialClose.setVisibility(View.INVISIBLE);
        startTimers();
        startAnimationButton();

        binding.cardViewOneMonthFreeTrial.setOnClickListener(v -> {
            setBorderOneMonth();
        });
        binding.radioOneMonthFreeTrial.setOnClickListener(v -> setBorderOneMonth());
        binding.cardViewSevenDaysFreeTrial.setOnClickListener(v -> setBorderFreeTrial());
        binding.radioFreeTrial.setOnClickListener(v -> setBorderFreeTrial());
        binding.textViewInAppTerms.setOnClickListener(v -> IntentHelpers.shareIntent(
                requireActivity(),
                Config.termsUrl));
        binding.textViewInAppPrivacy.setOnClickListener(v -> IntentHelpers.shareIntent(
                requireActivity(),
                Config.privacyPolicyUrl));
        binding.buttonFreeTrial.setOnClickListener(v -> onInApp());

        getPrices();
        setObserveSubs();
        return root;
    }

    private void setObserveSubs() {
        //isPurchased
        trivialDriveRepository.isPurchased(Config.productID1).observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    closeInAppFragment();
                }
            }
        });

        trivialDriveRepository.isPurchased(Config.productID2).observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    closeInAppFragment();
                }
            }
        });
    }

    private void closeInAppFragment() {
        Config.premium = true;
        sharedPreferenceManager.putBoolean("premium", true);
        requireActivity().startActivity(new Intent(requireActivity(), SplashScreenActivity.class));
        requireActivity().finish();
    }

    private void startAnimationButton() {
        Animation mEnlargeAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.enlarge);
        binding.buttonFreeTrial.startAnimation(mEnlargeAnimation);
    }

    private void onInApp() {
        //name method: get_price OR inApp
        if (binding.radioOneMonthFreeTrial.isChecked()) {
            trivialDriveRepository.buySku(requireActivity(), Config.productID1);
        } else {
            trivialDriveRepository.buySku(requireActivity(), Config.productID2);
        }
    }

    private void getPrices() {
        trivialDriveRepository.getSkuPrice(Config.productID1).observe(requireActivity(), s -> {
            SpannableStringBuilder snackbarTextOne = new SpannableStringBuilder();
            snackbarTextOne.append(requireActivity().getString(R.string.string_one_month));
            snackbarTextOne.setSpan(new AbsoluteSizeSpan(14, true),
                    0, snackbarTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarTextOne.append(" ");
            snackbarTextOne.append(Html.fromHtml("<font color=\"#FFA800\">" + s + "</font>", 0));

            binding.radioOneMonthFreeTrial.setText(
                    snackbarTextOne
            );
        });
        trivialDriveRepository.getSkuPrice(Config.productID2).observe(requireActivity(), s -> {
            int endSymbol;
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
            snackbarText.append(requireActivity().getString(R.string.string_free_7_days));
            snackbarText.append("\n");
            snackbarText.setSpan(new AbsoluteSizeSpan(14, true),
                    0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            endSymbol = snackbarText.length();

            snackbarText.append(Html.fromHtml("<font color=\"#FFA800\">" + s + "</font>", 0));
            snackbarText.append(requireActivity().getString(R.string.year_desc)).append(" ");
            snackbarText.append(requireActivity().getString(R.string.after_desc));
            snackbarText.setSpan(new AbsoluteSizeSpan(10, true),
                    endSymbol, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.radioFreeTrial.setText(
                    snackbarText
            );
        });
    }

    private void showInterDone() {
        InterstitialManager.showInter(requireActivity(), new InterDismiss() {
            @Override
            public void interDismiss(int count) {
                if (count == 1) {
                    ViewDialog.showDialogRemoveAds(requireActivity());
                } else {
                    nextActionInter();
                }
            }
        });

    }

    private void nextActionInter() {
        startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

    private void setBorderFreeTrial() {
        binding.cardViewOneMonthFreeTrial.setStrokeColor(requireActivity().getColor(R.color.light_gray));
        binding.radioOneMonthFreeTrial.setChecked(false);
        binding.cardViewSevenDaysFreeTrial.setStrokeColor(requireActivity().getColor(R.color.orange));
        binding.radioFreeTrial.setChecked(true);
        binding.cardViewOneMonthFreeTrial.invalidate();
        binding.cardViewSevenDaysFreeTrial.invalidate();
    }

    private void setBorderOneMonth() {
        binding.cardViewOneMonthFreeTrial.setStrokeColor(requireActivity().getColor(R.color.orange));
        binding.radioOneMonthFreeTrial.setChecked(true);
        binding.cardViewSevenDaysFreeTrial.setStrokeColor(requireActivity().getColor(R.color.light_gray));
        binding.radioFreeTrial.setChecked(false);
        binding.cardViewOneMonthFreeTrial.invalidate();
        binding.cardViewSevenDaysFreeTrial.invalidate();
    }

    private void startTimers() {
        new CountDownTimer(3000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                binding.imageViewFreeTrialClose.setVisibility(View.VISIBLE);
                binding.imageViewFreeTrialClose.setOnClickListener(v -> {
                    try {
                        if (InterstitialManager.getLoaded() && !Config.premium) {
                            showInterDone();
                        } else {
                            nextActionInter();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        nextActionInter();
                    }

                });
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}