package com.graduation.topjava.util;

import com.graduation.topjava.model.Voice;

import java.time.LocalTime;

public class VoiceUtil {
    private VoiceUtil() {
    }

    public static Boolean isAvailableUpdate(Voice voice) {
        return voice.getTime().isBefore(LocalTime.of(11, 0));
    }
}
