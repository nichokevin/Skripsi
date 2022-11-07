package com.example.skripsi.classification

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.android.gms.common.internal.Preconditions
import com.google.common.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class PoseCP {
    private val POSE_SAMPLES_FILE = "pose/fitness_pose_samples.csv"

    private val PLANK_CLASS = "plank"
    private val COBRA_CLASS = "cobra"
    private val TREE_CLASS = "tree"
    private val POSE_CLASSES = arrayOf(
        PLANK_CLASS, COBRA_CLASS, TREE_CLASS
    )
    private var isStreamMode = false

    //private val emaSmoothing: EMASmoothing? = null
    private var poseClassifier: PoseClassifier? = null

    /*@WorkerThread
    fun PoseClassifierProcessor(context: Context?, isStreamMode: Boolean) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper())
        this.isStreamMode = isStreamMode
        if (isStreamMode) {
            emaSmoothing = EMASmoothing()
            repCounters = ArrayList<RepetitionCounter>()
            lastRepResult = ""
        }
        if (context != null) {
            loadPoseSamples(context)
        }
    }


    private fun loadPoseSamples(context: Context) {
        val poseSamples: MutableList<PoseSample?> = java.util.ArrayList<PoseSample?>()
        try {
            val reader = BufferedReader(
                InputStreamReader(context.assets.open(POSE_SAMPLES_FILE))
            )
            var csvLine = reader.readLine()
            while (csvLine != null) {
                // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
                val poseSample: PoseSample = PoseSample.getPoseSample(csvLine, ",")
                if (poseSample != null) {
                    poseSamples.add(poseSample)
                }
                csvLine = reader.readLine()
            }
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Error when loading pose samples.\n$e"
            )
        }
        poseClassifier = PoseClassifier(poseSamples)
        if (isStreamMode) {
            for (className in POSE_CLASSES) {
                repCounters.add(RepetitionCounter(className))
            }
        }
    }*/

}