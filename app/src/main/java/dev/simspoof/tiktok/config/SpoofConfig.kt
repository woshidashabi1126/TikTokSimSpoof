package dev.simspoof.tiktok.config

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * Immutable snapshot of spoof settings.
 * Stored in a world-readable SharedPreferences file so the hook
 * process (TikTok) can read it via XSharedPreferences.
 */
data class SpoofConfig(
    val enabled: Boolean = true,

    // --- SIM card identity ---
    val simCountryIso: String = "us",        // e.g. "us", "gb", "de"
    val simOperatorMccMnc: String = "31026",  // MCC+MNC concatenated, US T-Mobile
    val simOperatorName: String = "T-Mobile",
    val simState: Int = 5,                   // TelephonyManager.SIM_STATE_READY

    // --- Network ---
    val networkCountryIso: String = "us",
    val networkOperatorMccMnc: String = "31026",
    val networkOperatorName: String = "T-Mobile",
    val networkType: Int = 13,               // NETWORK_TYPE_LTE
    val phoneType: Int = 1,                  // PHONE_TYPE_GSM

    // --- Subscriber info ---
    val line1Number: String = "",            // leave blank → return empty
    val subscriberId: String = "",           // IMSI — leave blank → return empty
    val iccId: String = "",                  // SIM serial — leave blank → return empty

    // --- Locale hint for TikTok internals ---
    val spoofLocale: Boolean = false,        // optional: also hook Locale.getDefault()
    val localeLanguage: String = "en",
    val localeCountry: String = "US"
) {
    companion object {
        const val PREFS_NAME = "simspoof_config"
        const val KEY_JSON   = "config_json"
        private val gson = Gson()

        fun load(ctx: Context): SpoofConfig {
            val prefs: SharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json = prefs.getString(KEY_JSON, null) ?: return SpoofConfig()
            return runCatching { gson.fromJson(json, SpoofConfig::class.java) }.getOrDefault(SpoofConfig())
        }

        fun save(ctx: Context, config: SpoofConfig) {
            ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_JSON, gson.toJson(config))
                .apply()
        }

        /** Called from hook process via XSharedPreferences */
        fun fromPrefs(prefs: de.robv.android.xposed.XSharedPreferences): SpoofConfig {
            val json = prefs.getString(KEY_JSON, null) ?: return SpoofConfig()
            return runCatching { gson.fromJson(json, SpoofConfig::class.java) }.getOrDefault(SpoofConfig())
        }
    }
}

