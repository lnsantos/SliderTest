package com.lnsanto.customtexting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lnsanto.customtexting.databinding.ActivityMainBinding
import com.lnsanto.customtexting.draw.ProgressDraw


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}


class SliderCustomView @JvmOverloads constructor(
    ctx: Context,
    attr: AttributeSet? = null,
    theme: Int = 0
) : AppCompatSeekBar(ctx, attr, theme) {

    private val progressDraw = ProgressDraw(resources)
    private val progressList = arrayListOf<ProgressItem>()
    private var total = 0f

    fun setup(data: ArrayList<ProgressItem>, dataTotal: Float) {
        total = dataTotal
        progressList.addAll(data)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        log("......", default = false)
        log("......", default = false)
        log("START DRAW", default = false)
        log("progress $progress", default = false)

        val progressBarWidth = width
        val progressBarHeight = height
        val thumbOffSet = progressBarHeight

        log("progressBarWidth : $progressBarWidth", default = false)
        log("progressBarHeight : $progressBarHeight")
        log("thumbOffSet : $thumbOffSet")

        if (progressList.size > 0) {

            var lastProgressX = 0
            var progressItemWidth = 0
            var progressItemRight = 0

            var currentRightDraw = 0

            val lastPosition = progressList.size
            var progressIndex = 0

            loop@ for (progressItem in progressList) {

                thumb = drawCircle(thumbOffSet, progressItem.color, Color.BLACK)

                val progressPaint = Paint().apply {
                    color = resources.getColor(progressItem.color)
                }

                val percentage = (progressItem.percentage / total) * 100

                progressItemWidth = ((percentage * progressBarWidth) / 100).toInt()
                progressItemRight = lastProgressX + progressItemWidth

                if (
                    lastPosition == progressIndex &&
                    progressItemRight != progressBarWidth
                ) {
                    progressItemRight = progressBarWidth
                }

                log("percentage : $percentage", default = false)
                log("lastProgressX : $lastProgressX")
                log("progressItemWidth : $progressItemWidth", default = false)
                log("progressItemRight : $progressItemRight", default = false)
                log("lastPosition : $lastPosition")
                log("progressIndex : $progressIndex")

                val nextPercentage = (progressItemRight.toFloat() / progressBarWidth) * 100

                log("nextPercentage : $nextPercentage", default = false)

                currentRightDraw = ((progress * progressBarWidth) / 100).toInt()

                Rect().apply {
                    val progressLeft = lastProgressX
                    val progressTop = progressBarHeight / 5
                    val progressRight = currentRightDraw
                    val progressBottom = (progressBarHeight - progressTop)

                    log("progressTop : $progressTop")

                    set(
                        progressLeft - 10,
                        progressTop,
                        progressRight,
                        progressBottom
                    )
                }.also {

                    if (progressIndex == 0) {
                        canvas?.drawRect(
                            it,
                            progressPaint
                        )
                    } else {
                        canvas?.drawRect(
                            it.apply { left = it.left + 10 },
                            Paint().apply {
                                color = Color.WHITE
                            }
                        )
                        canvas?.drawRect(
                            it,
                            progressPaint
                        )
                    }
                }
                lastProgressX = progressItemRight
                val restBackground = progressBarWidth - currentRightDraw
                val backgroundWidth = currentRightDraw + restBackground

                Rect().apply {
                    val progressLeft = currentRightDraw
                    val progressTop = progressBarHeight / 5
                    val progressRight = backgroundWidth
                    val progressBottom =(progressBarHeight - progressTop)

                    set(
                        progressLeft,
                        progressTop,
                        progressRight,
                        progressBottom
                    )
                }.also {
                    canvas?.drawRect(
                        it,
                        Paint().apply {
                            color = Color.BLACK
                        }
                    )
                }

                if (nextPercentage > progress) {
                    break@loop
                }

                progressIndex++
            }

        }

        super.onDraw(canvas)
    }

    private fun drawCircle(size: Int, backgroundColor: Int, borderColor: Int): GradientDrawable? {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setSize(size,size)
        shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        shape.setColor(resources.getColor(backgroundColor))
        shape.setStroke(10, borderColor)
        return shape
    }

    private fun log(data: String, default:Boolean = true) {

        if (default){
            return
        }

        Log.d("CUSTOM_VIEW_GG", data)
    }

    data class ProgressItem(
        val color: Int,
        val percentage: Float
    )
}














































