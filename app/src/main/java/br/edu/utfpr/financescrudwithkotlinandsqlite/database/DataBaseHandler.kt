package br.edu.utfpr.financescrudwithkotlinandsqlite.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.financescrudwithkotlinandsqlite.entity.Financa

class DataBaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, detalhe TEXT, valor REAL, dataLancamento TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val TABLE_NAME = "financa"
        private const val COD = 0
        private const val TIPO = 1
        private const val DETALHE = 2
        private const val VALOR = 3
        private const val DATALANCAMENTO = 4

        // Query para criar a tabela
        private const val CREATE_TABLE_USERS = (
                "CREATE TABLE " + TABLE_NAME + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "tipo TEXT," +
                        "detalhe TEXT," +
                        "valor REAL," +
                        "datalancamento TEXT" +
                        ")"
                )
    }

    fun insert(financa : Financa){
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("tipo", financa.tipo)
        registro.put("detalhe", financa.detalhe)
        registro.put("valor", financa.valor)
        registro.put("dataLancamento", financa.dataLancamento)

        db.insert(TABLE_NAME, null, registro)
    }
/*
    fun update(financa : Financa){
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("tipo", financa.tipo)
        registro.put("detalhe", financa.detalhe)
        registro.put("valor", financa.valor)
        registro.put("dataLancamento", financa.dataLancamento)

        db.update(TABLE_NAME, registro, "_id = ${financa._id}", null)
    }

    fun delete(id: Int){
        val db = this.writableDatabase

        db.delete(TABLE_NAME, "_id=${id}", null)
    }

    @SuppressLint("Recycle")
    fun find(id : Int) : Financa?{
        val db = this.writableDatabase

        val registro = db.query(
            "financa",
            null,
            "_id=${id}", //WHERE
            null, //argumentos do WHERE
            null,
            null,
            null
        )

        if(registro.moveToNext()){
            val financa = Financa(
                id,
                registro.getString(TIPO),
                registro.getString(DETALHE),
                registro.getString(VALOR),
                registro.getString(DATALANCAMENTO)
            )
            return financa
        }else{
            return null
        }
    }
*/
    @SuppressLint("Recycle")
    fun list() : MutableList<Financa>{
        val db = this.writableDatabase
        val registro = db.query(
            TABLE_NAME,
            null,
            null, //WHERE
            null, //argumentos do WHERE
            null,
            null,
            null
        )

        val registros = mutableListOf<Financa>()

        while(registro.moveToNext()){
            val financa = Financa(
                registro.getInt(COD),
                registro.getString(TIPO),
                registro.getString(DETALHE),
                registro.getDouble(VALOR),
                registro.getString(DATALANCAMENTO)
            )

            registros.add(financa)
        }

        return registros
    }

    fun calcularSaldo(): Double {
        val db = this.readableDatabase
        var saldo = 0.0

        val cursor = db.rawQuery("SELECT tipo, valor FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))

                if (tipo.equals("C", ignoreCase = true)) {
                    saldo += valor
                } else if (tipo.equals("D", ignoreCase = true)) {
                    saldo -= valor
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return saldo
    }
/*
    fun cursorList() : Cursor {
        val db = this.writableDatabase
        val registro = db.query(
            "financa",
            null,
            null, //WHERE
            null, //argumentos do WHERE
            null,
            null,
            null
        )

        return registro
    }
*/
}