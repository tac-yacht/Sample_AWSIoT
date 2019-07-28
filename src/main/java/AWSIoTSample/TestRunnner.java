package AWSIoTSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.iot.client.AWSIotMqttClient;

@Component
public class TestRunnner implements ApplicationRunner {

	@Autowired
	private AWSCredentialsProvider credential;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("hello");

		String clientEndpoint = "a34l17bcd28eho-ats.iot.ap-northeast-1.amazonaws.com";       // replace <prefix> and <region> with your own
		String clientId = "clientA";                              // replace with your own client ID. Use unique client IDs for concurrent connections.

		// AWS IAM credentials could be retrieved from AWS Cognito, STS, or other secure sources
		AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, credential.getCredentials().getAWSAccessKeyId(), credential.getCredentials().getAWSSecretKey());

		// optional parameters can be set before connect()
		String thingName = "bulb";                    // replace with your AWS IoT Thing name

		MyDevice device = new MyDevice(thingName);
		device.setReportInterval(1000);
		client.attach(device);

		client.connect();

		// Update shadow document
//		String update = "{\"state\":{\"reported\":{\"power\":\"off\"}}}";
//		device.update(update);

//		device.setPower(false);
//		Thread.sleep(3000);
//		device.setPower(true);
//		Thread.sleep(3000);
//		device.setPower(false);
//		Thread.sleep(3000);
//
		// Get the entire shadow document
//		String state = device.get();
//		System.out.println(state);
//
		client.detach(device);
		client.disconnect();
	}

}
