package com.example.scorllablegridview.connectivity

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}
