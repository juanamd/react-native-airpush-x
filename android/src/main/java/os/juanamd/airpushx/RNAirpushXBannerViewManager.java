package os.juanamd.airpushx;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.ap.ApBanner;
import com.ap.ApEventsListener;
import com.ap.ApPreparedAd;

public class RNAirpushXBannerViewManager extends SimpleViewManager<RelativeLayout> {

	public static final String TAG = "RNAirpushXBannerView";

	@Override
	public String getName() {
		return "RNAirpushXBanner";
	}

	@Override
	protected RelativeLayout createViewInstance(ThemedReactContext themedReactContext) {
		final Context baseContext = themedReactContext.getBaseContext();
        if (baseContext == null || !(baseContext instanceof Activity)) {
			Log.e(TAG, "Base context is not an activity!");
			return null;
        }
		
        final RelativeLayout container = new RelativeLayout(baseContext) {
			@Override protected void onAttachedToWindow() {
				super.onAttachedToWindow();
				Log.d(TAG, "onAttachedToWindow");
				if (getChildCount() == 0) {
					final ApBanner apBanner = new ApBanner(getContext());
					final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					addListeners(apBanner);
					addView(apBanner, lp);
					apBanner.load();
				}
			}

			@Override
			protected void onDetachedFromWindow() {
				super.onDetachedFromWindow();
				Log.d(TAG, "onDetachedFromWindow");
				final ApBanner apBanner = (ApBanner) getChildAt(0);
				if (apBanner != null) apBanner.destroy();
				if (getChildCount() > 0) removeAllViews();
			}

			@Override
			public void requestLayout() {
				super.requestLayout();
				Log.d(TAG, "requestLayout");
				if (getWidth() > 0 && getHeight() > 0) {
					measure(getWidth(), getHeight());
					layout(getLeft(), getTop(), getRight(), getBottom());
				}
			}
		};

		return container;
	}

	private void addListeners(final ApBanner apBanner) {
		Log.d(TAG, "addListeners");
		apBanner.setEventsListener(new ApEventsListener() {
			@Override
			public void onLoaded(ApPreparedAd ad) {
				Log.i(TAG, "banner loaded");
				ad.show();
			}
			@Override
			public void onFailed(String reason) {
				Log.e(TAG, "banner failed: " + reason);
			}
			@Override
			public void onClicked() {
				Log.i(TAG, "banner clicked");
			}
			@Override
			public void onOpened() {
				Log.i(TAG, "banner opened");
			}
			@Override
			public void onClosed() {
				Log.i(TAG, "banner closed");
			}
			@Override
			public void onLeaveApplication() {
				Log.i(TAG, "banner left application");
			}
		});
	}

}
