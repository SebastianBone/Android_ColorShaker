package com.example.colorshaker;

//Die benötigten Imports

import java.util.Random;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {

	//benötigte Objekte erstellen
	private SensorManager sensorManager;
	private boolean color = false;
	private View view;
	private long lastUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//den Titelnamen ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Vollbild einstellen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		// auf das entsprechende Layout verweisen
		setContentView(R.layout.fragment_main);
		// view auf das entsprechende Layout-Objekt verweisen
		view = findViewById(R.id.surfaceView1);
		// Startfarbe
		view.setBackgroundColor(Color.WHITE);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Sensor-Typ der ausgelesen werden soll und Aktion bewirkt
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			getAccelerometer(event);
		}
		
	}
	
	//Methode die bei Sensorwirkung benutzt wird
	private void getAccelerometer(SensorEvent event){
		
		// Array für die Accelerometer xyz-Achsen 
		float [] values = event.values;
		
		float x = values[0];
		float y = values[1];
		float z = values[2];
		
		//Attribut um Heftigkeit der Bewegung zu ermitteln
		float accelationsSquareRoot = (x * x+y * y+z * z) / 
				(SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		
		
		long actualTime = System.currentTimeMillis();
		
		if(accelationsSquareRoot >=2){
			//Mindestabstand zwischen zwei Aktionen bestimmen
			if(actualTime - lastUpdate < 200){
				return;
			}
			lastUpdate = actualTime;
			
			//als Feedback Pop-Up erzeugen 
			Toast.makeText(this, "shaking!", Toast.LENGTH_SHORT).show();
			
			// Random-Color erzeugen
			Random rand = new Random();
			 
		    int redValue = rand.nextInt(255);
		    int greenValue = rand.nextInt(255);
		    int blueValue = rand.nextInt(255);
		    
		    int nextColor = Color.rgb(redValue, greenValue, blueValue);
		    
		    if(color){
		    	view.setBackgroundColor(nextColor);
		    }
		    //else Block, damit die Farbe nicht bei nächster Bewegung auf weiß zurückspringt
		    else{
		    	view.setBackgroundColor(nextColor);
		    }
		   
			color = !color;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//zu verwendender Sensor
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}
}
