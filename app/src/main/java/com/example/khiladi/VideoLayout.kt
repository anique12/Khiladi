package com.example.khiladi

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.video_layout_feeds.*

class VideoLayout : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val controller = MediaController(activity)
        val customVideoView = videoView as CustomVideoView
        customVideoView.setPlayPauseListener(object : CustomVideoView.PlayPauseListener{
            override fun onPause() {
                customVideoView.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            }

            override fun onPlay() {
                customVideoView.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }

        })
        customVideoView.setMediaController(controller)
        customVideoView.setBackgroundColor(resources.getColor(android.R.color.darker_gray))

        return inflater.inflate(R.layout.video_layout_feeds, container, false)



    }
}


class CustomVideoView : VideoView {

    private var mListener: PlayPauseListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setPlayPauseListener(listener: PlayPauseListener) {
        mListener = listener
    }

    override fun pause() {
        super.pause()
        if (mListener != null) {
            mListener!!.onPause()
        }
    }

    override fun start() {
        super.start()
        if (mListener != null) {
            mListener!!.onPlay()
        }
    }

    interface PlayPauseListener {
        fun onPlay()
        fun onPause()
    }

}