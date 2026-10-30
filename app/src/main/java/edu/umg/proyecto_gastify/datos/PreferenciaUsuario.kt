package edu.umg.proyecto_gastify.datos

import android.content.Context

// Guarda las preferencias del usuario en este teléfono.
class PreferenciasUsuario(context: Context) {

    private val preferencias = context.applicationContext
        .getSharedPreferences(
            "preferencias_usuario",
            Context.MODE_PRIVATE
        )

    // Devuelve el nombre guardado o un texto vacío.
    fun obtenerNombre(): String {
        return preferencias.getString("nombre", "") ?: ""
    }

    // Guarda el nombre sin espacios sobrantes en los extremos.
    fun guardarNombre(nombre: String) {
        preferencias.edit()
            .putString("nombre", nombre.trim())
            .apply()
    }
}
