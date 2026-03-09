package com.fedeveloper95.aodtile

import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class AODTileService : TileService() {

    private val settingKey = "doze_always_on"

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        toggleAOD()
        updateTileState()
    }

    private fun toggleAOD() {
        try {
            val currentAodState = Settings.Secure.getInt(contentResolver, settingKey, 0)
            val newAodState = if (currentAodState == 1) 0 else 1
            Settings.Secure.putInt(contentResolver, settingKey, newAodState)
            val message = if (newAodState == 1) getString(R.string.aod_enabled) else getString(R.string.aod_disabled)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val aodState = Settings.Secure.getInt(contentResolver, settingKey, 0)
        tile.state = if (aodState == 1) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }
}