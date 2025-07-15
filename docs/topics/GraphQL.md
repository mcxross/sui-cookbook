# GraphQL Client

The `Sui` instance in the Kotlin SDK is a GraphQL client under the hood. It offers a robust and user-friendly
interface for querying Sui network data, including `Transactions`, `Checkpoints`, `Objects`, and `Events`, from a data indexer
operator. If no custom operator is specified, it defaults to the `mainnet` GraphQL endpoint maintained by **Mysten Labs**.

> Event subscriptions are not yet supported and will become available once the GraphQL interface from data indexer
> operators reaches general availability.
{style="note"}

The `Sui()` instance is the entry point for data retrieval.

Create our client instance:

```kotlin
val sui = Sui()
```

Call the appropriate coins method on the client

> You can find a complete list of available methods and usage in the SDK [reference](https://mcxross.github.io/ksui/)

## Coins Data

The `Sui` class includes an abstraction called `Coin`, which provides access to the coin-related API.

### Account Balance

Fetch the balance of a specific coin type for a given address.

```Kotlin
val res  = sui.getBalance(AccountAddress("0x1f3...11c"))
```

### Total Supply

Fetch the total supply for a given coin type.

```Kotlin
val res = sui.getTotalSupply("0x2::sui::SUI")
```

## Move Data

The `Sui` class includes an abstraction called `Move`, which provides access to the move-related API.

### Normalized Move Function

Fetch the normalized (desugared and with types expanded) representation of a Move function.

```Kotlin
val res = sui.getNormalizedMoveFunction("package::module::function")
```

### Move Function Argument Types

Fetch the argument types for a specific Move function.

```Kotlin
val res = sui.getMoveFunctionArgTypes("package::module::function")
```

## Response Handling

Information regarding response handling can be found [here](response-handling.md)

## Reference

You can find a complete list of available methods and usage in the SDK [reference](https://mcxross.github.io/ksui/)