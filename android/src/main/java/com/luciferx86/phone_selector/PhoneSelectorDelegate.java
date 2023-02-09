package com.luciferx86.phone_selector;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.VisibleForTesting;

import com.google.android.gms.auth.api.identity.Identity;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PhoneSelectorDelegate extends ComponentActivity implements PluginRegistry.ActivityResultListener {
    private static final String TAG = "PhoneSelectorDelegate";
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

    private boolean setPendingMethodCallAndResult(final MethodChannel.Result result) {
        if (this.pendingResult != null) {
            return false;
        }
        this.pendingResult = result;
        return true;
    }

    private static void finishWithAlreadyActiveError(final MethodChannel.Result result) {
        result.error("already_active", "Phone Selector is already active", null);
    }

    public void requestHint(MethodChannel.Result result) {
//        if (!this.setPendingMethodCallAndResult(result)) {
//            finishWithAlreadyActiveError(result);
//            return;
//        }
//        HintRequest hintRequest = new HintRequest.Builder()
//                .setPhoneNumberIdentifierSupported(true)
//                .build();
//        PendingIntent intent = Credentials.getClient(this.activity).getHintPickerIntent(hintRequest);
//        try {
//            ActivityCompat.startIntentSenderForResult(this.activity, intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null,0, 0, 0, new Bundle());
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
        ActivityResultLauncher phoneNumberHintIntentResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                try {
                                    String phoneNumber = Identity.getSignInClient(activity).getPhoneNumberFromIntent(result.getData());
                                    finishWithSuccess(phoneNumber);
                                } catch (Exception e) {
                                    Log.e(TAG, "Phone Number Hint failed", e);
                                    finishWithError("1", e.getMessage());
                                }
                            }


                        });
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