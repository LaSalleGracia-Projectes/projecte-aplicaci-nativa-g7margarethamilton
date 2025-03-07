package com.example.projecte_aplicaci_nativa_g7margarethamilton.Model

import android.provider.ContactsContract.CommonDataKinds.Nickname

data class Usuari(
    var nickname: String,
    var email: String,
    var contrasenya: String
    )