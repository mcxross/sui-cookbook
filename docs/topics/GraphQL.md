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

Every data-fetching operation in the SDK returns a `Result`, which models either a success `(Ok)` or a failure `(Err)`. This design ensures you handle both outcomes explicitly, making your code more predictable and safer by default.

You can work with the result in several ways:

### Using when Expression

This is the most explicit and idiomatic way to handle both cases:
```Kotlin
val res = sui.getBalance(AccountAddress("0x1f3...11c"))

when (res) {
    is Result.Ok -> {
        val data = res.value
        // Handle successful result
    }
    is Result.Err -> {
        val error = res.error
        // Handle error case
    }
}
```

### Using Convenience Functions

The Result class includes helper methods for more concise handling:
- `unwrap()`: Returns the value if successful, or throws if it’s an error.
- `unwrapErr()`: Returns the error if failed, or throws if it’s successful.
- `expect("message")`: Like `unwrap()`, but includes a custom error message.
- `expectErr { "message" }`: Like `unwrapErr()`, with a custom error message.
```Kotlin
val data = result.expect { "Failed to fetch account balance" }
```

Use these when you’re confident about the outcome, or in testing/debug scenarios where failure should immediately surface.

## Reference

You can find a complete list of available methods and usage in the SDK [reference](https://mcxross.github.io/ksui/)