
package com.example.apppredict20

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver

@Database(
    entities = [
        Produto::class,
        Categoria::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun produtoDAO(): ProdutoDAO

    abstract fun categoriaDAO(): CategoriaDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obterBanco(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "app_predict_trabalho3_final_db"
                )
                    .setDriver(AndroidSQLiteDriver())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
