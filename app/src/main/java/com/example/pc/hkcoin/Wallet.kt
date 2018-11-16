package com.example.pc.hkcoin

import java.security.KeyPairGenerator

import java.security.PrivateKey

import java.security.PublicKey


data class Wallet(val publicKey: PublicKey, val privateKey: PrivateKey, val blockChain: BlockChain) {


    companion object { //선언 없이 바로 사용 가능

        fun create(blockChain: BlockChain): Wallet {

            val generator = KeyPairGenerator.getInstance("RSA") //key 생성 (public,privat)

            generator.initialize(2048)

            val keyPair = generator.generateKeyPair()



            return Wallet(keyPair.public, keyPair.private, blockChain)

        }

    }


    val balance: Int
        get() {

            return getMyTransactions().sumBy { it.amount }

        }


    private fun getMyTransactions(): Collection<TransactionOutput> {

        return blockChain.UTXO.filterValues { it.isMine(publicKey) }.values

    } //UTXO  트랜잭션에 의한 결과물들의 합, 수수료,보내는 값에 대한 지갑 http://brownbears.tistory.com/382


    fun sendFundsTo(recipient: PublicKey, amountToSend: Int): Transaction {

        if (amountToSend > balance) {

            throw IllegalArgumentException("Insufficient funds") as Throwable

        }





        val tx = Transaction.create(sender = publicKey, recipient = publicKey, amount = amountToSend)

        tx.outputs.add(TransactionOutput(recipient = recipient, amount = amountToSend, transactionHash = tx.hash))


        var collectedAmount = 0

        for (myTx in getMyTransactions()) {

            collectedAmount += myTx.amount

            tx.inputs.add(myTx)



            if (collectedAmount > amountToSend) {

                val change = collectedAmount - amountToSend

                tx.outputs.add(TransactionOutput(recipient = publicKey, amount = change, transactionHash = tx.hash))

            }



            if (collectedAmount >= amountToSend) {

                break

            }

        }

        return tx.sign(privateKey) //검증 가지고 있는 것보다 보낼 것이 더 많다면

    }

}