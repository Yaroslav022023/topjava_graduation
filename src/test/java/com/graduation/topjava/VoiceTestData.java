package com.graduation.topjava;

import com.graduation.topjava.model.Voice;

public class VoiceTestData {
    public static final MatcherFactory.Matcher<Voice> VOICE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Voice.class, "restaurant", "user");

    public static Voice getNew() {
        return new Voice();
    }
}
