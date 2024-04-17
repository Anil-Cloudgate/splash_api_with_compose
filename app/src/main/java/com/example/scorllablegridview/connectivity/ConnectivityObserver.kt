package com.example.scorllablegridview.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val connectionState: Flow<ConnectionState>

    val currentConnectionState: ConnectionState
}
