package com.application.iot;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientWrapper {
    private MqttClient mqttClient;
    private String topic;
    private MqttCallback mqttCallback;

    public MqttClientWrapper(String brokerUrl, String clientId, String topic, MqttCallback mqttCallback) throws MqttException {
        this.topic = topic;
        this.mqttCallback = mqttCallback;
        mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        mqttClient.setCallback(mqttCallback);
        connect();
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        mqttClient.connect(options);
        mqttClient.subscribe(topic);
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }
}
