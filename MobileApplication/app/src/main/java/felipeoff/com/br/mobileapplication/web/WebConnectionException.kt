package felipeoff.com.br.mobileapplication.web

import com.google.gson.JsonObject
import javax.net.ssl.HttpsURLConnection

/**
 * Created by cronos on 14/04/17.
 *
 * Esta classe servira para tratar o erro.
 */
class WebConnectionException(msg: String?, corpo: JsonObject?,
                             var code: Int? = 200, th: Throwable?) : Exception() {

    override val message: String = msg as String
    override val cause: Throwable = th as Throwable

    val REQUEST_OK = HttpsURLConnection.HTTP_OK
    val REQUEST_FORBIDEN = 403
    val REQUEST_ERROR = 503
    val REQUEST_NOT_FOUND = 404

}