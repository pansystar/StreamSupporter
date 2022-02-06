package com.pansystar.StreamSupporter.ui.main

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.pansystar.StreamSupporter.MainActivity
import java.util.ArrayList

object VoiceRecognize {
    var speechRecognizer: SpeechRecognizer? = null
    var recognitionListener: RecognitionListener? = null
    var recognizerIntent: Intent? = null

    lateinit var mainActivity: MainActivity

    var isListening: Boolean = false

    fun init(mainActivity: MainActivity) {

        this.mainActivity = mainActivity

        recognitionListener = SimpleRecognizerListener(mainActivity)

        // Recognizer のレスポンスを取得するための Intent を生成する
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 60000)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 60000)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 60000)
    }

    fun restartRecognize() {
        if (speechRecognizer != null) {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            isListening = false
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(mainActivity)
        speechRecognizer?.setRecognitionListener(recognitionListener)

        if(!isListening) {
            isListening = true
            speechRecognizer?.startListening(recognizerIntent)
        }
    }
}

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener)
    : RecognitionListener {

    interface SimpleRecognizerResponseListener {
        fun onError(p0: Int)
        fun onPartialResults(speechText: String)
        fun onResultsResponse(speechText: String)
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.d("log:: ", "準備できてます")
    }

    override fun onRmsChanged(p0: Float) {
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onPartialResults(p0: Bundle?) {
        val key = SpeechRecognizer.RESULTS_RECOGNITION
        val mResult = p0?.getStringArrayList(key)

        var result: Array<String?>? = null

        if (mResult != null) {
            result = arrayOfNulls(mResult.size)
            mResult.toArray(result)
        }

        val finalResult: String = result!![0].toString()

        if(finalResult.isBlank()) {
            return
        }

        listener.onPartialResults(finalResult)
        print(finalResult)

    }

    override fun onEvent(p0: Int, p1: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
        Log.d("log:: ", "始め！")
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(p0: Int) {
        when (p0) {
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> VoiceRecognize.restartRecognize()
            SpeechRecognizer.ERROR_NO_MATCH -> VoiceRecognize.restartRecognize()
        }
        listener.onError(p0)
    }


    // 7. onResultsから取得した値で判定し、特定の言葉を表示する
    override fun onResults(bundle: Bundle?) {

        VoiceRecognize.restartRecognize()

//        if (bundle == null) {
//            listener.onResultsResponse("")
//            return
//        }
//
//        val key = SpeechRecognizer.RESULTS_RECOGNITION
//        val result = bundle.getStringArrayList(key)
//        // なぜかスペースが入力されてしまう時があったので、スペースがあった場合は取り除くようにした。
//        val speechText = result?.get(0)
//
//        if (speechText.isNullOrEmpty()) {
//            listener.onResultsResponse("")
//        } else {
//            listener.onResultsResponse(speechText)
//        }


    }
}