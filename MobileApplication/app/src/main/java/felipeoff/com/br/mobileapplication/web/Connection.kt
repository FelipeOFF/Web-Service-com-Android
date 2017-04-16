package felipeoff.com.br.mobileapplication.web

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by cronos on 14/04/17.
 *
 * Esta interface servira para fazermos nossas
 * requisições.
 */
interface Connection {

    /**
     * Simples e rasteiro.
     * Quando precisamos fazer uma requisição, basta
     * colocarmos o ponto de acesso da url e o metodo
     * usado.
     *
     * No caso estou usando o metodo GET, então
     * preciso da anotação GET, e passo dentro dela,
     * o caminho da url que estou pretendendo fazer a
     * requisição.
     *
     * Quando precisarmos pasar um corpo, pasamos no
     * parametro do metodo.
     * O retorno vem depois dos dois pontos, no caso
     * estou pretendendo ter um retorno JsonObject.
     *
     * A maioria de nossos requests serão feitos desta
     * maneira
     *
     * O retorno sera aquele objeto que fizemos no JavaScript
     * <pre>
     * {@code
     *  "status": 200,
     *  "msg": "Result OK!",
     *  "stackErro": null,
     *  "result": []
     * }
     * </pre>
     */
    @GET("produtos")
    fun getProducts(): Call<JsonObject>

    /**
     * Aqui faremos uma tratativa parecida com a de cima.
     * Porem iremos usar um Query no parametro do método,
     * Na Pratica esta solicitação vai ficar assim:
     *
     * http://192.168.0.11:3000/produto/?id={ID_PASSADO}
     *
     * O retorno sera aquele objeto que fizemos no JavaScript
     *
     * <pre>
     * {@code
     *  "status": 200,
     *  "msg": "Result OK!",
     *  "stackErro": null,
     *  "result": []
     * }
     * </pre>
     */
    @GET("produto")
    fun getProduct(@Query("id") id: String): Call<JsonObject>

    /**
     * Repare que oque muda aqui é o método, ele é POST,
     * Usamos um parametro diferente também, como o Body
     *
     * O nosso Body devera ficar assim:
     * <pre>
     * {@code
     *  "name": "Teste",
     *  "price": 23.1
     * }
     * </pre>
     *
     * O retorno permanece o mesmo.
     */
    @POST("produto")
    fun saveProduct(@Body obj: JsonObject): Call<JsonObject>

    /**
     * Exelente, temos agora uma chamada de update.
     * acho que nessa parte passa a ser intuitivo
     *
     * O nosso Body devera ficar assim:
     * {
     *  "id": "58f0fc534152e019551142ef",
     *  "name": "Teste",
     *  "price": 23.1
     * }
     *
     * O retorno permanece o mesmo.
     */
    @PUT("produto")
    fun updateProduct(@Body obj: JsonObject): Call<JsonObject>

    /**
     * Exelente, temos agora uma chamada de delete.
     *
     * O nosso Body devera ficar assim:
     * {
     *  "id": "58f0fc534152e019551142ef",
     * }
     *
     * O retorno permanece o mesmo.
     */
    @HTTP(method = "DELETE", path = "produto", hasBody = true)
    fun deleteProduct(@Body obj: JsonObject): Call<JsonObject>

}