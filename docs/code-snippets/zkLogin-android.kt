private val zkLoginSuccess = mutableStateOf(false)
private var activityResult: ActivityResult? = null
private var zkLoginResponse: ZKLoginResponse? = null

private val zkLoginResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        activityResult = result
        zkLoginResponse =
          result.data?.getStringExtra("zkLoginResponse")?.let { jsonToZKLoginResponse(it) }
        zkLoginSuccess.value = true
      } else {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
    }
}


 val googleZKIntent =
              context zkLogin
                ZKLoginRequest(
                  OpenIDServiceConfiguration(
                    Google(),
                    System.getenv("GOOGLE_CLIENT_ID")!!, // Replace with your Google Client ID
                    System.getenv("GOOGLE_REDIRECT_URI")!!, // Replace with your Google Redirect URI,
                )
              )
launcher.launch(googleZKIntent)