# Programmable Transaction Blocks

On Sui, transactions are made up of a series of commands that operate on inputs to produce a result. These grouped 
commands are called programmable transaction blocks (PTBs), and they form the basis of all user transactions on the network.

The SDK provides two ways for building transactions depending on the users' needs or preference. 
The **DSL Style** and the **Builder Style**.

## DSL Style

With this approach, the SDK provides a top level `ptb {...}` that provides the scope for composing a PTB. 
Use this block to define a full transaction inline. This style is best when your logic is self-contained and readable 
as a single unit.

Here is an example of sending multiple coins to a single address:

```Kotlin
val ptb = ptb {
  val coins = splitCoins {
       coin = Argument.GasCoin
       into = listOf(1_000_000UL, 2_000_000UL)
  }

  transferObjects {
    objects = coins
    to = address(RECIEPIENT)
  }
}
```

You can attach multiple commands of the same type to a transaction, as well. For example, to get a list of transfers 
and iterate over them to transfer coins to each of them:

```Kotlin

 val transfers = listOf(
      TransferInfo(recipient = BOB_ADDRESS, amount = 100_000_000UL),
      TransferInfo(recipient = CAROL_ADDRESS, amount = 200_000_000UL),
)

val ptb = ptb {
  val coins = splitCoins {
    coin = Argument.GasCoin
    into = transfers.map { pure(it.amount) }
  }

  transfers.forEachIndexed { index, transfer ->
    transferObjects {
      objects = listOf(coins[index])
      to = address(transfer.recipient)
    }
  }
}
```

## Builder Style

Use this when you need to build a PTB gradually — for example, across different parts of your app or conditionally based 
on user input.

```Kotlin
val builder = ProgrammableTransactionBuilder()

builder.moveCall(FUNCTION_ID, args = listOf(builder.pure(0UL)))
```

To finalize the assembling of the PTB with the builder style, call the `build` method as shown below:

```Kotlin
val ptb = builder.build()
```

## Inputs and Results

Some Move functions need arguments. The SDK provides a powerful `arg()` function that intelligently creates the correct 
argument type for you based on the value and its Move type.

### For simple values (Primitives):

```Kotlin
val amount = arg(1_000_000UL) // u64
val name = arg("My First NFT")   // string
val recipient = arg(BOB_ADDRESS) // address
```
### For Objects:
To use an on-chain object as an input, simply pass its ID string. The builder will automatically fetch its details 
during the final build step.

```Kotlin
val myNFT = `object`("0xCOIN_OBJECT_ID")
```

### For Lists (Vectors):

The `arg()` function is smart enough to handle lists correctly. It automatically determines whether to serialize the 
list directly (for pure types like `address`) or to create a `makeMoveVec` command (for a list of objects).

```Kotlin
val senders = arg(
      listOf(
        "0x7a...902",
        "0x7a...903",
      ),
      TypeTag.Address,
)
```

### Chaining Commands and Using Results

The real power of PTBs comes from using the result of one command as an input to the next. The APIs make this as intuitive
as possible.

Here’s how to perform the common task of creating an on-chain object and sending the new object to a recipient:

```Kotlin
val ptb = ptb {
    val members = arg(
        listOf(
          "0xfa...904",
          "0xcd...fe0",
        ),
        TypeTag.Address,
    )
      
    val acc = moveCall {
      target = TARGET
      arguments = listOf(members)
    }

    transferObjects {
      objects = listOf(acc)
      to = address(BOB_ADDRESS)
    }
}
```

## Operator Overloads

The SDK provides helper overloads when working with the DSL style for passing inputs to
commands. These are the `unaryPlus` operator and `plus` operator.

#### unaryPlus operator

Use this when you're passing a single input into a command as shown below:

```Kotlin
ptb {
    val coins = splitCoins {
        coin = Argument.GasCoin
        into = +pure(1_000_000UL)
    }
}
```

#### plus operator

Use this when passing a list of inputs into a command as shown below:

```Kotlin
ptb {
    val coins = splitCoins {
        coin = Argument.GasCoin
        into = pure(1_000_000UL) + pure(2_000_000UL)
    }
}
```

## Signing and Executing

To execute a transaction on-chain, we need to first sign than execute the transaction.

The SDK provides a method that collapses these two steps into a single method, `signAndExecuteTransactionBlock`.

```Kotlin
val res = sui.signAndExecuteTransactionBlock(
    signer = ALICE_ACCOUNT,
    ptb = ptb,
    gasBudget = 15_000_000UL,
)
```

## Observing the results of a transaction

When you call `sui.signAndExecuteTransaction`, the transaction is finalized on the blockchain before the function returns. 
However, the effects of that transaction might not be visible right away.

There are two ways to observe the outcome of a transaction. The first is by using the options parameter available in 
methods like `sui.signAndExecuteTransaction`. You can specify options such as `showObjectChanges` and `showBalanceChanges`, 
which include additional details about the transaction’s effects in the response. These can be used immediately in your 
application to update the UI or trigger further actions.

The second approach is to query the blockchain directly using RPC methods like `sui.getBalances`, which return the current 
state of objects or balances for a specific address. These methods rely on the RPC node indexing the transaction effects, 
which may not happen right after execution.

To ensure that subsequent RPC calls reflect the transaction’s outcome, you can call `waitForTransaction` on the client. 
This method waits until the transaction has been fully processed and its effects are available to the indexer.

```Kotlin
val res = 
    sui.signAndExecuteTransactionBlock(ALICE_ACCOUNT, ptb = ptb)
    .expect {
      "Transaction Execution Failed"
    }
 
sui.waitForTransaction(res?.executeTransactionBlock?.effects?
    .transactionBlock?.rPC_TRANSACTION_FIELDS?.digest!!)
```

## Response Handling

You can find more info on response handling [here](response-handling.md)


## Performing Dry Runs

Simulate running a transaction to inspect its effects without committing to them on-chain.

Once a transaction has been assembled, its serialized bytes can be retrieved as a Base64-encoded string, as shown below:

<compare first-title="Infix Notation" second-title="Dot Notation">
    <code-block lang="kotlin">
       val txBytes = ptb compose (ALICE_ACCOUNT to 10_000_000UL)
    </code-block>
    <code-block lang="kotlin">
       val txBytes = ptb.compose(ALICE_ACCOUNT to 10_000_000UL)
    </code-block>
</compare>

This can then be passed to the `dryRunTransactionBlock` method on the `Sui` instance as:

```Kotlin
val res = sui.dryRunTransactionBlock(txBytes)
```

In the next section of Transactions, we're going to see how to visually compose PTBs using
**[PTB Studio](https://ptb.studio)**.