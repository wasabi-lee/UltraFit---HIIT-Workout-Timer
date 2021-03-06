package io.incepted.ultrafittimer.view

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import timber.log.Timber

class ProgressHelper(private val progressBar: MaterialProgressBar) {

    private var progressAnimator: ObjectAnimator? = null

    private var pausedTime = 0L

    private var paused = false


    private fun startProgressAnim(max: Int?, progress: Int?, paused: Boolean?) {

        if (max == null || progress == null || paused == null) return


        val duration = (max - progress) * 1000

        Timber.d("duration $duration / max $max / progress $progress")


        if (duration < 0) return

        progressBar.max = max * 1000
        progressBar.progress = progress * 1000


        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", progress * 1000, max * 1000)
        progressAnimator?.duration = duration.toLong()
        progressAnimator?.interpolator = LinearInterpolator()

        if (!paused)
            progressAnimator?.start()
    }


    fun animateProgressBar(max: Int?, progress: Int?, paused: Boolean?) {
            if (max == null || progress == null || paused == null) return
            startProgressAnim(max, progress, paused)

    }


    fun completeProgress() {
        progressAnimator?.end()
        progressBar.max = 100
        progressBar.progress = 100
    }


    fun pauseProgressBar() {
        pausedTime = progressAnimator?.currentPlayTime ?: 0L
        progressAnimator?.cancel()
        paused = true
    }

    fun resumeProgressBar() {
        progressAnimator?.currentPlayTime = pausedTime
        progressAnimator?.start()
        paused = false
    }

}