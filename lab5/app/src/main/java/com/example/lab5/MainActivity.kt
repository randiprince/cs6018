package com.example.lab5

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab5.ui.theme.Lab5Theme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//adapted from in class code
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val gravityFlow : Flow<FloatArray> = getGravityData(gravitySensor, sensorManager)
        setContent {
            Lab5Theme {
                    Surface(
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            var x by remember{ mutableStateOf(0.0f) }
                            var y by remember{ mutableStateOf(0.0f) }
                            
                            LaunchedEffect(key1 = gravityFlow) {
                                gravityFlow.collect {gravityReading ->
                                    x = gravityReading[0]
                                    y = gravityReading[1]
                                }
                            }

                            Marble(xPos = x, yPos = y)
                        }
                    }

            }
        }
    }
}

//adapted from in class code
fun getGravityData(gravitySensor: Sensor, sensorManager: SensorManager): Flow<FloatArray> {
    return channelFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event !== null) {
                    Log.e("Sensor event!", event.values.toString())
                    var success = channel.trySend(event.values.copyOf()).isSuccess
                    Log.e("success?", success.toString())
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //If you care :shrug:
            }
        }
        sensorManager.registerListener(listener, gravitySensor, SensorManager.SENSOR_DELAY_GAME) // use game for smoother roll vs normal

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}

//adapted from in class code
@Composable
fun Marble(xPos: Float, yPos: Float, modifier: Modifier = Modifier) {

    var xOffset by remember { mutableStateOf(0.0f) }
    var yOffset by remember { mutableStateOf(0.0f) }

    if (xPos > 0) {
        xOffset -= 4
    } else if (xPos < 0) {
        xOffset += 4
    }

    if (yPos > 0) {
        yOffset += 4 // add here instead of sub b/c offset coord system has y pointing down
    } else if (yPos < 0) {
        yOffset -= 4
    }

    //hold the internal box
    Box(modifier = modifier.fillMaxSize()) {
        //this is the blue box
        Box(modifier = modifier
            //what we're animating
            .fillMaxSize(fraction = 1.0f)
            .align(Alignment.Center)
            //make the background blue, we could animate the color if we wanted
            .drawBehind {
                drawRect(Color.Cyan)
            }
        ) {
            //box with constraints lets us access the maxWidth of our parent which is helpful here
            BoxWithConstraints {
                // deal with edge cases of offsets to keep marble in screen of 412x915dp screen
                if (xOffset < 0) {
                    xOffset = 0.0f
                }
                if (xOffset > (maxWidth.value - 80.0f)) {
                    xOffset = maxWidth.value - 80.0f
                }
                if (yOffset < 0) {
                    yOffset = 0.0f
                }
                if (yOffset > (maxHeight.value - 80.0f)) {
                    yOffset = maxHeight.value - 80.0f
                }
                Box(
                    modifier = modifier
                        .offset( //place it
                            xOffset.dp,
                            yOffset.dp
                        )
                        .size(80.dp)
                        .clip(CircleShape) //only draw in a circle
                        .drawBehind {
                            drawRect(Color.Red)
                        },
                    contentAlignment = Alignment.Center //put the text in the middle of the circle
                ) {
                }
            }

        }
    }
}