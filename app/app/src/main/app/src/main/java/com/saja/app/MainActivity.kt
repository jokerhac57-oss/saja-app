package com.saja.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val perms = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.POST_NOTIFICATIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(60, 60, 60, 60) }
        box.addView(TextView(this).apply { text = "SAJA"; textSize = 32f; gravity = Gravity.CENTER })
        box.addView(Button(this).apply { text = "START LISTENING"; setOnClickListener { requestOrStart() } })
        box.addView(Button(this).apply { text = "STOP"; setOnClickListener { stopService(Intent(this@MainActivity, ListenService::class.java)) } })
        setContentView(box)
    }

    private fun requestOrStart() {
        if (Build.VERSION.SDK_INT >= 23 && perms.any { checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }) {
            requestPermissions(perms, 1)
        } else {
            startService(Intent(this, ListenService::class.java).apply { addFlags(0) })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (results.isNotEmpty() && results[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            startForegroundService(Intent(this, ListenService::class.java))
        }
    }
}
