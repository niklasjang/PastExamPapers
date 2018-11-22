package com.example.niklasjang.bottomnavigationbar_with_fragment_example


import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.ArrayList

class ImageFragment() : Fragment() {
    private var imageModelArrayList = ArrayList<ImageModel>()
    private val myImageList = intArrayOf(R.drawable.harley2, R.drawable.benz2, R.drawable.vecto, R.drawable.webshots, R.drawable.bikess)

    fun newInstance(position: Int): ImageFragment {
        val f = ImageFragment()
        // Supply index input as an argument.
        val args = Bundle()
        args.putInt("index", position)
        f.arguments = args

        var ivPhoto = view?.findViewById<ImageView>(R.id.ivPhoto)
        ivPhoto?.setImageResource(imageModelArrayList[position].getImage_drawables())
        return f
    }

    //will be changed?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("123","123")
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageModelArrayList = populateList()
        //var ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)
    }



    private fun populateList(): ArrayList<ImageModel> {
        val list = ArrayList<ImageModel>()
        for (i in 0..3) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }
        return list
    }

}
