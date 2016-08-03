package com.tapjoy;

import java.util.Hashtable;

import android.content.Context;

public class TapjoyConnect {
	
	private static TapjoyConnect tapjoyConnectInstance;
	
	private TapjoyConnect(Context context, String appID, String secretKey, Hashtable arg4, TapjoyConnectNotifier notifier) {
    }
	 
	public void actionComplete(String actionID) {
        
    }

    public void awardTapPoints(int amount, TapjoyAwardPointsNotifier notifier) {
        
    }

    public void cacheVideos() {
    }

    public void enableBannerAdAutoRefresh(boolean shouldAutoRefresh) {
    }

    public void enableDisplayAdAutoRefresh(boolean shouldAutoRefresh) {
    }

    public static void enableLogging(boolean enable) {
    }

    public void enablePaidAppWithActionID(String paidAppPayPerActionID) {
    }

    public void enableVideoCache(boolean enable) {
    }

    public String getAppID() {
        return "1234567890";
    }

    public float getCurrencyMultiplier() {
        return 10.0f;
    }

    public void getDailyRewardAd(TapjoyDailyRewardAdNotifier notifier) {
    }

    public void getDailyRewardAdWithCurrencyID(String currencyID, TapjoyDailyRewardAdNotifier notifier) {
    }

    public void getDisplayAd(TapjoyDisplayAdNotifier notifier) {
    }

    public void getDisplayAdWithCurrencyID(String currencyID, TapjoyDisplayAdNotifier notifier) {
    }

    public void getFeaturedApp(TapjoyFeaturedAppNotifier notifier) {
    }

    public void getFeaturedAppWithCurrencyID(String currencyID, TapjoyFeaturedAppNotifier notifier) {
    }

    public void getFullScreenAd(TapjoyFullScreenAdNotifier notifier) {
    }

    public void getFullScreenAdWithCurrencyID(String currencyID, TapjoyFullScreenAdNotifier notifier) {
    }

    public void getTapPoints(TapjoyNotifier notifier) {
    }

    public static TapjoyConnect getTapjoyConnectInstance() {
        return tapjoyConnectInstance;
    }

    public String getUserID() {
        return "1235678";
    }

    public void initVideoAd(TapjoyVideoNotifier notifier) {
    }

    public static void requestTapjoyConnect(Context context, String appID, String secretKey) {
    	TapjoyConnect.requestTapjoyConnect(context, appID, secretKey, null);
    }

    public static void requestTapjoyConnect(Context context, String appID, String secretKey, Hashtable arg4) {
    	TapjoyConnect.requestTapjoyConnect(context, appID, secretKey, null, null);
    }

    public static void requestTapjoyConnect(Context context, String appID, String secretKey, Hashtable arg9, TapjoyConnectNotifier notifier) {
    	TapjoyConnect.tapjoyConnectInstance = new TapjoyConnect(context, appID, secretKey, arg9, notifier);
    	if(notifier != null){
    		notifier.connectSuccess();
    	}
    }

    public void sendIAPEvent(String name, float price, int quantity, String currencyCode) {
    }

    public void sendShutDownEvent() {
    }

    public void setBannerAdSize(String dimensions) {
    }

    public void setCurrencyMultiplier(float multiplier) {
    }

    public void setDisplayAdSize(String dimensions) {
    }

    public void setEarnedPointsNotifier(TapjoyEarnedPointsNotifier notifier) {
    }

    public void setFeaturedAppDisplayCount(int count) {
    }

    public static void setFlagKeyValue(String key, String value) {
    }

    public void setUserID(String userID) {
    }

    public void setVideoCacheCount(int count) {
    }

    public void setVideoNotifier(TapjoyVideoNotifier notifier) {
    }

    public void showDailyRewardAd() {
    }

    public void showFeaturedAppFullScreenAd() {
    }

    public void showFullScreenAd() {
    }

    public void showOffers() {
    }

    public void showOffersWithCurrencyID(String currencyID, boolean enableCurrencySelector) {
    }

    public void spendTapPoints(int amount, TapjoySpendPointsNotifier notifier) {
    }
}
