package br.edu.utfpr.financescrudwithkotlinandsqlite.entity

data class Financa (
    var _id: Int,
    var tipo: String,
    var detalhe: String,
    var valor: Double,
    var dataLancamento: String
)