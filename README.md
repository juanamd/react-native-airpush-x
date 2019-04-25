# react-native-airpush-x

Airpush SDK-X support for **Android**
*Based on [react-native-airpush](https://www.npmjs.com/package/react-native-airpush)*

*NOTE: only interstitial and banner supported for now*

## Getting started

`$ yarn add react-native-airpush-x` 

`$ react-native link react-native-airpush-x`

## Usage

```javascript
import  Airpush, { BannerView } from  "react-native-airpush-x";

Airpush.setApiKey(apiKey, appId);
Airpush.enableTestMode();
Airpush.requestInterstitial();
Airpush.showInterstitial()
	.then(message => console.log(message))
	.catch(reason => console.log(reason));

<BannerView width={320} height={50} />;
```
