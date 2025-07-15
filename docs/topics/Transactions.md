# Transactions

Transactions are how we change the state of the blockchain. On Sui, a transaction consists of a sequence of commands 
known as Programmable Transaction Blocks (PTBs).

These commands are executed atomically — meaning either all commands succeed, or the entire transaction fails. PTBs can 
be constructed using either an SDK or a Transaction Builder.

In this chapter, we’ll walk through how to build PTBs using both approaches.