package com.linh.retrofittest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.linh.retrofittest.ui.theme.RetrofitTestTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.Duration.Companion.days
import kotlin.time.times

object RetrofitClient {
    private const val BASE_URL = "http://100.124.45.48:3000/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val eosEyesApi: EosEyesApi by lazy {
        RetrofitClient.retrofit.create(EosEyesApi::class.java)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitTestTheme {
                var text: String by remember { mutableStateOf("Hello, World!") }
                LaunchedEffect(Unit) {
                    val endInstant = Clock.System.now()
                    val endTimestamp = endInstant.toEpochMilliseconds()
                    val startTimestamp = getStartTime(SamplingInterval.ONE_HOUR, endInstant)

                    val response = ApiClient.eosEyesApi.getRamOhlcData(
                        SamplingInterval.ONE_HOUR.value,
                        startTimestamp,
                        endTimestamp
                    )
                    text = response.toString()
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(text = text, modifier = Modifier.padding(innerPadding))
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
    RetrofitTestTheme {
        Greeting("Android")
    }
}

private fun getStartTime(samplingInterval: SamplingInterval, endInstant: Instant): Long {
    return when (samplingInterval) {
        SamplingInterval.FIVE_MINUTES -> {
            endInstant.minus(1.days).toEpochMilliseconds()
        }

        SamplingInterval.FIFTEEN_MINUTES -> {
            endInstant.minus(2.days).toEpochMilliseconds()
        }

        SamplingInterval.THIRTY_MINUTES -> {
            endInstant.minus(4.days).toEpochMilliseconds()
        }

        SamplingInterval.ONE_HOUR -> {
            endInstant.minus(7.days).toEpochMilliseconds()
        }

        SamplingInterval.FOUR_HOURS -> {
            endInstant.minus(30.days).toEpochMilliseconds()
        }

        SamplingInterval.ONE_DAY -> {
            endInstant.minus(30 * 5.days).toEpochMilliseconds() // 5 months
        }

        SamplingInterval.ONE_WEEK -> {
            endInstant.minus(53 * 7.days).toEpochMilliseconds() // 53 weeks
        }

        SamplingInterval.ONE_MONTH -> {
            1527786000000 // 2018-06-01
        }
    }
}

enum class SamplingInterval(val value: String) {
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    THIRTY_MINUTES("30m"),
    ONE_HOUR("1h"),
    FOUR_HOURS("4h"),
    ONE_DAY("1d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1m")
}