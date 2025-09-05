package com.fedeveloper95.aodtile

import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class AODTileService : TileService() {

    private val SETTING_KEY = "doze_always_on"

    // Questo metodo viene chiamato quando il tile diventa visibile.
    override fun onStartListening() {
        super.onStartListening()
        // Aggiorna lo stato del tile in base al valore attuale dell'AOD.
        updateTileState()
    }

    // Questo metodo viene chiamato quando il tile viene cliccato.
    override fun onClick() {
        super.onClick()
        toggleAOD()
        updateTileState()
    }

    // Questo metodo gestisce la logica di attivazione/disattivazione.
    private fun toggleAOD() {
        try {
            // Legge lo stato attuale del valore 'doze_always_on'.
            val currentAodState = Settings.Secure.getInt(contentResolver, SETTING_KEY, 0)
            // Imposta il nuovo stato: 1 per attivo, 0 per disattivo.
            val newAodState = if (currentAodState == 1) 0 else 1
            Settings.Secure.putInt(contentResolver, SETTING_KEY, newAodState)

            // Mostra un messaggio di conferma.
            val message = if (newAodState == 1) "AOD attivato" else "AOD disattivato"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        } catch (e: SecurityException) {
            // Viene lanciata se non si ha il permesso WRITE_SECURE_SETTINGS.
            Toast.makeText(this, "Permesso WRITE_SECURE_SETTINGS non concesso. Concedilo tramite ADB.", Toast.LENGTH_LONG).show()
        }
    }

    // Questo metodo aggiorna l'aspetto del tile (stato attivo/inattivo).
    private fun updateTileState() {
        val tile = qsTile
        if (tile != null) {
            // Legge lo stato attuale.
            val aodState = Settings.Secure.getInt(contentResolver, SETTING_KEY, 0)
            // Imposta lo stato del tile.
            tile.state = if (aodState == 1) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            // Aggiorna il tile.
            tile.updateTile()
        }
    }
}