package com.crowcode.fitnessy

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crowcode.fitnessy.auth.LoginActivity
import com.crowcode.fitnessy.auth.SignUpActivity
import com.crowcode.fitnessy.onboard.ButtonUi
import com.crowcode.fitnessy.onboard.OnboardingScreen
import com.crowcode.fitnessy.onboard.OnboardingUtils
import com.crowcode.fitnessy.ui.theme.FitnessyTheme
import com.crowcode.fitnessy.ui.theme.loginBtn
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

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.welcome),
                    contentDescription = "just an image",
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = "Welcome To Fitnessy",
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontSize = 32.sp
                    )
                )
                Text(
                    text = "Get fit and stay healthy with our easy-to-use app.",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .padding(26.dp, 0.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        letterSpacing = 1.1.sp
                    )
                )
                LoginButton()
                SignUpButton()
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


    @Preview
    @Composable
    fun LoginButton() {
        val context = LocalContext.current
        val i = Intent(context, LoginActivity::class.java)
        ButtonUi(
            text = "Login",
            fontSize = 16,
            backgroundColor = loginBtn,
            textColor = Color.Black,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 24.dp)
                .fillMaxWidth()

        ) {
            context.startActivity(i)
        }
    }


    @Preview
    @Composable
    fun SignUpButton() {
        val context = LocalContext.current
        val i = Intent(context, SignUpActivity::class.java)
        ButtonUi(
            text = "Sign Up",
            fontSize = 16,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            context.startActivity(i)
        }
    }

}
