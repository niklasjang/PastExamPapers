package com.example.pc.hkcoin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import java.security.Timestamp
import java.time.Instant


class MainA : AppCompatActivity() {
    //Log.d 를 사용 할때 절대  원래 변수를 인자로 쓰지 마시오
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val blockChain = BlockChain()

        val wallet1 = Wallet.create(blockChain)

        val wallet2 = Wallet.create(blockChain)



        println("Wallet 1 balance: ${wallet1.balance}")

        println("Wallet 2 balance: ${wallet2.balance}")



        val tx1 = Transaction.create(sender = wallet1.publicKey, recipient = wallet1.publicKey, amount = 100) //블록을 만들 데이터, SuperUser

        tx1.outputs.add(TransactionOutput(recipient = wallet1.publicKey, amount = 100, transactionHash = tx1.hash)) // 보낸 데이터 저장

        tx1.sign(wallet1.privateKey) //서명



        var genesisBlock = Block(previousHash = "0") //첫 번째 블록

        genesisBlock.addTransaction(tx1) // 트랜젝션을 블록에 추가

        genesisBlock = blockChain.add(genesisBlock) // 블록체인에 블록이 하나 있다. 블록체인(첫번째 블록)



        println("Wallet 1 balance: ${wallet1.balance}")

        println("Wallet 2 balance: ${wallet2.balance}")



        val tx2 = wallet1.sendFundsTo(recipient = wallet2.publicKey, amountToSend = 33) //sendFundsTo 함수 안에서 트랜 잭션 creat

        val secondBlock = blockChain.add(Block(genesisBlock.hash).addTransaction(tx2))
        genesisBlock=secondBlock

        println("Wallet 1 balance: ${wallet1.balance}")

        println("Wallet 2 balance: ${wallet2.balance}")




    }
}








