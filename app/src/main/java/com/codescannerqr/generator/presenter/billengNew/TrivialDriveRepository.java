package com.codescannerqr.generator.presenter.billengNew;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrivialDriveRepository {
    // Source for all constants
    final static public int GAS_TANK_MIN = 0;
    final static public int GAS_TANK_MAX = 4;
    final static public int GAS_TANK_INFINITE = 5;
    //The following SKU strings must match the ones we have in the Google Play developer console.
    // SKUs for non-subscription purchases
    static final public String SKU_PREMIUM = "premium";
    static final public String SKU_GAS = "gas";
    // SKU for subscription purchases (infinite gas)
    static final public String SKU_INFINITE_GAS_MONTHLY = Config.productID1;
    static final public String SKU_INFINITE_GAS_YEARLY = Config.productID2;
    static final String TAG = "TrivialDrive:" + TrivialDriveRepository.class.getSimpleName();
    public static final String[] INAPP_SKUS = new String[]{};
    public static final String[] SUBSCRIPTION_SKUS = new String[]{SKU_INFINITE_GAS_MONTHLY,
            SKU_INFINITE_GAS_YEARLY};
    public static final String[] AUTO_CONSUME_SKUS = new String[]{SKU_GAS};

    final BillingDataSource billingDataSource;
    //final GameStateModel gameStateModel;
    final SingleMediatorLiveEvent<Integer> gameMessages;
    final SingleMediatorLiveEvent<Integer> allMessages = new SingleMediatorLiveEvent<>();
    final ExecutorService driveExecutor = Executors.newSingleThreadExecutor();

    public TrivialDriveRepository(BillingDataSource billingDataSource) {
        this.billingDataSource = billingDataSource;

        gameMessages = new SingleMediatorLiveEvent<>();
        setupMessagesSingleMediatorLiveEvent();

        // Since both are tied to application lifecycle
        billingDataSource.observeConsumedPurchases().observeForever(skuList -> {
            for ( String sku: skuList ) {
                if (sku.equals(SKU_GAS)) {
                    //gameStateModel.incrementGas(GAS_TANK_MAX);
                }
            }
        });
    }

    /**
     * Sets up the event that we can use to send messages up to the UI to be used in Snackbars. This
     * SingleMediatorLiveEvent observes changes in SingleLiveEvents coming from the rest of the game
     * and combines them into a single source with new purchase events from the BillingDataSource.
     * Since the billing data source doesn't know about our SKUs, it also transforms the known SKU
     * strings into useful String messages.
     */
    void setupMessagesSingleMediatorLiveEvent() {
        final LiveData<List<String>> billingMessages = billingDataSource.observeNewPurchases();
        allMessages.addSource(gameMessages, allMessages::setValue);
        allMessages.addSource(billingMessages,
                stringList -> {
                    for (String s: stringList) {
                        switch (s) {
                            case SKU_GAS:
                                allMessages.setValue(R.string.message_more_gas_acquired);
                                break;
                            case SKU_PREMIUM:
                                allMessages.setValue(R.string.message_premium);
                                break;
                            case SKU_INFINITE_GAS_MONTHLY:
                            case SKU_INFINITE_GAS_YEARLY:
                                // this makes sure that upgraded and downgraded subscriptions are
                                // reflected correctly in the app UI
                                billingDataSource.refreshPurchasesAsync();
                                allMessages.setValue(R.string.message_subscribed);
                                break;
                        }
                    }
                });
    }

    /**
     * Automatic support for upgrading/downgrading subscription.
     *
     * @param activity Needed by billing library to start the Google Play billing activity
     * @param sku the product ID to purchase
     */
    public void buySku(Activity activity, String sku) {
        String oldSku = null;
        switch (sku) {
            case SKU_INFINITE_GAS_MONTHLY:
                oldSku = SKU_INFINITE_GAS_YEARLY;
                break;
            case SKU_INFINITE_GAS_YEARLY:
                oldSku = SKU_INFINITE_GAS_MONTHLY;
                break;
        }
        if ( null != oldSku ) {
            billingDataSource.launchBillingFlow(activity, sku, "");
        } else {
            billingDataSource.launchBillingFlow(activity, sku);
        }
    }

    /**
     * Return LiveData that indicates whether the sku is currently purchased.
     *
     * @param sku the SKU to get and observe the value for
     * @return LiveData that returns true if the sku is purchased.
     */
    public LiveData<Boolean> isPurchased(String sku) {
        return billingDataSource.isPurchased(sku);
    }

    public final LiveData<String> getSkuPrice(String sku) {
        Log.d("TAGING", "GET SKU: "+billingDataSource.getSkuPrice(sku).getValue());
        return billingDataSource.getSkuPrice(sku);
    }
}
