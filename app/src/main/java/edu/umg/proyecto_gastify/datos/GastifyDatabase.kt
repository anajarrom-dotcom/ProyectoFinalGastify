package edu.umg.proyecto_gastify.datos

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room

// Declara las tablas y la versión de la base de datos.
@Database(
    entities = [GastoEntidad::class],
    version = 1,
    exportSchema = true
)
abstract class GastifyDatabase : RoomDatabase() {

    // Proporciona acceso a las operaciones de gastos.
    abstract fun gastoDao(): GastoDao

    // Permite obtener la base de datos sin crear instancias repetidas.
    companion object {

        @Volatile
        private var instancia: GastifyDatabase? = null

        fun obtenerInstancia(context: Context): GastifyDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    GastifyDatabase::class.java,
                    "gastify.db"
                ).build().also {
                    instancia = it
                }
            }
        }
    }
}