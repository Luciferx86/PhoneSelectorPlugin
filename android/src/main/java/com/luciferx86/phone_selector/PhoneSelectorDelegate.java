package com.luciferx86.phone_selector;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PhoneSelectorDelegate implements PluginRegistry.ActivityResultListener {
    private static final String TAG = "PhoneSelectorDelegate";
    private static final int REQUEST_CODE = (PhoneSelectorPlugin.class.hashCode() + 43) & 0x0000ffff;
    private static final int CREDENTIAL_PICKER_REQUEST = 120;

    private final Activity activity;
    private MethodChannel.Result pendingResult;
    private EventChannel.EventSink eventSink;

    public PhoneSelectorDelegate(final Activity activity) {
        this(
                activity,
                null
        );
    }

    public void setEventHandler(final EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @VisibleForTesting
    PhoneSelectorDelegate(final Activity activity, final MethodChannel.Result result) {
        this.activity = activity;
        this.pendingResult = result;
    }

    @Override
    public boolean onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            finishWithSuccess(credentials.getId().substring(3));
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            Log.i(TAG, "User cancelled the picker request");
            finishWithError("np_numbers", "No Numbers found");
            return true;
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == 0) {
            finishWithSuccess("");
        }
        return false;
    }

    private void finishWithSuccess(String data) {
        if (eventSink != null) {
            this.dispatchEventStatus(false);
        }

        // Temporary fix, remove this null-check after Flutter Engine 1.14 has landed on stable
        if (this.pendingResult != null) {
            this.pendingResult.success(data);
            this.clearPendingResult();
        }
    }

    public void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this.activity).getHintPickerIntent(hintRequest);
        try {
            this.activity.startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, this.activity.getIntent(), 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void finishWithError(final String errorCode, final String errorMessage) {
        if (this.pendingResult == null) {
            return;
        }

        if (eventSink != null) {
            this.dispatchEventStatus(false);
        }
        this.pendingResult.error(errorCode, errorMessage, null);
        this.clearPendingResult();
    }

    private void dispatchEventStatus(final boolean status) {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(final Message message) {
                eventSink.success(status);
            }
        }.obtainMessage().sendToTarget();
    }

    private void clearPendingResult() {
        this.pendingResult = null;
    }


}