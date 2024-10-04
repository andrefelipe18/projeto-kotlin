package com.example.p2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createManutencoesTable = ("CREATE TABLE manutencoes (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    nome_peca_servico TEXT NOT NULL,\n" +
                "    data_manutencao INT NOT NULL,\n" +
                "    valor_gasto REAL NOT NULL,\n" +
                "    nome_prestador TEXT,\n" +
                "    quilometragem INTEGER NOT NULL,\n" +
                "    observacoes TEXT\n" +
                ");\n")

        val createNotificationTable = ("CREATE TABLE notifications (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    title TEXT NOT NULL,\n" +
                "    value TEXT NOT NULL,\n" +
                "    type TEXT NOT NULL\n" +
                ");\n")

        db.execSQL(createNotificationTable)
        db.execSQL(createManutencoesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS manutencoes")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "app_database.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper =
            instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
    }
}