package com.topjava.graduation;

import com.topjava.graduation.model.Voice;

public class VoiceTestData {
    public static final MatcherFactory.Matcher<Voice> VOICE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Voice.class, "restaurant", "user");

    public static Voice getNew() {
        return new Voice();
    }
}