package com.luciferx86.phone_selector

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.EventChannel


/** PhoneSelectorPlugin */
class PhoneSelectorPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
	/// The MethodChannel that will the communication between Flutter and native Android
	///
	/// This local reference serves to register the plugin with the Flutter Engine and unregister it
	/// when the Flutter Engine is detached from the Activity
	private var channel: MethodChannel?=null
	private val CREDENTIAL_PICKER_REQUEST = 120
	private var activity: Activity? = null
	var result: Result? = null
	private var eventSink: EventChannel.EventSink? = null

	override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
		channel = MethodChannel(flutterPluginBinding.binaryMessenger, "phone_selector")
		channel.setMethodCallHandler(this)
	}

	override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
		if (call.method == "callPhoneSelector") {
			this.result = result
			requestHint();
		} else {
			result.notImplemented()
		}
	}

	override fun onAttachedToActivity(@NonNull binding: ActivityPluginBinding) {
		activity = binding.activity
		binding.addActivityResultListener(this)
	}

	override fun onDetachedFromActivityForConfigChanges() {
		// TODO: the Activity your plugin was attached to was
		// destroyed to change configuration.
		// This call will be followed by onReattachedToActivityForConfigChanges().
	}

	override fun onReattachedToActivityForConfigChanges(@NonNull activityPluginBinding: ActivityPluginBinding) {
		// TODO: your plugin is now attached to a new Activity
		// after a configuration change.
	}

	override fun onDetachedFromActivity() {
		cleanUp()
	}

	override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
		cleanUp()
	}


	private fun requestHint() {
		val hintRequest: HintRequest = HintRequest.Builder()
				.setPhoneNumberIdentifierSupported(true)
				.build()
		val intent: PendingIntent = Credentials.getClient(this.activity!!).getHintPickerIntent(hintRequest)
		try {
			startIntentSenderForResult(this.activity!!, intent.intentSender, CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, Bundle())
		} catch (e: SendIntentException) {
			e.printStackTrace()
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
		if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
			// Obtain the phone number from the result
			val credentials: Credential = data!!.getParcelableExtra(Credential.EXTRA_KEY)
			result?.success(credentials.getId().substring(3));
		} else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
			this.result?.error("No Numbers found", null, null)
		} else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == 0) {
			result?.success("");
		} else {
			this.result?.error("CREDENTIAL_PICKER_REQUEST", null, null)
		}
		return false
	}

	private fun cleanUp() {
		activity = null
		channel?.setMethodCallHandler(null)
		channel = null
		eventSink = null
		result = null;
	}
}
