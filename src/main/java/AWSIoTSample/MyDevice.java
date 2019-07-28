package AWSIoTSample;

import java.util.Map.Entry;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.core.AbstractAwsIotClient;
import com.amazonaws.services.iot.client.shadow.AwsIotDeviceDeltaListener;

public class MyDevice extends AWSIotDevice {

	@AWSIotDeviceProperty
	private boolean power = false;

	public MyDevice(String thingName) {
		super(thingName);
	}

	@Override
	public void activate() throws AWSIotException {
        stopSync();
		super.activate();

		AbstractAwsIotClient client = getClient();
		Entry<String,AWSIotTopic> deltaTopic = client.getSubscriptions().entrySet().stream()
			.filter(e-> AwsIotDeviceDeltaListener.class.isInstance(e.getValue()))
		.findAny().get();

		client.unsubscribe(deltaTopic.getValue());
		AWSIotTopic awsIotTopic = new AwsIotDeviceDeltaListener(deltaTopic.getKey(), shadowUpdateQos, this) {
			@Override
			public void onMessage(AWSIotMessage message) {
				System.out.println("DeviceDelta:"+message.getStringPayload());
				//super.onMessage(message);
			}
		};
		client.subscribe(awsIotTopic, client.getServerAckTimeout());
        startSync();
	}

	@Override
	public void onShadowUpdate(String jsonState) {
		super.onShadowUpdate(jsonState);
		System.out.println(jsonState);
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}
}
