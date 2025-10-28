package com.example.lifetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.example.lifetracker.ui.theme.LifeTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Data class representing a lifecycle event with its name, timestamp, and color for UI display
data class LifecycleEvent(
    val name: String,
    val timestamp: String,
    val color: Color
)

// ViewModel that holds the list of lifecycle events and a flag to show/hide snackbar notifications
class LifeTrackerViewModel : ViewModel() {
    // Mutable list of events that will notify observers on changes
    private val _events = mutableStateListOf<LifecycleEvent>()
    val events: List<LifecycleEvent> get() = _events

    // Boolean state to control snackbar visibility
    var showSnackbar by mutableStateOf(true)
        private set

    // Logs a new lifecycle event with the current time and color coding
    fun logEvent(eventName: String, color: Color) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _events.add(0, LifecycleEvent(eventName, time, color))
    }

    fun toggleSnackbar(enabled: Boolean) {
        showSnackbar = enabled
    }
}

// Lifecycle observer to log lifecycle callbacks and show snackbar notifications when events occur
class LifeTrackerObserver(
    private val viewModel: LifeTrackerViewModel,
    private val snackbarScope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) : DefaultLifecycleObserver {

    // Helper function to log events and show snackbar if enabled
    private fun onEvent(eventName: String, color: Color) {
        viewModel.logEvent(eventName, color)
        if (viewModel.showSnackbar) {
            snackbarScope.launch {
                snackbarHostState.showSnackbar("Lifecycle event: $eventName")
            }
        }
    }

    // Lifecycle callback overrides to capture each event with associated color
    override fun onCreate(owner: LifecycleOwner) {
        onEvent("onCreate()", Color(0xFF4CAF50))
    }

    override fun onStart(owner: LifecycleOwner) {
        onEvent("onStart()", Color(0xFF2196F3))
    }

    override fun onResume(owner: LifecycleOwner) {
        onEvent("onResume()", Color(0xFFFFC107))
    }

    override fun onPause(owner: LifecycleOwner) {
        onEvent("onPause()", Color(0xFFFF5722))
    }

    override fun onStop(owner: LifecycleOwner) {
        onEvent("onStop()", Color(0xFFF44336))
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: LifeTrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LifeTrackerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                // Register lifecycle observer with snackbar state and scope
                LaunchedEffect(Unit) {
                    lifecycle.addObserver(LifeTrackerObserver(viewModel, coroutineScope, snackbarHostState))
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = { LifeTrackerTopAppBar(viewModel) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LifeTrackerScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
// Top app bar with title, menu icon, and an action button to toggle snackbar notifications
@Composable
fun LifeTrackerTopAppBar(viewModel: LifeTrackerViewModel) {
    TopAppBar(
        title = { Text("LifeTracker – Lifecycle Logger") },
        navigationIcon = {
            IconButton(onClick = { /* handle navigation click */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { viewModel.toggleSnackbar(!viewModel.showSnackbar) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = if (viewModel.showSnackbar) "Disable Snackbar" else "Enable Snackbar"
                )
            }
        }
    )
}

// Main screen composable displaying lifecycle events in a scrollable list with color-coded backgrounds
@Composable
fun LifeTrackerScreen(viewModel: LifeTrackerViewModel, modifier: Modifier = Modifier) {
    val events = viewModel.events

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(events) { event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(event.color.copy(alpha = 0.2f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${event.name} • ${event.timestamp}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LifeTrackerPreview() {
    LifeTrackerTheme {
        LifeTrackerScreen(viewModel = LifeTrackerViewModel())
    }
}
