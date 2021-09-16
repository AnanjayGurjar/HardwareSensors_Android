package android.example.hardwaresensors

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Random
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorEventListener: SensorEventListener
    lateinit var sensorManager: SensorManager
    lateinit var proxSensor: Sensor
    lateinit var accelSensor: Sensor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(sensorManager == null){
            Toast.makeText(this, "Couldnot get sensors", Toast.LENGTH_SHORT).show()
            finish()
            return
        }else{
//            val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
//            sensors.forEach{
//                Log.d(Companion.HWSENS, """ ${it.name}  |  ${it.stringType}  |  ${it.vendor}""")

            proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)




        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, proxSensor,  1000*1000)
        sensorManager.registerListener(this, accelSensor, 1000*1000)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
    private fun colorForAccel(accel: Float): Int{
        return (((accel + 12)/24)*255).roundToInt()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val colors = arrayOf(Color.BLUE, Color.CYAN, Color.GRAY, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.BLACK)
        if (event != null) {
            if((event.sensor.type == Sensor.TYPE_PROXIMITY)) {
                if (event.values[0] > 0) {
                    fl_proxSensor.setBackgroundColor(colors[Random().nextInt(6)])
                }
            }

            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){

                val randomColor = Color.rgb(colorForAccel(event.values[0]),
                    colorForAccel(event.values[1]),
                    colorForAccel(event.values[2]))
                fl_acclSensor.setBackgroundColor(randomColor)

            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


}