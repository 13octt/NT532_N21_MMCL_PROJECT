package com.application.iot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String MQTT_BROKER_URI = "tcp://mqtt.eclipse.org:1883";
    private static final String MQTT_TOPIC_GAS = "sensors/gas";
    private static final String MQTT_TOPIC_RAIN = "sensors/rain";
    private static final String MQTT_TOPIC_LIGHT = "sensors/light";
    private static final String MQTT_TOPIC_TEMP = "sensors/temp";
    private static final String MQTT_TOPIC_HUMIDITY = "sensors/humidity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}