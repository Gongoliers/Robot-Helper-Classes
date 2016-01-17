package org.usfirst.frc.team5112;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera {

	private Image frame;
//	private int session;
	private boolean cameraStarted;
	private USBCamera camera;

	public Camera(String cameraName) {
//		session = NIVision.IMAQdxOpenCamera(cameraName, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
//		NIVision.IMAQdxConfigureGrab(session);
		camera.openCamera();
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		cameraStarted = false;
		camera = new USBCamera(cameraName);
	}
	
	public void setBrightness(int brightness){
		camera.setBrightness(brightness);
	}
	
	public int getBrightness(){
		return camera.getBrightness();
	}
	
	public void setExposureManual(int exposure){
		camera.setExposureManual(exposure);
	}
	
	public void setExposureAuto(){
		camera.setExposureAuto();
	}
	
	public Image getImage(){
		return frame;
	}

	public void start() {
//		NIVision.IMAQdxStartAcquisition(session);
		camera.startCapture();
		cameraStarted = true;
	}

	public void stop() {
//		NIVision.IMAQdxStopAcquisition(session);
//		camera.closeCamera();
		camera.stopCapture();
		cameraStarted = false;
	}

	public Image getCurrentFrame() {
		if (!cameraStarted)
			start();
//		NIVision.IMAQdxGrab(session, frame, 1);
		camera.getImage(frame);
		return frame;
	}
	
	public void setFPS(int fps){
		camera.setFPS(fps);
	}

	public void displayOnCameraServer() {
		CameraServer.getInstance().setImage(getCurrentFrame());
	}

	public double[] convertPixelSystemToAimingSystem(int[] pixel, int resolutionX, int resolutionY) {
		double[] aimingPoint = new double[2];
		aimingPoint[0] = (pixel[0] - resolutionX / 2.0) / (resolutionX / 2.0);
		aimingPoint[1] = -(pixel[1] - resolutionY / 2.0) / (resolutionY / 2.0);
		return aimingPoint;
	}

	public double getDistanceToTarget(double targetFeet, int targetPixels, double fieldOfViewAngle,
			int fieldOfViewPixels) {
		return targetFeet * fieldOfViewPixels / (2 * targetPixels * Math.tan(Math.toDegrees(fieldOfViewAngle)));
	}

	public double getFieldOfViewAngle(double targetFeet, int targetPixels, int fieldOfViewPixels,
			double distanceToTarget) {
		return Math.toDegrees(Math.atan(targetFeet * fieldOfViewPixels / (targetPixels * distanceToTarget)));
	}
}
