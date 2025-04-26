package com.crowcode.fitnessy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crowcode.fitnessy.onboard.OnboardingScreen
import com.crowcode.fitnessy.onboard.OnboardingUtils
import com.crowcode.fitnessy.ui.theme.FitnessyTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txt = findViewById<TextView>(R.id.txt)
        txt.setOnClickListener {
            val i = Intent(this, OnboardingActivity::class.java)
            startActivity(i)
        }

        setContent {
            FitnessyTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (onboardingUtils.isOnboardingCompleted()) {
                        ShowHomeScreen()
                    } else {
                        ShowOnboardingScreen()

                    }
                }
            }
        }
    }

    @Composable
    private fun ShowHomeScreen() {
        val context = LocalContext.current
        val i = Intent(context, HomeActivity::class.java)
        Column(
            modifier = Modifier
                .padding(all = 50.dp)
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .clickable {
                        context.startActivity(i)
                    }

            )
        }


    }

    @Composable
    private fun ShowOnboardingScreen() {
        val scope = rememberCoroutineScope()
        OnboardingScreen {
            onboardingUtils.setOnboardingCompleted()
            scope.launch {
                setContent {
                    ShowHomeScreen()
                }
            }
        }


    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FitnessyTheme {
            ShowHomeScreen()
        }
    }
}