package og.ogstartracker.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArduinoApi {

	@GET("on")
	suspend fun startSiderealTracking(
		@Query("direction") direction: Int,
		@Query("tspeed") tspeed: Int,
	): Response<String>

	@GET("off")
	suspend fun stopSiderealTracking(): Response<String>

	@GET("left")
	suspend fun turnLeft(
		@Query("speed") speed: Int
	): Response<String>

	@GET("right")
	suspend fun turnRight(
		@Query("speed") speed: Int
	): Response<String>

	@GET("start")
	suspend fun startCapture(
		@Query("exposure") exposure: Int,
		@Query("numExposures") numExposures: Int,
		@Query("focalLength") focalLength: Int,
		@Query("pixSize") pixSize: Int,
		@Query("ditherEnabled") ditherEnabled: Int,
	): Response<String>

	@GET("abort")
	suspend fun abortCapture(): Response<String>

	@GET("status")
	suspend fun getStatus(): Response<String>

	@GET("version")
	suspend fun getVersion(): Response<String>
}