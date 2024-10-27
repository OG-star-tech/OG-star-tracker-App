package og.ogstartracker.utils

import com.squareup.moshi.JsonDataException
import og.ogstartracker.network.ErrorIdentificationImpl
import og.ogstartracker.network.Resource
import og.ogstartracker.network.ResourceUtils
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <E : Any> tryOnline(
	doOnSuccess: suspend ((E?) -> Unit) = {},
	request: suspend () -> Response<E>
): Resource<E> {
	return try {
		val response = request()
		if (response.isSuccessful) {
			doOnSuccess(response.body())
			ResourceUtils.success(response.body())
		} else {
			//don't handle any errors
			ResourceUtils.error(ErrorIdentificationImpl.ConnectionProblem)
		}
	} catch (e: HttpException) {
		Timber.e(e)
		ResourceUtils.error(code = e.code())
	} catch (e: SocketTimeoutException) {
		Timber.e(e)
		ResourceUtils.error(ErrorIdentificationImpl.ConnectionProblem)
	} catch (e: UnknownHostException) {
		Timber.e(e)
		ResourceUtils.error(ErrorIdentificationImpl.ConnectionProblem)
	} catch (e: IOException) {
		Timber.e(e)
		if (e is ProtocolException) {
			ResourceUtils.error(ErrorIdentificationImpl.NoContent)
		} else {
			ResourceUtils.error(ErrorIdentificationImpl.Unknown)
		}
	} catch (e: JsonDataException) {
		Timber.e(e)
		ResourceUtils.error(ErrorIdentificationImpl.Unknown)
	}
}

fun <T : Any> parseResponseToResource(response: Response<T>): Resource<T> {
	return if (response.isSuccessful) {
		ResourceUtils.success(response.body())
	} else {
		ResourceUtils.error(code = response.code())
	}
}
