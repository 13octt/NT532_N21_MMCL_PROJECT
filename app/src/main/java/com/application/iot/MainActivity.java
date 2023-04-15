package com.application.iot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements MqttCallbackExtended{

    private static final String TAG = "MainActivity";
    private static final String BROKER_URL = "tcp://34.171.157.201:1883";
    private static final String CLIENT_ID = "my-android-app";
    private static final String TOPIC = "dht11/temperature";

    private TextView temp;
    private TextView humidity;
    private ProgressBar progressBar;
    private MqttClientWrapper mqttClientWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = findViewById(R.id.temperature_value_text_view);
        humidity = findViewById(R.id.humidity_value_text_view);
        progressBar = findViewById(R.id.progress_bar);

        try {
            mqttClientWrapper = new MqttClientWrapper(BROKER_URL, CLIENT_ID, TOPIC, this);
        } catch (MqttException e) {
            Log.e(TAG, "Error creating MQTT client", e);
        }

    }

    public void updateUI(final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] values = data.split(",");
                if (values.length == 2) {
                    temp.setText(values[0] + "°C");
                    humidity.setText(values[1] + "%");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        Log.e(TAG, "MQTT connect");

    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "MQTT connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        Log.d(TAG, "Received message on topic " + topic + ": " + payload);

        // TODO: Parse the message payload and update the UI
        updateUI(payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}