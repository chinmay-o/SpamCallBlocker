package com.example.prefixcallblocker;

import android.telecom.Call;
import android.telecom.CallScreeningService;

public class PrefixCallScreeningService extends CallScreeningService {
    private static final String BLOCK_PREFIX_DIGITS = "911409";

    @Override public void onScreenCall(Call.Details details) {
        String number = null;
        if (details.getHandle() != null) number = details.getHandle().getSchemeSpecificPart();
        boolean shouldBlock = isBlockedPrefix(number);

        CallResponse.Builder response = new CallResponse.Builder();
        if (shouldBlock) {
            response.setDisallowCall(true)
                    .setRejectCall(true)
                    .setSkipCallLog(false)
                    .setSkipNotification(false);
        }
        respondToCall(details, response.build());
    }

    private boolean isBlockedPrefix(String rawNumber) {
        if (rawNumber == null || rawNumber.isEmpty()) return false;
        String digits = rawNumber.replaceAll("[^0-9]", "");
        // Match +91 1409..., 911409..., and international 0091 1409... formats.
        if (digits.startsWith("00")) digits = digits.substring(2);
        return digits.startsWith(BLOCK_PREFIX_DIGITS);
    }
}
