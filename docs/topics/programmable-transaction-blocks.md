# Programmable Transaction Blocks

The Kotlin SDK does a great job by abstracting PTB construction using a simple
expressive Domain Specific Language as we'll explore below.

The `programmableTx {}` DSL block is the top most block that creates a typed scope for PTB construction. Within it, we can
call all available commands, pass their results to the next command and so forth.

For example, the Sui `moveCall` command expects a mandatory `target` argument, optional`arguments`, and
`typedArguments` argument. The natural mapping for these is the `moveCall` within `programmableTx` DSL,
where each property (target, arguments, and typedArgument) is used directly with matching names.

Let's see how to call `hello_world` **Move** **function** in the `hello_wolrd` **module** of 
`0x883393ee444fb828aa0e977670cf233b0078b41d144e6208719557cb3888244d` **package**. The function
has the following signature:

```Rust
[[[public entry fun hello_world(arg0: u64)|https://testnet.suivision.xyz/package/0x883393ee444fb828aa0e977670cf233b0078b41d144e6208719557cb3888244d?tab=Code]]]
```

Things to note, the function expects a `u64` as an argument. Has no type parameters. The module name and package. Click on it for details.

Given the above properties, we can call it as follows:

```Kotlin
val ptb = programmableTx {
      command {
         moveCall {
            target = "0x883393ee444fb828aa0e977670cf233b0078b41d144e6208719557cb3888244d::hello_wolrd::hello_world"
            typeArguments = emptyList()
            arguments = inputs(0UL)
       }
   }
}
```

You can also pass results from one command to the next by storing them in a variable, as shown below:

```Kotlin
val ptb = programmableTx {
    command {
        val splitCoins = splitCoins {
            coin = Argument.GasCoin
            into = inputs(1_000_000UL)
        }
        
        transferObjects {
            objects = inputs(splitCoins)
            to = input(alice.address)
        }
    }
}
```

## Transaction Block Dry Run

Simulate a transaction before execution:

```Kotlin
val result = [[[sui|getting-started.topic#sui-client]]].dryRunTransactionBlock(ptb)
```

## Sign And Execute Transaction Block

Once you've constructed, you can sign and execute it:

```Kotlin
val result = [[[sui|getting-started.topic#sui-client]]].signAndExecuteTransactionBlock(alice, ptb)
```

In the next section of Transactions, we're going to see how to visually compose PTBs using
**[PTB Studio](https://ptb.studio)**.