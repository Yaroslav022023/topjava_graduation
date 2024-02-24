package com.graduation.topjava;

import com.graduation.topjava.model.Voice;

import java.time.LocalTime;

public class VoiceTestData {
    public static final MatcherFactory.Matcher<Voice> VOICE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Voice.class, "restaurant", "user");

    public static Voice getNew() {
        Voice voice = new Voice();
        voice.setTime(LocalTime.of(9, 0));
        return voice;
    }
}
