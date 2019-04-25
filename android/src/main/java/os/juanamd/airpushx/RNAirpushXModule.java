package os.juanamd.airpushx;

import android.util.Log;
import android.app.Activity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.ap.ApSdk;
import com.ap.ApInterstitial;
import com.ap.ApEventsListener;
import com.ap.ApPreparedAd;

public class RNAirpushXModule extends ReactContextBaseJavaModule {

	private static final String TAG = "RNAirpushXModule";
	private static ApPreparedAd preparedInterstitial;
	private static Promise interstitialPromise;

	public RNAirpushXModule(ReactApplicationContext reactContext) {
		super(reactContext);
	}

	@Override
	public String getName() {
		return "RNAirpushX";
	}

	@ReactMethod
	public void init(String apiKey, String appId) {
		Log.d(TAG, "init");
		ReactApplicationContext context = this.getReactApplicationContext();
		ApSdk.init(context, apiKey, appId);
	}

	@ReactMethod
	public void enableTestMode() {
		Log.d(TAG, "enableTestMode");
		ApSdk.enableTestMode();
	}

	@ReactMethod
	public void requestInterstitial() {
		Log.d(TAG, "requestInterstitial");
		Activity activity = this.getReactApplicationContext().getCurrentActivity();
		ApInterstitial airPushInterstitial = new ApInterstitial(activity);
		airPushInterstitial.setEventsListener(new ApEventsListener() {
			@Override
			public void onLoaded(ApPreparedAd iAirPushPreparedAd) {
				preparedInterstitial = iAirPushPreparedAd;
				Log.d(TAG, "Interstitial loaded");
			}

			@Override
			public void onFailed(String reason) {
				Log.e(TAG, "Interstitial failed: " + reason);
				rejectInterstitialPromise(reason);
			}

			@Override
			public void onClicked() {
				resolveInterstitialPromise("clicked");
				Log.d(TAG, "Interstitial clicked");
			}

			@Override
			public void onOpened() {
				Log.d(TAG, "Interstitial opened");
			}

			@Override
			public void onClosed() {
				resolveInterstitialPromise("closed");
				Log.d(TAG, "Interstitial closed");
			}

			@Override
			public void onLeaveApplication() {
				Log.d(TAG, "Interstitial left application");
			}
		});
		airPushInterstitial.load();
	}

	private void resolveInterstitialPromise(String statusMessage) {
		if (interstitialPromise != null) {
			try {
				interstitialPromise.resolve(statusMessage);
			} catch (Exception e) {
				rejectInterstitialPromise(e.toString());
			}
		}
	}

	private void rejectInterstitialPromise(String message) {
		if (interstitialPromise != null) {
			try {
				if (message != null) interstitialPromise.reject(message);
				else interstitialPromise.reject("null message");
			} catch (Exception e) {
				Log.e(TAG, "rejectInterstitialPromise failed: " + e.toString());
			}
		}
	}

	@ReactMethod
	public void showInterstitial(Promise promise) {
		Log.d(TAG, "showInterstitial");
		if (preparedInterstitial == null) {
			promise.reject("undefined interstitial");
			Log.e(TAG, "Attempted to show an undefined interstitial!");
		}
		else {
			interstitialPromise = promise;
			preparedInterstitial.show();
		}
	}

}