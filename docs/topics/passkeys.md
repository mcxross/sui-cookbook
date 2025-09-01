# Passkeys

Passkeys are a modern authentication method that replaces traditional passwords with cryptographic keys. 
They provide a more secure and user-friendly way to authenticate users across various platforms and devices.

> The SDK currently only supports Passkeys in Android environment.
> iOS and Web support will be added in future releases.
{style="note"}

## Prequisites

In order to use passkeys, you need to create an association between your application and a website your own.

### Add support for Digital Asset Links

You can declare this association by completing the following steps:

1. Create a Digital Asset Links JSON file. For example, to declare that the website https://signin.example.com
and an Android app with the package name `com.example` can share sign-in credentials, create a file named `assetlinks.json` 
with the following content:

```json
[
  {
    "relation" : [
      "delegate_permission/common.handle_all_urls",
      "delegate_permission/common.get_login_creds"
    ],
    "target" : {
      "namespace" : "android_app",
      "package_name" : "com.example.android",
      "sha256_cert_fingerprints" : [
        SHA_HEX_VALUE
      ]
    }
  }
]
```

You can generate the SHA256 fingerprint using the following command:

```bash
./gradle signingReport
```

2. Host the Digital Assets Link JSON file at the following location on the sign-in domain:

```plain text
https://domain[:optional_port]/.well-known/assetlinks.json
```

For example, if your sign-in domain is signin.example.com, host the JSON file at https://signin.example.com/.well-known/assetlinks.json.

The MIME type for the Digital Assets Link file must be JSON. Verify that the server sends a Content-Type: application/json header in the response.

3. Verify that your host permits Google to retrieve your Digital Asset Link file. If you have a robots.txt file, it must 
allow the Googlebot agent to retrieve /.well-known/assetlinks.json. Most sites can allow any automated agent to retrieve 
files in the /.well-known/ path so that other services can access the metadata in those files:

````Plain Text
User-agent: *
Allow: /.well-known/
````

> Note: the https://signin.example.com/.well-known/assetlinks.json link must return a 200 HTTP response with a 
> JSON MIME Content-Type header. Returning a 301 or 302 HTTP redirect or a non-JSON Content-Type causes verification to 
> fail. The following is an example showing a request and the related response headers.
{style="note"}

```Plain Text
> GET /.well-known/assetlinks.json HTTP/1.1
> User-Agent: curl/7.35.0
> Host: signin.example.com

< HTTP/1.1 200 OK
< Content-Type: application/json
```

## Creating a Passkey

Once you've set up the Digital Asset Links, passkey creation follows a similar API as other account types.

The `PasskeyAccount` class provides a static `create` method that initiates the passkey creation process. 
This methods requires a `Provider` instance, which is responsible for handling the platform-specific passkey operations.

```kotlin
val provider = PasskeyProvider(context, "signin.mcxross.xyz")
```

The default `PasskeyProvider` implementation requires an Android `Context` and the domain name associated with your application.

> Note: The domain name must match the one used in the Digital Asset Links file.
> Also, ensure that your application has internet permissions in the AndroidManifest.xml file.
{style="note"}

Once you have the provider set up, you can create a new passkey account as follows:

```kotlin
val result = PasskeyAccount.create(provider, "User Name")
```

The `create` method returns a `Result<PasskeyAccount, Exception>`, allowing you to handle both success and failure cases appropriately.
If the creation is successful, you can retrieve the `PasskeyAccount` instance from the `Ok` variant of the result.
Here's an example of creating a passkey account:

```kotlin
val provider = PasskeyProvider(context, "signin.mcxross.xyz")
val result = PasskeyAccount.create(provider, "User Name")
when (result) {
    is Result.Ok -> {
        val passkeyAccount = result.value
        // Use the passkey account
    }
    is Result.Err -> {
        val error = result.error
        // Handle the error
    }
}
```

> Note: During passkey creation, this is the only time the public key is accessible.
> No later operations like signing will expose the public key.
> Ensure to store it securely for future use. This is what you'll use to derive your Sui address.
{style="warning"}


## Account Restoration

To restore an existing passkey account, you can use the `PasskeyAccount` constructor along with a `PasskeyPublicKey` and
`Provider` instance.

```kotlin
val pk = PasskeyPublicKey(publicKeyBytes)
val provider = PasskeyProvider(context, "signin.mcxross.xyz")
val account = PasskeyAccount(pk, provider)
```

## Signing Transactions

To use passkeys for signing Sui transactions, simply pass the `PasskeyAccount` instance to the transaction builder 
methods, just like you would with other account types.

```kotlin
val sui = Sui()

val account = PasskeyAccount(...) // existing passkey account

val ptb = ptb {
    val coins = splitCoins {
        coin = Argument.GasCoin
        into = +pure(1_000_000UL)
    }
    transferObjects {
        objects = coins
        to = address("0x644...688")
    }
}

sui.signAndExecuteTransactionBlock(manager.currentAccount!!, ptb)
```

For more details on transaction building and execution, refer to the [Transactions](programmable-transaction-blocks.md) guide.
          
## Signing Messages
You can sign messages using the `sign` method on the `PasskeyAccount` class.
This returns a `Result<ByteArray, Exception>`, so you can handle success and failure cases appropriately. 
Here's an example of signing a message:

```kotlin
val account = PasskeyAccount(...) // existing passkey account
val message = "Hello, Sui!".toByteArray()
val result = account.sign(message)
when (result) {
    is Result.Ok -> {
        val signature = result.value
        // Use the signature
    }
    is Result.Err -> {
        val error = result.error
        // Handle the error
    }
}
```

> Note: The sign operation may prompt the user for authentication, such as biometric verification or device PIN.
> This is typically how you sign in with passkeys i.e., signing a challenge message.
{style="note"}

## Signature Verification
You can verify signatures using the `verify` method on the `PasskeyAccount` class.
This returns a `Result<Boolean, Exception>`, so you can handle success and failure cases appropriately. 
Here's an example of verifying a signature:

```kotlin
val account = PasskeyAccount(...) // existing passkey account
val message = "Hello, Sui!".toByteArray()
val signature = ... // signature to verify
val result = account.verify(message, signature)
when (result) {
    is Result.Ok -> {
        val isValid = result.value
        // Use the verification result
    }
    is Result.Err -> {
        val error = result.error
        // Handle the error
    }
}
```

## Passkey Management

While the SDK provides the core functionality for creating and using passkeys,
it does not include a full-fledged passkey management system.
You may want to implement additional features such as public key storage based on your application's requirements.

Once your account is set up, you can start performing transactions on Sui. Let's
get into that in the next chapter.