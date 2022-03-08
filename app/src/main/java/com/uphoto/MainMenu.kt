package com.uphoto

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView



/**
 * A simple [Fragment] subclass.
 * Use the [MainMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "main_activity"
class MainMenu : Fragment() {


    private lateinit var selectbutton: Button
    private lateinit var appname: TextView

    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "datapasser")
        dataPasser = context as OnDataPass
    }
    fun passData(data: String){
        dataPasser?.onDataPass(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "bundle start")


    }
    fun onClick(view: View)
    {
      passData("select screen")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        selectbutton = view.findViewById(R.id.select_button)
        appname = view.findViewById(R.id.textView)
        selectbutton.setOnClickListener{
            onClick(selectbutton)
        }

        Log.d(TAG, "view create")

        return view
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance():MainMenu{
            return MainMenu()
        }
    }
    override fun onDetach() {
        super.onDetach()
        dataPasser = null
    }
}