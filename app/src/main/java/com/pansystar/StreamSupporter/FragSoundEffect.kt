package com.pansystar.StreamSupporter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.hjson.JsonObject


/**
 * A simple [Fragment] subclass.
 * Use the [FragSoundEffect.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragSoundEffect : Fragment() {

    lateinit var mainActivity: MainActivity

    public fun init(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_frag_sound_effect, container, false)

        view.findViewById<Button>(R.id.tettereButton).setOnClickListener {
            var json = JsonObject()
            json["type"] = 0x00110001
            json["id"] = 1
            mainActivity.server.sendMessage(json)
        }

        view.findViewById<Button>(R.id.namidaButton).setOnClickListener {
            var json = JsonObject()
            json["type"] = 0x00110001
            json["id"] = 2
            mainActivity.server.sendMessage(json)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragSoundEffect.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragSoundEffect().apply {
                arguments = Bundle().apply {
                }
            }
    }
}