/** Preset SIM profiles — one entry per country/carrier */
enum class SimProfile(
    val flag: String,
    val countryName: String,
    val carrierName: String,
    val config: SpoofConfig
) {
    // ── North America ──────────────────────────────────────────────────────────
    US_T_MOBILE("🇺🇸", "United States", "T-Mobile", SpoofConfig(
        simCountryIso = "us", simOperatorMccMnc = "31026", simOperatorName = "T-Mobile",
        networkCountryIso = "us", networkOperatorMccMnc = "31026", networkOperatorName = "T-Mobile",
        localeLanguage = "en", localeCountry = "US"
    )),
    US_VERIZON("🇺🇸", "United States", "Verizon", SpoofConfig(
        simCountryIso = "us", simOperatorMccMnc = "31101", simOperatorName = "Verizon",
        networkCountryIso = "us", networkOperatorMccMnc = "31101", networkOperatorName = "Verizon",
        phoneType = 2, localeLanguage = "en", localeCountry = "US"
    )),
    US_ATT("🇺🇸", "United States", "AT&T", SpoofConfig(
        simCountryIso = "us", simOperatorMccMnc = "310410", simOperatorName = "AT&T",
        networkCountryIso = "us", networkOperatorMccMnc = "310410", networkOperatorName = "AT&T",
        localeLanguage = "en", localeCountry = "US"
    )),
    CA_ROGERS("🇨🇦", "Canada", "Rogers", SpoofConfig(
        simCountryIso = "ca", simOperatorMccMnc = "30272", simOperatorName = "Rogers",
        networkCountryIso = "ca", networkOperatorMccMnc = "30272", networkOperatorName = "Rogers",
        localeLanguage = "en", localeCountry = "CA"
    )),
    CA_BELL("🇨🇦", "Canada", "Bell", SpoofConfig(
        simCountryIso = "ca", simOperatorMccMnc = "30230", simOperatorName = "Bell",
        networkCountryIso = "ca", networkOperatorMccMnc = "30230", networkOperatorName = "Bell",
        localeLanguage = "en", localeCountry = "CA"
    )),
    MX_TELCEL("🇲🇽", "Mexico", "Telcel", SpoofConfig(
        simCountryIso = "mx", simOperatorMccMnc = "33420", simOperatorName = "Telcel",
        networkCountryIso = "mx", networkOperatorMccMnc = "33420", networkOperatorName = "Telcel",
        localeLanguage = "es", localeCountry = "MX"
    )),

    // ── Western Europe ─────────────────────────────────────────────────────────
    GB_EE("🇬🇧", "United Kingdom", "EE", SpoofConfig(
        simCountryIso = "gb", simOperatorMccMnc = "23430", simOperatorName = "EE",
        networkCountryIso = "gb", networkOperatorMccMnc = "23430", networkOperatorName = "EE",
        localeLanguage = "en", localeCountry = "GB"
    )),
    GB_O2("🇬🇧", "United Kingdom", "O2", SpoofConfig(
        simCountryIso = "gb", simOperatorMccMnc = "23410", simOperatorName = "O2",
        networkCountryIso = "gb", networkOperatorMccMnc = "23410", networkOperatorName = "O2",
        localeLanguage = "en", localeCountry = "GB"
    )),
    FR_ORANGE("🇫🇷", "France", "Orange", SpoofConfig(
        simCountryIso = "fr", simOperatorMccMnc = "20801", simOperatorName = "Orange",
        networkCountryIso = "fr", networkOperatorMccMnc = "20801", networkOperatorName = "Orange",
        localeLanguage = "fr", localeCountry = "FR"
    )),
    FR_SFR("🇫🇷", "France", "SFR", SpoofConfig(
        simCountryIso = "fr", simOperatorMccMnc = "20810", simOperatorName = "SFR",
        networkCountryIso = "fr", networkOperatorMccMnc = "20810", networkOperatorName = "SFR",
        localeLanguage = "fr", localeCountry = "FR"
    )),
    DE_TELEKOM("🇩🇪", "Germany", "Telekom", SpoofConfig(
        simCountryIso = "de", simOperatorMccMnc = "26201", simOperatorName = "Telekom",
        networkCountryIso = "de", networkOperatorMccMnc = "26201", networkOperatorName = "Telekom",
        localeLanguage = "de", localeCountry = "DE"
    )),
    DE_VODAFONE("🇩🇪", "Germany", "Vodafone", SpoofConfig(
        simCountryIso = "de", simOperatorMccMnc = "26202", simOperatorName = "Vodafone",
        networkCountryIso = "de", networkOperatorMccMnc = "26202", networkOperatorName = "Vodafone",
        localeLanguage = "de", localeCountry = "DE"
    )),
    IT_TIM("🇮🇹", "Italy", "TIM", SpoofConfig(
        simCountryIso = "it", simOperatorMccMnc = "22201", simOperatorName = "TIM",
        networkCountryIso = "it", networkOperatorMccMnc = "22201", networkOperatorName = "TIM",
        localeLanguage = "it", localeCountry = "IT"
    )),
    ES_MOVISTAR("🇪🇸", "Spain", "Movistar", SpoofConfig(
        simCountryIso = "es", simOperatorMccMnc = "21407", simOperatorName = "Movistar",
        networkCountryIso = "es", networkOperatorMccMnc = "21407", networkOperatorName = "Movistar",
        localeLanguage = "es", localeCountry = "ES"
    )),
    NL_KPN("🇳🇱", "Netherlands", "KPN", SpoofConfig(
        simCountryIso = "nl", simOperatorMccMnc = "20408", simOperatorName = "KPN",
        networkCountryIso = "nl", networkOperatorMccMnc = "20408", networkOperatorName = "KPN",
        localeLanguage = "nl", localeCountry = "NL"
    )),
    BE_PROXIMUS("🇧🇪", "Belgium", "Proximus", SpoofConfig(
        simCountryIso = "be", simOperatorMccMnc = "20601", simOperatorName = "Proximus",
        networkCountryIso = "be", networkOperatorMccMnc = "20601", networkOperatorName = "Proximus",
        localeLanguage = "fr", localeCountry = "BE"
    )),
    CH_SWISSCOM("🇨🇭", "Switzerland", "Swisscom", SpoofConfig(
        simCountryIso = "ch", simOperatorMccMnc = "22801", simOperatorName = "Swisscom",
        networkCountryIso = "ch", networkOperatorMccMnc = "22801", networkOperatorName = "Swisscom",
        localeLanguage = "de", localeCountry = "CH"
    )),
    AT_A1("🇦🇹", "Austria", "A1", SpoofConfig(
        simCountryIso = "at", simOperatorMccMnc = "23201", simOperatorName = "A1",
        networkCountryIso = "at", networkOperatorMccMnc = "23201", networkOperatorName = "A1",
        localeLanguage = "de", localeCountry = "AT"
    )),
    SE_TELIA("🇸🇪", "Sweden", "Telia", SpoofConfig(
        simCountryIso = "se", simOperatorMccMnc = "24001", simOperatorName = "Telia",
        networkCountryIso = "se", networkOperatorMccMnc = "24001", networkOperatorName = "Telia",
        localeLanguage = "sv", localeCountry = "SE"
    )),
    NO_TELENOR("🇳🇴", "Norway", "Telenor", SpoofConfig(
        simCountryIso = "no", simOperatorMccMnc = "24201", simOperatorName = "Telenor",
        networkCountryIso = "no", networkOperatorMccMnc = "24201", networkOperatorName = "Telenor",
        localeLanguage = "no", localeCountry = "NO"
    )),
    DK_TDC("🇩🇰", "Denmark", "TDC", SpoofConfig(
        simCountryIso = "dk", simOperatorMccMnc = "23801", simOperatorName = "TDC",
        networkCountryIso = "dk", networkOperatorMccMnc = "23801", networkOperatorName = "TDC",
        localeLanguage = "da", localeCountry = "DK"
    )),
    FI_ELISA("🇫🇮", "Finland", "Elisa", SpoofConfig(
        simCountryIso = "fi", simOperatorMccMnc = "24405", simOperatorName = "Elisa",
        networkCountryIso = "fi", networkOperatorMccMnc = "24405", networkOperatorName = "Elisa",
        localeLanguage = "fi", localeCountry = "FI"
    )),
    PL_PLAY("🇵🇱", "Poland", "Play", SpoofConfig(
        simCountryIso = "pl", simOperatorMccMnc = "26006", simOperatorName = "Play",
        networkCountryIso = "pl", networkOperatorMccMnc = "26006", networkOperatorName = "Play",
        localeLanguage = "pl", localeCountry = "PL"
    )),
    PT_MEO("🇵🇹", "Portugal", "MEO", SpoofConfig(
        simCountryIso = "pt", simOperatorMccMnc = "26801", simOperatorName = "MEO",
        networkCountryIso = "pt", networkOperatorMccMnc = "26801", networkOperatorName = "MEO",
        localeLanguage = "pt", localeCountry = "PT"
    )),
    GR_COSMOTE("🇬🇷", "Greece", "Cosmote", SpoofConfig(
        simCountryIso = "gr", simOperatorMccMnc = "20201", simOperatorName = "Cosmote",
        networkCountryIso = "gr", networkOperatorMccMnc = "20201", networkOperatorName = "Cosmote",
        localeLanguage = "el", localeCountry = "GR"
    )),

    // ── Eastern Europe ─────────────────────────────────────────────────────────
    RU_MTS("🇷🇺", "Russia", "MTS", SpoofConfig(
        simCountryIso = "ru", simOperatorMccMnc = "25001", simOperatorName = "MTS",
        networkCountryIso = "ru", networkOperatorMccMnc = "25001", networkOperatorName = "MTS",
        localeLanguage = "ru", localeCountry = "RU"
    )),
    RU_BEELINE("🇷🇺", "Russia", "Beeline", SpoofConfig(
        simCountryIso = "ru", simOperatorMccMnc = "25099", simOperatorName = "Beeline",
        networkCountryIso = "ru", networkOperatorMccMnc = "25099", networkOperatorName = "Beeline",
        localeLanguage = "ru", localeCountry = "RU"
    )),
    UA_KYIVSTAR("🇺🇦", "Ukraine", "Kyivstar", SpoofConfig(
        simCountryIso = "ua", simOperatorMccMnc = "25503", simOperatorName = "Kyivstar",
        networkCountryIso = "ua", networkOperatorMccMnc = "25503", networkOperatorName = "Kyivstar",
        localeLanguage = "uk", localeCountry = "UA"
    )),
    CZ_T_MOBILE("🇨🇿", "Czech Republic", "T-Mobile", SpoofConfig(
        simCountryIso = "cz", simOperatorMccMnc = "23001", simOperatorName = "T-Mobile",
        networkCountryIso = "cz", networkOperatorMccMnc = "23001", networkOperatorName = "T-Mobile",
        localeLanguage = "cs", localeCountry = "CZ"
    )),
    RO_ORANGE("🇷🇴", "Romania", "Orange", SpoofConfig(
        simCountryIso = "ro", simOperatorMccMnc = "22610", simOperatorName = "Orange",
        networkCountryIso = "ro", networkOperatorMccMnc = "22610", networkOperatorName = "Orange",
        localeLanguage = "ro", localeCountry = "RO"
    )),
    HU_TELEKOM("🇭🇺", "Hungary", "Telekom", SpoofConfig(
        simCountryIso = "hu", simOperatorMccMnc = "21630", simOperatorName = "Telekom",
        networkCountryIso = "hu", networkOperatorMccMnc = "21630", networkOperatorName = "Telekom",
        localeLanguage = "hu", localeCountry = "HU"
    )),

    // ── Asia-Pacific ───────────────────────────────────────────────────────────
    JP_DOCOMO("🇯🇵", "Japan", "NTT DOCOMO", SpoofConfig(
        simCountryIso = "jp", simOperatorMccMnc = "44010", simOperatorName = "NTT DOCOMO",
        networkCountryIso = "jp", networkOperatorMccMnc = "44010", networkOperatorName = "NTT DOCOMO",
        localeLanguage = "ja", localeCountry = "JP"
    )),
    JP_SOFTBANK("🇯🇵", "Japan", "SoftBank", SpoofConfig(
        simCountryIso = "jp", simOperatorMccMnc = "44020", simOperatorName = "SoftBank",
        networkCountryIso = "jp", networkOperatorMccMnc = "44020", networkOperatorName = "SoftBank",
        localeLanguage = "ja", localeCountry = "JP"
    )),
    KR_SKT("🇰🇷", "South Korea", "SK Telecom", SpoofConfig(
        simCountryIso = "kr", simOperatorMccMnc = "45005", simOperatorName = "SK Telecom",
        networkCountryIso = "kr", networkOperatorMccMnc = "45005", networkOperatorName = "SK Telecom",
        localeLanguage = "ko", localeCountry = "KR"
    )),
    AU_TELSTRA("🇦🇺", "Australia", "Telstra", SpoofConfig(
        simCountryIso = "au", simOperatorMccMnc = "50501", simOperatorName = "Telstra",
        networkCountryIso = "au", networkOperatorMccMnc = "50501", networkOperatorName = "Telstra",
        localeLanguage = "en", localeCountry = "AU"
    )),
    NZ_SPARK("🇳🇿", "New Zealand", "Spark", SpoofConfig(
        simCountryIso = "nz", simOperatorMccMnc = "53005", simOperatorName = "Spark",
        networkCountryIso = "nz", networkOperatorMccMnc = "53005", networkOperatorName = "Spark",
        localeLanguage = "en", localeCountry = "NZ"
    )),
    SG_SINGTEL("🇸🇬", "Singapore", "Singtel", SpoofConfig(
        simCountryIso = "sg", simOperatorMccMnc = "52501", simOperatorName = "Singtel",
        networkCountryIso = "sg", networkOperatorMccMnc = "52501", networkOperatorName = "Singtel",
        localeLanguage = "en", localeCountry = "SG"
    )),
    IN_AIRTEL("🇮🇳", "India", "Airtel", SpoofConfig(
        simCountryIso = "in", simOperatorMccMnc = "40410", simOperatorName = "Airtel",
        networkCountryIso = "in", networkOperatorMccMnc = "40410", networkOperatorName = "Airtel",
        localeLanguage = "hi", localeCountry = "IN"
    )),
    TH_AIS("🇹🇭", "Thailand", "AIS", SpoofConfig(
        simCountryIso = "th", simOperatorMccMnc = "52001", simOperatorName = "AIS",
        networkCountryIso = "th", networkOperatorMccMnc = "52001", networkOperatorName = "AIS",
        localeLanguage = "th", localeCountry = "TH"
    )),
    ID_TELKOMSEL("🇮🇩", "Indonesia", "Telkomsel", SpoofConfig(
        simCountryIso = "id", simOperatorMccMnc = "51010", simOperatorName = "Telkomsel",
        networkCountryIso = "id", networkOperatorMccMnc = "51010", networkOperatorName = "Telkomsel",
        localeLanguage = "in", localeCountry = "ID"
    )),
    PH_GLOBE("🇵🇭", "Philippines", "Globe", SpoofConfig(
        simCountryIso = "ph", simOperatorMccMnc = "51502", simOperatorName = "Globe",
        networkCountryIso = "ph", networkOperatorMccMnc = "51502", networkOperatorName = "Globe",
        localeLanguage = "en", localeCountry = "PH"
    )),
    MY_MAXIS("🇲🇾", "Malaysia", "Maxis", SpoofConfig(
        simCountryIso = "my", simOperatorMccMnc = "50212", simOperatorName = "Maxis",
        networkCountryIso = "my", networkOperatorMccMnc = "50212", networkOperatorName = "Maxis",
        localeLanguage = "ms", localeCountry = "MY"
    )),
    VN_VIETTEL("🇻🇳", "Vietnam", "Viettel", SpoofConfig(
        simCountryIso = "vn", simOperatorMccMnc = "45204", simOperatorName = "Viettel",
        networkCountryIso = "vn", networkOperatorMccMnc = "45204", networkOperatorName = "Viettel",
        localeLanguage = "vi", localeCountry = "VN"
    )),

    // ── Middle East & Africa ───────────────────────────────────────────────────
    AE_ETISALAT("🇦🇪", "UAE", "Etisalat", SpoofConfig(
        simCountryIso = "ae", simOperatorMccMnc = "42402", simOperatorName = "Etisalat",
        networkCountryIso = "ae", networkOperatorMccMnc = "42402", networkOperatorName = "Etisalat",
        localeLanguage = "ar", localeCountry = "AE"
    )),
    SA_STC("🇸🇦", "Saudi Arabia", "STC", SpoofConfig(
        simCountryIso = "sa", simOperatorMccMnc = "42001", simOperatorName = "STC",
        networkCountryIso = "sa", networkOperatorMccMnc = "42001", networkOperatorName = "STC",
        localeLanguage = "ar", localeCountry = "SA"
    )),
    TR_TURKCELL("🇹🇷", "Turkey", "Turkcell", SpoofConfig(
        simCountryIso = "tr", simOperatorMccMnc = "28601", simOperatorName = "Turkcell",
        networkCountryIso = "tr", networkOperatorMccMnc = "28601", networkOperatorName = "Turkcell",
        localeLanguage = "tr", localeCountry = "TR"
    )),
    ZA_VODACOM("🇿🇦", "South Africa", "Vodacom", SpoofConfig(
        simCountryIso = "za", simOperatorMccMnc = "65501", simOperatorName = "Vodacom",
        networkCountryIso = "za", networkOperatorMccMnc = "65501", networkOperatorName = "Vodacom",
        localeLanguage = "en", localeCountry = "ZA"
    )),
    EG_ORANGE("🇪🇬", "Egypt", "Orange", SpoofConfig(
        simCountryIso = "eg", simOperatorMccMnc = "60201", simOperatorName = "Orange",
        networkCountryIso = "eg", networkOperatorMccMnc = "60201", networkOperatorName = "Orange",
        localeLanguage = "ar", localeCountry = "EG"
    )),
    NG_MTN("🇳🇬", "Nigeria", "MTN", SpoofConfig(
        simCountryIso = "ng", simOperatorMccMnc = "62130", simOperatorName = "MTN",
        networkCountryIso = "ng", networkOperatorMccMnc = "62130", networkOperatorName = "MTN",
        localeLanguage = "en", localeCountry = "NG"
    )),

    // ── Latin America ──────────────────────────────────────────────────────────
    BR_VIVO("🇧🇷", "Brazil", "Vivo", SpoofConfig(
        simCountryIso = "br", simOperatorMccMnc = "72406", simOperatorName = "Vivo",
        networkCountryIso = "br", networkOperatorMccMnc = "72406", networkOperatorName = "Vivo",
        localeLanguage = "pt", localeCountry = "BR"
    )),
    AR_CLARO("🇦🇷", "Argentina", "Claro", SpoofConfig(
        simCountryIso = "ar", simOperatorMccMnc = "72230", simOperatorName = "Claro",
        networkCountryIso = "ar", networkOperatorMccMnc = "72230", networkOperatorName = "Claro",
        localeLanguage = "es", localeCountry = "AR"
    )),
    CO_CLARO("🇨🇴", "Colombia", "Claro", SpoofConfig(
        simCountryIso = "co", simOperatorMccMnc = "73001", simOperatorName = "Claro",
        networkCountryIso = "co", networkOperatorMccMnc = "73001", networkOperatorName = "Claro",
        localeLanguage = "es", localeCountry = "CO"
    ));

    /** Short display label for list items */
    val label: String get() = "$flag $countryName — $carrierName"
}
