package edu.umg.proyecto_gastify.datos

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Declara las tablas y la versión de la base de datos.
@Database(
    entities = [GastoEntidad::class, IngresoEntidad::class],
    version = 2,
    exportSchema = true
)
abstract class GastifyDatabase : RoomDatabase() {

    // Proporciona acceso a las operaciones de gastos.
    abstract fun gastoDao(): GastoDao

    abstract fun ingresoDao(): IngresoDao

    // Permite obtener la base de datos sin crear instancias repetidas.
    companion object {
        // Actualiza la base de datos sin borrar los gastos existentes.
        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS ingresos (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                montoCentavos INTEGER NOT NULL,
                descripcion TEXT NOT NULL,
                fuente TEXT NOT NULL,
                fechaMillis INTEGER NOT NULL
            )
            """.trimIndent()
                )
            }
        }


        @Volatile
        private var instancia: GastifyDatabase? = null

        fun obtenerInstancia(context: Context): GastifyDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    GastifyDatabase::class.java,
                    "gastify.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .build()
                    .also {
                        instancia = it
                    }
            }
        }
    }
}