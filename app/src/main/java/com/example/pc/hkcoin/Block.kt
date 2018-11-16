package com.example.pc.hkcoin



import android.util.Log
import java.time.Instant


data class Block(
    val previousHash: String,

    val transactions: MutableList<Transaction> = mutableListOf(),

    val timestamp: Long = Instant.now().toEpochMilli(), //만들어진 시간

    val nonce: Long = 0, //작업증명 횟수

    var hash: String = ""
) {
    // 우리가 원하는 데이터를 여기다 선언 하면 될 뜻


    init {

        hash = calculateHash()

    }


    fun calculateHash(): String {

        return "$previousHash$transactions$timestamp$nonce".hash() //데이터 해쉬

    }


    fun addTransaction(transaction: Transaction): Block {


        if (transaction.isSignatureValid())

            transactions.add(transaction)

        return this

    }

}