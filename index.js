import React from "react";
import { NativeModules, requireNativeComponent } from "react-native";

const { RNAirpushX } = NativeModules;

export default class Airpush {
	static init(apiKey, appId) {
		RNAirpushX.init(apiKey, appId);
	}

	static enableTestMode() {
		RNAirpushX.enableTestMode();
	}

	static requestInterstitial() {
		RNAirpushX.requestInterstitial();
	}

	static showInterstitial() {
		return RNAirpushX.showInterstitial();
	}
}

const RNBannerView = requireNativeComponent("RNAirpushXBanner");

export class BannerView extends React.Component {
	render() {
		const { width, height } = this.props;
		return <RNBannerView style={{ width, height }} />;
	}
}
