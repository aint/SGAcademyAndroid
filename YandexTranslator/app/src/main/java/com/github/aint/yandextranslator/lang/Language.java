package com.github.aint.yandextranslator.lang;

public enum Language {
    Afrikaans("af"),
    Arabic("ar"),
    Azerbaijani("az"),
    Basque("eu"),
    Bashkir("ba"),
    Belarusian("be"),
    Bulgarian("bg"),
    Bengali("bn"),
    Bosnian("bs"),
    Catalan("ca"),
    Cebuano("ceb"),
    Chinese("zh"),
    Czech("cs"),
    Welsh("cy"),
    Danish("da"),
    German("de"),
    Greek("el"),
    English("en"),
    Esperanto("eo"),
    Estonian("et"),
    Persian("fa"),
    Finnish("fi"),
    French("fr"),
    Irish("ga"),
    Scottish("gd"),
    Galician("gl"),
    Gujarati("gu"),
    Hebrew("he"),
    Hindi("hi"),
    Croatian("hr"),
    Haitian("ht"),
    Hungarian("hu"),
    Armenian("hy"),
    Indonesian("id"),
    Icelandic("is"),
    Italian("it"),
    Japanese("ja"),
    Javanese("jv"),
    Georgian("ka"),
    Kazakh("kk"),
    Kannada("kn"),
    Korean("ko"),
    Kirghiz("ky"),
    Latin("la"),
    Lithuanian("lt"),
    Latvian("lv"),
    Malagasy("mg"),
    Maori("mi"),
    Macedonian("mk"),
    Malayalam("ml"),
    Mongolian("mn"),
    Marathi("mr"),
    Malay("ms"),
    Maltese("mt"),
    Nepali("ne"),
    Dutch("nl"),
    Norwegian("no"),
    Punjabi("pa"),
    Polish("pl"),
    Portuguese("pt"),
    Romanian("ro"),
    Russian("ru"),
    Sinhalese("si"),
    Slovak("sk"),
    Slovenian("sl"),
    Albanian("sq"),
    Serbian("sr"),
    Sundanese("su"),
    Spanish("es"),
    Swedish("sv"),
    Swahili("sw"),
    Tamil("ta"),
    Telugu("te"),
    Tajik("tg"),
    Thai("th"),
    Tagalog("tl"),
    Turkish("tr"),
    Tatar("tt"),
    Udmurt("udm"),
    Ukrainian("uk"),
    Urdu("ur"),
    Uzbek("uz"),
    Vietnamese("vi"),
    Yiddish("yi");

    private final String langCode;

    Language(String langCode) {
        this.langCode = langCode;
    }

    public static Language lookUp(String languageCode) {
        for (Language language : values()) {
            if (language.getLangCode().equalsIgnoreCase(languageCode)) {
                return language;
            }
        }
        return null;
    }

    public String getLangCode() {
        return langCode;
    }
}
