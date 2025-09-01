# Accounts

In order to perform actions on Sui, you need an account. An account is represented by a key pair (public and private keys) 
that allows you to sign transactions and manage your assets on the Sui network.

Sui is cryptographically agile, supporting transaction signing with Google accounts (via zkLogin), passkeys, and 
standard key schemes such as Ed25519, ECDSA, and more.

The Kotlin SDK currently supports the following types of accounts:

- **Ed25519Account**: This account type uses the Ed25519 signature scheme.
- **Secp256k1Account**: This account type uses the Secp256k1 signature scheme.
- **Secp256r1Account**: This account type uses the Secp256r1 signature scheme.
- **PasskeyAccount**: This account type uses passkeys for authentication.

All account types have a common predicatable interface, allowing you to interact with them consistently.