package com.graduation.topjava.util;

import com.graduation.topjava.model.Voice;
import com.graduation.topjava.util.exception.VotingRestrictionsException;

import java.time.LocalTime;

public class VoiceUtil {
    private VoiceUtil() {
    }

    public static boolean isAvailableUpdate(Voice voice) {
        if (voice.getTime().isBefore(LocalTime.of(11, 0))) {
            return true;
        }
        throw new VotingRestrictionsException("It is not possible to change the voting time after 11:00 a.m.");
    }
}
