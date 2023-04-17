package com.application.iot;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;



public class MainActivity extends AppCompatActivity {
    String topic = "your/topic";
    String payload = "{\"temperature\":25.0,\"humidity\":50.0}";
    private MqttAndroidClient mqttAndroidClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serverUri = "tcp://35.222.45.221:1883";
        String clientId = "client";
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);

        String username = "thanhduy";
        String password = "thanhduy";

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            IMqttToken token = mqttAndroidClient.connect(mqttConnectOptions);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connection success
                    Log.d("MQTT", "Success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Connection failed
                    Log.d("MQTT", "Failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

//        try {
//            mqttAndroidClient.publish(topic, new MqttMessage(payload.getBytes()));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }






    }
}
