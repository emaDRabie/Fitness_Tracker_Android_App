package com.crowcode.fitnessy

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crowcode.fitnessy.onboard.OnboardingScreen
import com.crowcode.fitnessy.onboard.OnboardingUtils
import com.crowcode.fitnessy.ui.theme.FitnessyTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessyTheme {
                if (onboardingUtils.isOnboardingCompleted()) {
                    HomeScreen()
                } else {
                    OnboardingFlow()
                }
            }
        }
    }

    @Composable
    private fun HomeScreen() {
        val context = LocalContext.current
        val intent = Intent(context, HomeActivity::class.java)

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Home Screen",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.clickable {
                        context.startActivity(intent)
                    }
                )
                Text(
                    text = "Emad Rabie",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }

    @Composable
    private fun OnboardingFlow() {
        val scope = rememberCoroutineScope()
        OnboardingScreen {
            onboardingUtils.setOnboardingCompleted()
            scope.launch {
                setContent {
                        HomeScreen()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun HomeScreenPreview() {
            HomeScreen()
    }
}
