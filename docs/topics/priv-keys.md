# Private Keys

The Kotlin SDK supports vanilla account schemes such as `ED25519`, `SECP256K1` and `SECP256R1`.

In this chapter, we’ll explore account creation, restoration and usage for these schemes

## Creating new Accounts

All account types in the Kotlin SDK extend from a common `Account` abstract class.

This class provides a `create` static method that allows you to create a new account of a specified type and defaults to
`SignatureScheme.ED25519` if no type is specified.

```kotlin
val account = Account.create() // creates an ED25519 acc by default
```

You can also specify the signature scheme as follows:

```kotlin
val secp256k1Account = Account.create(SignatureScheme.SECP256K1)
val secp256r1Account = Account.create(SignatureScheme.SECP256R1)
```

You can also create accounts of a specific type directly by invoking the `generate` method on the respective classes:

```kotlin
val ed25519Account = Ed25519Account.generate()
val secp256k1Account = Secp256k1Account.generate()
val secp256r1Account = Secp256r1Account.generate()
```

## Importing an existing Account

In case you already have existing account credentials like a Bech32 encoded private key or
passphrase that you'd like to import and use, there's an overloaded `import` static method
on the `Accounts` class. Pass in either the private key string or the passphrase.

For a private key:

```kotlin
```

{ src="account.kt" include-lines="3" }

For a passphrase:

```kotlin
```

{ src="account.kt" include-lines="4" }

## Signing Messages

You can sign messages using the `sign` method on the `Account` class.
This returns a `Result<ByteArray, Exception>`, so you can handle success and failure cases
appropriately. Here's an example of signing a message:

```kotlin
val account = Secp256k1Account.generate()
val message = "Hello, Sui!".toByteArray()
val signature = account.sign(message).unwrap()
println("Signature: $signature")
```

## Signature Verification

You can verify signatures using the `verify` method on the `Account` class.
This returns a `Result<Boolean, Exception>`, so you can handle success and failure cases
appropriately. Here's an example of verifying a signature:

```kotlin
val account = Secp256k1Account.generate()

// Sign a message
val message = "Hello, Sui!".toByteArray()
val sig = account.sign(message).unwrap()

// Verify the signature using the account's public key
val isValid = account.publicKey.verify(message, sig).unwrap()

println("Sig is valid: $isValid") // Output: Sig is valid: true
```

The SDK extensively uses the `Result` type to model success and failure cases. This ensures you handle both outcomes 
explicitly, making your code more predictable and safer by default.
For more details on response handling, check out the [response handling](response-handling.md) chapter.