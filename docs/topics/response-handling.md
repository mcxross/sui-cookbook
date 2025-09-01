# Response Handling

Every data-fetching operation in the SDK returns a `Result`, which models either a success `(Ok)` or a failure `(Err)`. 
This design ensures you handle both outcomes explicitly, making your code more predictable and safer by default.

You can work with the result in several ways:

### Using `when` Expression

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