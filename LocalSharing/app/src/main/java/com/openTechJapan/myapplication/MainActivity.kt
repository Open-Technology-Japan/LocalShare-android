package com.openTechJapan.myapplication

import android.net.nsd.NsdManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openTechJapan.myapplication.nsd.NsdService
import com.openTechJapan.myapplication.ui.theme.LocalSharingTheme
import org.webrtc.PeerConnectionFactory
import timber.log.Timber

class MainActivity : ComponentActivity() {

    val nsdService = NsdService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        nsdService.registerService(50000, this)
        enableEdgeToEdge()
        setContent {
            LocalSharingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button({}) {
                            Text("Hello")
                        }
                        Button({}) {
                            Text("Hello")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LocalSharingTheme {
        Greeting("Android")
    }
}