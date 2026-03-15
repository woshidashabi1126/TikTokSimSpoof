<div align="center">

<img src="https://img.shields.io/badge/Platform-Android%208.1%2B-brightgreen?style=flat-square&logo=android" />
<img src="https://img.shields.io/badge/LSPosed-Required-blue?style=flat-square" />
<img src="https://img.shields.io/badge/Language-Kotlin-orange?style=flat-square&logo=kotlin" />
<img src="https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square" />
<img src="https://img.shields.io/badge/Root-Required-red?style=flat-square" />

# 📡 TikTok SIM Spoof

**LSPosed module that hooks Android's Telephony API to spoof SIM card and network parameters inside TikTok, bypassing its region detection.**

[Features](#-features) · [Requirements](#-requirements) · [Installation](#-installation) · [Usage](#-usage) · [How it works](#-how-it-works) · [FAQ](#-faq)

</div>

---

## ✨ Features

- 🌍 **40+ country presets** — US, UK, France, Germany, Japan, Brazil and more, each with real carrier MCC/MNC values
- 🔍 **Searchable country picker** — filter by country name, carrier or ISO code
- ✏️ **Full manual control** — override every field individually
- 📶 **Complete SIM spoofing** — country ISO, operator name, MCC/MNC, SIM state, phone type, network type
- 🔒 **Optional subscriber spoofing** — Line 1 Number and IMSI for deeper fingerprint masking
- 🌐 **Optional locale spoofing** — hooks `Locale.getDefault()` alongside SIM data
- 📦 **Scoped to TikTok only** — hooks are injected exclusively into TikTok's process, nothing else is affected
- ⚡ **Zero performance impact** — pure method hooks, no background services

---

## 📋 Requirements

| Requirement | Details |
|---|---|
| Android | 8.1+ (API 27+) |
| Root | Magisk or KernelSU |
| Framework | LSPosed (Zygisk or MagiskHide branch) |
| Target apps | TikTok, TikTok Lite, Douyin, TikTok for Business |

---

## 📦 Installation

**1. Clone and build**
```bash
git clone https://github.com/itachicoders/TikTokSimSpoof.git
cd TikTokSimSpoof
./gradlew assembleRelease
```
APK will be at `app/build/outputs/apk/release/app-release.apk`

**2. Install the APK** on your device.

**3. Activate in LSPosed Manager**
- Open LSPosed → Modules
- Find **SIM Spoof** and enable it
- In the scope, check **TikTok** (and any other TikTok variants you have installed)
- Reboot or soft-reboot via LSPosed

**4. Configure** — open the SIM Spoof app, pick a country, tap **Save & Apply**

**5. Force-stop TikTok** and relaunch — changes take effect immediately.

---

## 🚀 Usage

### Quick start — preset
1. Open **SIM Spoof**
2. Tap **Choose country preset...**
3. Search or scroll to your target country (e.g. `United States`)
4. Select a carrier — all fields are filled automatically
5. Optionally enable **Also Spoof Locale** for the strongest effect
6. Tap **Save & Apply**
7. Force-stop TikTok → reopen

### Manual configuration

All fields can be edited freely after selecting a preset, or from scratch:

| Field | Description | Example |
|---|---|---|
| SIM Country ISO | Two-letter country code of the SIM | `us` |
| SIM Operator MCC+MNC | Mobile Country Code + Network Code | `31026` |
| SIM Operator Name | Carrier name stored on SIM | `T-Mobile` |
| Network Country ISO | Country reported by the live network | `us` |
| Network Operator MCC+MNC | MCC+MNC of the connected network | `31026` |
| Network Operator Name | Carrier name from the network | `T-Mobile` |

### Optional — Subscriber fields

Leave blank to keep your real values. Fill in only if you need deeper spoofing:

| Field | When to use |
|---|---|
| **Line 1 Number** | Registering a new account with a specific country phone number prefix |
| **Subscriber ID (IMSI)** | Bypassing bans or device fingerprinting — starts with MCC+MNC digits |

### What to fill depending on your goal

| Goal | Required fields |
|---|---|
| Change feed / content region | SIM + Network only |
| Register account in a specific country | SIM + Network + Line 1 Number |
| Bypass ban / fingerprint detection | SIM + Network + IMSI |
| Maximum spoofing | Everything + Spoof Locale |

---

## ⚙️ How it works

The module uses LSPosed's Xposed API to hook `TelephonyManager` and `SubscriptionManager` methods **inside TikTok's process**. When TikTok calls any of these methods to determine the device's location, it receives the fake values you configured instead of the real ones.

### Hooked methods

| Method | Purpose |
|---|---|
| `getSimCountryIso()` | SIM card country |
| `getNetworkCountryIso()` | Registered network country |
| `getSimOperator()` | MCC+MNC of SIM |
| `getNetworkOperator()` | MCC+MNC of network |
| `getSimOperatorName()` | Carrier name on SIM |
| `getNetworkOperatorName()` | Carrier name from network |
| `getSimState()` | Returns `SIM_STATE_READY` |
| `getPhoneType()` | GSM / CDMA |
| `getNetworkType()` / `getDataNetworkType()` | LTE |
| `isNetworkRoaming()` | Always `false` |
| `getLine1Number()` | Phone number *(optional)* |
| `getSubscriberId()` | IMSI *(optional)* |
| `SubscriptionManager.getCountryIso()` | API 29+ country |
| `Locale.getDefault()` | System locale *(optional)* |

### Supported TikTok packages

| Package | App |
|---|---|
| `com.zhiliaoapp.musically` | TikTok Global |
| `com.ss.android.ugc.trill` | TikTok (Asia) |
| `com.zhiliaoapp.musically.go` | TikTok Lite |
| `com.ss.android.ugc.aweme` | Douyin |
| `com.tiktok.business` | TikTok for Business |

---

## 🌍 Built-in country presets

<details>
<summary>Click to expand full list</summary>

| Flag | Country | Carrier | MCC+MNC |
|---|---|---|---|
| 🇺🇸 | United States | T-Mobile | 31026 |
| 🇺🇸 | United States | Verizon | 31101 |
| 🇺🇸 | United States | AT&T | 310410 |
| 🇨🇦 | Canada | Rogers | 30272 |
| 🇨🇦 | Canada | Bell | 30230 |
| 🇲🇽 | Mexico | Telcel | 33420 |
| 🇬🇧 | United Kingdom | EE | 23430 |
| 🇬🇧 | United Kingdom | O2 | 23410 |
| 🇫🇷 | France | Orange | 20801 |
| 🇫🇷 | France | SFR | 20810 |
| 🇩🇪 | Germany | Telekom | 26201 |
| 🇩🇪 | Germany | Vodafone | 26202 |
| 🇮🇹 | Italy | TIM | 22201 |
| 🇪🇸 | Spain | Movistar | 21407 |
| 🇳🇱 | Netherlands | KPN | 20408 |
| 🇧🇪 | Belgium | Proximus | 20601 |
| 🇨🇭 | Switzerland | Swisscom | 22801 |
| 🇦🇹 | Austria | A1 | 23201 |
| 🇸🇪 | Sweden | Telia | 24001 |
| 🇳🇴 | Norway | Telenor | 24201 |
| 🇩🇰 | Denmark | TDC | 23801 |
| 🇫🇮 | Finland | Elisa | 24405 |
| 🇵🇱 | Poland | Play | 26006 |
| 🇵🇹 | Portugal | MEO | 26801 |
| 🇬🇷 | Greece | Cosmote | 20201 |
| 🇷🇺 | Russia | MTS | 25001 |
| 🇷🇺 | Russia | Beeline | 25099 |
| 🇺🇦 | Ukraine | Kyivstar | 25503 |
| 🇨🇿 | Czech Republic | T-Mobile | 23001 |
| 🇷🇴 | Romania | Orange | 22610 |
| 🇭🇺 | Hungary | Telekom | 21630 |
| 🇯🇵 | Japan | NTT DOCOMO | 44010 |
| 🇯🇵 | Japan | SoftBank | 44020 |
| 🇰🇷 | South Korea | SK Telecom | 45005 |
| 🇦🇺 | Australia | Telstra | 50501 |
| 🇳🇿 | New Zealand | Spark | 53005 |
| 🇸🇬 | Singapore | Singtel | 52501 |
| 🇮🇳 | India | Airtel | 40410 |
| 🇹🇭 | Thailand | AIS | 52001 |
| 🇮🇩 | Indonesia | Telkomsel | 51010 |
| 🇵🇭 | Philippines | Globe | 51502 |
| 🇲🇾 | Malaysia | Maxis | 50212 |
| 🇻🇳 | Vietnam | Viettel | 45204 |
| 🇦🇪 | UAE | Etisalat | 42402 |
| 🇸🇦 | Saudi Arabia | STC | 42001 |
| 🇹🇷 | Turkey | Turkcell | 28601 |
| 🇿🇦 | South Africa | Vodacom | 65501 |
| 🇪🇬 | Egypt | Orange | 60201 |
| 🇳🇬 | Nigeria | MTN | 62130 |
| 🇧🇷 | Brazil | Vivo | 72406 |
| 🇦🇷 | Argentina | Claro | 72230 |
| 🇨🇴 | Colombia | Claro | 73001 |

Full MCC/MNC reference: [mcc-mnc.com](https://mcc-mnc.com/)

</details>

---

## ❓ FAQ

**Q: The module shows as active in LSPosed but TikTok still shows the wrong region.**
A: Make sure you force-stopped TikTok completely (not just swiped away). Go to Settings → Apps → TikTok → Force Stop, then reopen.

**Q: Does this work with TikTok Lite / Douyin?**
A: Yes. All five TikTok package variants are in the scope. Just enable each one you have installed in LSPosed's scope settings.

**Q: Will this get my account banned?**
A: Spoofing SIM data is undetectable at the OS level since the hooks run inside TikTok's own process. However, TikTok also checks your IP address — use a VPN matching your target country alongside this module for the best results.

**Q: I enabled Spoof Locale but TikTok still shows my real language.**
A: TikTok caches locale aggressively. Clear TikTok's app data (Settings → Apps → TikTok → Clear Data) and relaunch.

**Q: Can I use this on a non-rooted device?**
A: No. LSPosed requires Magisk or KernelSU root with Zygisk enabled.

---

## 🛠 Troubleshooting

| Symptom | Fix |
|---|---|
| App crashes on launch | Check Android version — requires API 27+ |
| Module inactive in LSPosed | Re-enable module, reboot device |
| No effect after saving | Force-stop TikTok (not just swipe), reopen |
| XSharedPreferences reads defaults | Disable and re-enable module in LSPosed, reboot |
| Hooks not firing | Check LSPosed logs for `SimSpoof` tag |

To view hook logs: LSPosed Manager → Logs → filter by `SimSpoof`.

---

## 📁 Project structure

```
TikTokSimSpoof/
├── app/src/main/
│   ├── assets/
│   │   └── xposed_init              ← LSPosed entry point declaration
│   ├── java/dev/simspoof/tiktok/
│   │   ├── config/
│   │   │   └── SpoofConfig.kt       ← Data model + 40+ country presets
│   │   ├── hook/
│   │   │   └── MainHook.kt          ← Xposed hooks (TelephonyManager etc.)
│   │   └── ui/
│   │       └── MainActivity.kt      ← Config UI with country picker
│   └── res/
│       ├── layout/
│       │   ├── activity_main.xml
│       │   ├── dialog_country_picker.xml
│       │   └── item_country.xml
│       └── xml/
│           └── xposed_scope.xml     ← TikTok package scope declaration
```

---

## 📄 License

```
MIT License — Copyright (c) 2025 itachicoders
```

---

<div align="center">
Made by <a href="https://github.com/itachicoders">itachicoders</a>
</div>
