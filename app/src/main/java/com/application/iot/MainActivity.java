package com.application.iot;
import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private MqttAndroidClient mqttAndroidClient;
    private static final String TOPIC_TEMPERATURE = "TEMP";
    private static final String TOPIC_HUMIDITY = "HUMI";
    private static final String TOPIC_GAS = "GAS";

    TextView tvTemp,tvHumidity, tvGas, tvLedOn, tvLedOff;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serverUri = "tcp://35.222.45.221:1883";
        String clientId = "SMARTPHONE";


        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        connectToMQTTBroker();
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                Log.d("MQTT", "Received message on topic: " + topic + " , " + payload);
                if (topic.equals("TEMP")) {
                    // xử lý dữ liệu nhiệt độ
                    tvTemp = findViewById(R.id.temperature_value_text_view);
                    tvTemp.setText(payload);
                } else if (topic.equals("HUMI")) {
                    // xử lý dữ liệu độ ẩm
                    tvHumidity = findViewById(R.id.humidity_value_text_view);
                    tvHumidity.setText(payload);
                } else if(topic.equals(TOPIC_GAS)){
                    tvGas = findViewById(R.id.gas_value_text_view);
                    tvGas.setText(payload);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }

    public void connectToMQTTBroker(){
        try {
            String username = "thanhduy";
            String password = "thanhduy";
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(password.toCharArray());
            IMqttToken token = mqttAndroidClient.connect(mqttConnectOptions);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connection success
                    Log.d("MQTT", "Connection Success");
                    subscribeToTopic(TOPIC_TEMPERATURE);
                    subscribeToTopic(TOPIC_HUMIDITY);
                    subscribeToTopic(TOPIC_GAS);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Connection failed
                    Log.d("MQTT", "Connection Failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(String topic){
//        String topic = "SENSORS";
        int qos = 1;
        try {
            IMqttToken subToken = mqttAndroidClient.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d("MQTT", "Subscribed to topic: " + topic + ", qos: " + qos);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("MQTT", "Failed to subscribe.");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String payload, int qos){
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);

            mqttAndroidClient.publish(topic, message);
            Log.d("MQTT", "Topic: " + topic + ", Message: " + message);

            tvLedOn = findViewById(R.id.led_on_text_view);
            tvLedOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    publishMessage("DEVICE", "RELAY:ON", 1);
                    Log.d("MQTT", "PUBLISH LED SUCCESSFUL");

                }
            });
            tvLedOff = findViewById(R.id.led_off_text_view);
            tvLedOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    publishMessage("DEVICE", "RELAY:OFF", 1);
                    Log.d("MQTT", "PUBLISH LED SUCCESSFUL");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
