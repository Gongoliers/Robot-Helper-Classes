package org.usfirst.frc.team5112;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

	private Image frame;
	private int session;
	private boolean cameraStarted;

	public Camera(String cameraName) {
		session = NIVision.IMAQdxOpenCamera(cameraName, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		cameraStarted = false;
	}

	public void start() {
		NIVision.IMAQdxStartAcquisition(session);
		cameraStarted = true;
	}

	public void stop() {
		NIVision.IMAQdxStopAcquisition(session);
		cameraStarted = false;
	}

	public Image getCurrentFrame() {
		if (!cameraStarted)
			start();
		NIVision.IMAQdxGrab(session, frame, 1);
		return frame;
	}

	public void displayOnCameraServer() {
		CameraServer.getInstance().setImage(getCurrentFrame());
	}
}
