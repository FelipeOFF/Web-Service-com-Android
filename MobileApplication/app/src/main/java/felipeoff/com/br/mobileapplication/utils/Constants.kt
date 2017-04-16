package felipeoff.com.br.mobileapplication.utils

import java.text.NumberFormat
import java.util.*

/**
 * Created by cronos on 14/04/17.
 *
 * Aqui irá ficar algumas constantes para logo mais serem usadas.
 *
 * Este método de colocar constantes usadas constantemente na aplicação
 * é util por conta de Manutibilidade, caso futuramente precisemos alterar
 * alguma variavel.
 */
class Constants {

    companion object {
        /*
            Você deve substituir esta url pelo ip ou URL,
            da maquina que contenha seu Server
         */
        val URL: String = "http://192.168.0.11"
        val PORT: Int = 3000
        val nf: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        fun getFullURL(): String = "${URL}:${PORT}"
    }

}