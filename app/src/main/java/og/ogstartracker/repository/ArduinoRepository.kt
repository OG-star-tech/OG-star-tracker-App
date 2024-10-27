package og.ogstartracker.repository

import kotlinx.coroutines.flow.StateFlow
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.models.TrackingMode
import og.ogstartracker.network.Resource

interface ArduinoRepository {

	/**
	 * Last message is cached so that message is not shown multiple times.
	 */
	val lastArduinoMessage: StateFlow<String?>

	/**
	 * Starts the sidereal tracking in the specified direction.
	 *
	 * This function sends a command to the Arduino to start sidereal tracking. Sidereal tracking is used in astronomy to
	 * compensate for the Earth's rotation. The Arduino controls a motor that moves the telescope at the same speed as the
	 * Earth's rotation, but in the opposite direction. This keeps the stars in the same position in the telescope's field
	 * of view for a longer period of time.
	 *
	 * @param direction The direction of the hemisphere. This can be either northern or southern hemisphere.
	 * @param trackingMode The mode of tracking. This can be sidereal, lunar, or solar tracking mode.
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun startSideRealTracking(
		direction: Hemisphere,
		trackingMode: TrackingMode,
	): Resource<String>

	/**
	 * Stops the sidereal tracking.
	 *
	 * This function sends a command to the Arduino to stop sidereal tracking.
	 *
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun stopSideRealTracking(): Resource<String>

	/**
	 * Rotates the telescope to the left by a specified number of steps.
	 *
	 * This function sends a command to the Arduino to rotate the telescope to the left by a certain number of steps. The stepCount
	 * parameter controls the amount of rotation. A higher value will result in a larger rotation.
	 *
	 * @param stepCount The number of steps to rotate. This can be any integer value.
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun turnLeft(stepCount: Int): Resource<String>

	/**
	 * Rotates the telescope to the right by a specified number of steps.
	 *
	 * This function sends a command to the Arduino to rotate the telescope to the right by a certain number of steps. The stepCount
	 * parameter controls the amount of rotation. A higher value will result in a larger rotation.
	 *
	 * @param stepCount The number of steps to rotate. This can be any integer value.
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun turnRight(stepCount: Int): Resource<String>

	/**
	 * Starts the capture process with the specified parameters.
	 *
	 * This function sends a command to the Arduino to start capturing images. The parameters control the characteristics of the capture process.
	 *
	 * @param exposure The exposure time for each image, in seconds.
	 * @param numExposures The number of images to capture.
	 * @param focalLength The focal length of the telescope, in millimeters.
	 * @param pixSize The size of the pixels in the camera sensor, in micrometers.
	 * @param ditherEnabled A flag indicating whether dithering is enabled. Dithering is a technique used in astrophotography to reduce noise in the final image.
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun startCapture(
		exposure: Int,
		numExposures: Int,
		focalLength: Int,
		pixSize: Int,
		ditherEnabled: Int,
	): Resource<String>

	/**
	 * Aborts the current capture process.
	 *
	 * This function sends a command to the Arduino to abort the current capture process. If there is no ongoing capture process,
	 * the function will return a success message.
	 *
	 * @return A Resource object that contains a String. If the operation is successful, the String will be a success
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun abortCapture(): Resource<String>

	/**
	 * Retrieves the current status of the Arduino.
	 *
	 * This function sends a command to the Arduino to get its current status. The status includes information about the current
	 * operation, such as whether the Arduino is currently capturing images or tracking stars.
	 *
	 * @return A Resource object that contains a String. If the operation is successful, the String will be the status
	 * message. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun getStatus(): Resource<String>

	/**
	 * Resets the last message sent by the Arduino.
	 *
	 * This function resets the last message sent by the Arduino. This is useful in situations where the last message needs to be
	 * cleared, such as after an error has been resolved.
	 *
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun resetLastMessage()

	/**
	 * Retrieves the version of the Arduino firmware.
	 *
	 * This function sends a command to the Arduino to get the version of its firmware. The version is returned as a String.
	 *
	 * @return A Resource object that contains a String. If the operation is successful, the String will be the version
	 * number. If the operation fails, the String will be an error message.
	 * @throws Exception If there is a problem with the communication with the Arduino, an exception will be thrown.
	 */
	suspend fun getVersion(): Resource<String>
}