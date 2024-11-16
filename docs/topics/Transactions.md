# Transactions

Transactions allow us to alter the state of a Blockchain. In Sui,
these transactions are made up of a chain of commands termed
Programmable Transaction Blocks.

The execution of these commands is an all-or-nothing operation, in that, either all
commands execute successfully or the entire transaction fails. Construction of
these PTBs is either by SDKs or Transaction Builders.

In this chapter, we'll cover how to construct PTBs with both an SDK and a Transaction
Builder.