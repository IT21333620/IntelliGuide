package com.example.intelliguide.models

class Message {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }

    var message: String = ""
    var sentBy: String = ""


}