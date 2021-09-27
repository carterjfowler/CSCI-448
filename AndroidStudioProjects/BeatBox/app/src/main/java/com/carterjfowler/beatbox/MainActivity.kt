package com.carterjfowler.beatbox

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterjfowler.beatbox.databinding.ActivityMainBinding
import com.carterjfowler.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private  lateinit var beatBox: BeatBox
    private lateinit var scaleListener: ScaleGestureDetector
    private lateinit var binding: ActivityMainBinding

    private var scaleFactor = 3.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)
        scaleListener = ScaleGestureDetector(applicationContext, ScaleListener())

        binding  = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.playbackSpeedSeekBar.setOnSeekBarChangeListener(this)

        val playbackText = this.resources.getString( R.string.playback_speed_label, 100 )
        binding.playbackTextView.setText(playbackText)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, scaleFactor.toInt())
            adapter = SoundAdapter(beatBox.sounds)
        }
        binding.recyclerView.setOnTouchListener { v: View, event: MotionEvent ->
            scaleListener.onTouchEvent(event)
            if(!scaleListener.isInProgress) {
                onTouchEvent(event)
            } else {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val playbackText = this.resources.getString( R.string.playback_speed_label, progress )
        binding.playbackTextView.setText(playbackText)
        val floatRate : Float = (progress / 100.0f)
        beatBox.rate = floatRate
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }

    private inner class ScaleListener
        : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            scaleFactor *= detector?.scaleFactor ?: 1.0f
            if (scaleFactor < 1) scaleFactor = 1.0f
            else if (scaleFactor > 8) scaleFactor = 8.0f
            binding.recyclerView.layoutManager =
                GridLayoutManager(applicationContext, scaleFactor.toInt())
            return super.onScale(detector)
        }
    }

}
