package com.developer.cookie.myquote.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.developer.cookie.myquote.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class helps set specific color first vowel in title
 */
public class ColorationTextChar {

    public static Spannable setFirstVowelColor(String text, String language, Context context) {
        Spannable newText = new SpannableString(text);
        if (!text.isEmpty() || !text.equals("")) {
            int index = getFirstVowelIndex(text, language);
            if (index != -1) {
                newText = new SpannableString(text);
                newText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.card_background)),
                        index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return newText;
            }
        }
        return newText;
    }

    /**
     * Method returns index of first vowel in title (for english and russian languages)
     *
     * @param text current text
     * @param language current language
     * @return index of first vowel
     */
    private static int getFirstVowelIndex(String text, String language) {

        String patternString;

        if (language.equals("en")) {
            patternString = "(?i:[aeiouy]).*";
            return getIndex(patternString, text);

        } if (language.equals("ru")) {
            patternString = "(?ui:[аеёиоуыэюя]).*";
            return getIndex(patternString, text);
        }

        return -1;
    }

    private static int getIndex(String patternString, String text) {
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.start();
        } else {
            return -1;
        }
    }
}
