# Accounts

## Creating new Accounts

To create a new Sui Account, simply invoke the static `create` method on the `Account` class.

```kotlin
```

{ src="account.kt" include-lines="1" }

## Importing an existing Account

In case you already have existing account credentials like a Bech32 encoded private key or
passphrase that you'd like to import and use.

For a private key:

```kotlin
```

{ src="account.kt" include-lines="3" }

For a passphrase:

```kotlin
```

{ src="account.kt" include-lines="4" }

## Get Account Balance

Once you've an `account`, you can either retrieve the balance for a specific coin type or for
all coin types.

For a specific coin type:

```kotlin
val suiBalance = [[[sui|getting-started.topic#sui-client]]].getBalance(account.address)
```

This defaults to the `0x2::sui::SUI` coin type, or you can pass a particular coin type argument to the `type` parameter
as shown below:

```kotlin
val particularTypeBalance = [[[sui|getting-started.topic#sui-client]]].getBalance(account.address, PARTICULAR_TYPE)
```

You can also simply retrieve all coin type balances as follows:

```kotlin
val allBalances = [[[sui|getting-started.topic#sui-client]]].getAllBalances(account.address)
```

## Get Account Objects

You can also retrieve objects owned by a given account address:

```kotlin
val ownedObjects = [[[sui|getting-started.topic#sui-client]]].getOwnedObjects(account.address)
```

## Get Account Coins

Account Coins retrieval also follows the same pattern

```kotlin
val coins = [[[sui|getting-started.topic#sui-client]]].getCoins(account.address)
```

## Get Account Stakes

You can fetch stakes for an account as follows:

```kotlin
val stakes = [[[sui|getting-started.topic#sui-client]]].getStakes(account.address)
```

## Resolve Account Name Service Names

```Kotlin
val names = [[[sui|getting-started.topic#sui-client]]].resolveNameServiceNames(account.address)
```

## Request Test Tokens

If your `Sui()` client is configured for testnet, you can request test tokens from a faucet
as follows:

```kotlin
val response = [[[sui|getting-started.topic#sui-client]]].requestTestTokens(account.address)
```