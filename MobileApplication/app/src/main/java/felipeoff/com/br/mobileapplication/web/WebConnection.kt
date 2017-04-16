package felipeoff.com.br.mobileapplication.web

import android.content.Context
import com.google.gson.JsonObject
import felipeoff.com.br.mobileapplication.BuildConfig
import felipeoff.com.br.mobileapplication.model.Product
import felipeoff.com.br.mobileapplication.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import felipeoff.com.br.mobileapplication.activities.MainActivity

/**
 * Created by Felipe on 09/04/17.
 *
 * Esta classe servira para fazermos comunicações
 * com o servidor. Basicamente vamos fazer algumas
 * funções de CRUD aqui.
 *
 * Vamos estar usando o Retrofit, para fazer a comunicação
 * caso você queira dar uma olhada com mais calma na documentação
 * do retrofit, você pode acessar:
 * @link http://square.github.io/retrofit/
 *
 * Estarei usando principalmente códigos HTTP:
 * @link https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 *
 * Siga esta orden para ver cada linha:
 * 275 - Criando esta classe, usando o design pattern Singleton
 * 49 - Criando o método construtor
 * 265 - Criando uma instancia do Retrofit
 * 88 - Método para retornar todos os produtos do WebService
 * 116 - Método para retorno de apenas um produto
 * 135 - Slava os produtos
 * 164 - Atualiza um produto
 * 185 - Deleta um produto
 * 292 - Conclusão
 */
class WebConnection private constructor() {

    // A instancia do RETROFIT que sera usada em cada metodos
    private var retrofit: Retrofit? = null

    /**
     * Aqui ira ficar a inicialização da classe,
     * não se esqueça que esta classe tera apensas uma
     * instancia, então nesta função init, não sera executada
     * mais de uma vez.
     *
     * Basicamente além de eu estar iniciando o retrofit
     * estou também criando um log, a cada requisição.
     */
    init {
        /*
            Vamos verificar se esta em modo debug, não queremos
            que fique mostrando toda a hora no console as
            requisições que fazemos.
         */
        if (BuildConfig.DEBUG) {
            // Se ele estiver em modo DEBUG, sera mostrado no LogCat, um log da requisição
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            this.retrofit = Retrofit.Builder()
                    .baseUrl(Constants.getFullURL())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        } else {
            /*
                Caso não esteja em modo DEBUG, então vamos criar
                um cliente que apenas faça simples requisições
            */
            val client = OkHttpClient.Builder().build()
            this.retrofit = Retrofit.Builder()
                    .baseUrl(Constants.getFullURL())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }

    /**
     * Este método sera responsavel por fazer request dos produtos
     * vamos ter dois callbacks aqui, ambos serão responsaveis,
     * pela resposta vinda do servidor. Isso posibilita que
     * as respostas já sejam tratadas.
     * Na maioria das vezes não precisarei colocar a mão na resposta
     * ela já esta feita no servidor, para que refazer? Isso deixa as
     * respostas dinamicas, é mais facil alterar algo no Web Service
     * do que na aplicação.
     *
     * Claro que se você ver que a resposta vinda do servidor não coindiz
     * com a ação do usuario, ou você deseja tratar melhor esta resposta.
     * você pode fazer uma tratativa diferente, como por exemplo criar,
     * códigos que significam uma resposta tecnica, e depois tratar este código
     *
     * Você poderia esta incluindo esta tratativa na classe:
     * @see WebConnectionException
     */
    fun getProducts(onSucess: (listProduct: List<Product>) -> Unit,
                    onError: (error: WebConnectionException) -> Unit) {

        // Precisamos chamar nossa interface que sera tratada, pelo retrofit.
        val call: Call<JsonObject>? = getConnection()?.getProducts()

        // Precisamos de apenas um método
        callbackList(call, onSucess, onError)
    }

    /**
     * Repare que todos os métodos, são muito parecidos, só precisamos,
     * de dois retornos diferentes em teze, São eles: retorno de uma lista,
     * e retorno de apenas um registro.
     *
     * Claro que se você tiver varias respostas diferentes você precisa,
     * tratar o retorno de maneiras diferentes.
     */
    fun getProduct(id: String, onSucess: (listProduct: Product) -> Unit,
                   onError: (error: WebConnectionException) -> Unit) {

        // Precisamos chamar nossa interface que sera tratada, pelo retrofit.
        // Vamos retornar apenas um registro aqui
        val call: Call<JsonObject>? = getConnection()?.getProduct(id)

        // Precisamos de apenas um método
        callback(call, onSucess, onError)
    }

    /**
     * Repare que todos os métodos, são muito parecidos, só precisamos,
     * de dois retornos diferentes em teze, São eles: retorno de uma lista,
     * e retorno de apenas um registro.
     *
     * Claro que se você tiver varias respostas diferentes você precisa,
     * tratar o retorno de maneiras diferentes.
     *
     * Vamos precisar de um Body neste request, este body é um JSON, que sera
     * enviado para o servidor.
     * Estou usando a Blibioteca Gson para esta fazendo esta tratativa.
     * Porem você pode usar o:
     * @see JSONObject
     */
    fun saveProduct(name: String, price: Double, onSucess: (listProduct: Product) -> Unit,
                   onError: (error: WebConnectionException) -> Unit) {

        // Aqui precisamos de uma instancia do objeto JsonObject, do GSON
        val obj = JsonObject()
        obj.addProperty("name", name)
        obj.addProperty("price", price)
        // Agora sim vamos fazer nossa instancia de chamada.
        // Repare que estou colocando o body como um passagen de parametro
        val call: Call<JsonObject>? = getConnection()?.saveProduct(obj)

        // Precisamos de apenas um método
        callback(call, onSucess, onError)
    }

    /**
     * Este sera nosso método de update, oque você tem que saber
     * aqui é que ele não é tão diferente dos demais.
     */
    fun updateProduct(id: String, name: String, price: Double, onSucess: (listProduct: Product) -> Unit,
                      onError: (error: WebConnectionException) -> Unit){

        // Aqui precisamos de uma instancia do objeto JsonObject, do GSON
        val obj = JsonObject()
        obj.addProperty("id", id)
        obj.addProperty("name", name)
        obj.addProperty("price", price)
        // Agora sim vamos fazer nossa instancia de chamada.
        // Repare que estou colocando o body como um passagen de parametro
        val call: Call<JsonObject>? = getConnection()?.updateProduct(obj)

        // Precisamos de apenas um método
        callback(call, onSucess, onError)

    }

    /**
     * Pronto com o metodo delete feito, nosso crud já esta pronto.
     */
    fun deleteProduct(id: String, onSucess: (listProduct: Product) -> Unit,
                      onError: (error: WebConnectionException) -> Unit){

        // Aqui precisamos de uma instancia do objeto JsonObject, do GSON
        val obj = JsonObject()
        obj.addProperty("id", id)
        // Agora sim vamos fazer nossa instancia de chamada.
        // Repare que estou colocando o body como um passagen de parametro
        val call: Call<JsonObject>? = getConnection()?.deleteProduct(obj)

        // Precisamos de apenas um método
        callback(call, onSucess, onError)

    }

    // Aqui estamos querendo retornar varios produtos
    private fun callbackList(call: Call<JsonObject>?, onSucess: (listProduct: List<Product>) -> Unit,
                     onError: (error: WebConnectionException) -> Unit) {

        // Vamos usar o callback do Retrofit, ele recebe um objeto Callback
        call?.enqueue(object : Callback<JsonObject> {
            // Esta função só sera chamada quando ouver um erro na chamada.
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                // Se ouver um erro, vamos mostrar para o usuario uma mensagen
                onError(WebConnectionException("Não foi possivel retornar os produtos", null, 404, t))
            }

            /**
             * Este método sera chamado se a comunicação com o servidor for concluida.
             * Isso não significa que não veio nenhum erro, apenas siginifica que a solicitação
             * com o servidor acabou, e temos que tratar a resposta
             */
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                // Vejamos se o código da resposta esta OK, se estiver então deu certo.
                if (response?.code() == HttpURLConnection.HTTP_OK) {
                    onSucess(Product.arrayProductFromData(response.body().get("result").asJsonArray.toString()))
                } else {
                    // Se o código vinher diferente de 200, então tivemos um problema, na comunicação.
                    // Vamos pegar a resposta e mandar para o usuario visualiza-la
                    onError(WebConnectionException(response?.body()?.get("msg")?.asString,
                            response?.body(), response?.code(), null))
                }
            }
        })
    }

    // Aqui estamos querendo retornar apenas um produto
    private fun callback(call: Call<JsonObject>?, onSucess: (listProduct: Product) -> Unit,
                 onError: (error: WebConnectionException) -> Unit) {

        // Vamos usar o callback do Retrofit, ele recebe um objeto Callback
        call?.enqueue(object : Callback<JsonObject> {
            // Esta função só sera chamada quando ouver um erro na chamada.
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                // Se ouver um erro, vamos mostrar para o usuario uma mensagen
                onError(WebConnectionException("Não foi possivel retornar os produtos", null, 404, t))
            }

            /**
             * Este método sera chamado se a comunicação com o servidor for concluida.
             * Isso não significa que não veio nenhum erro, apenas siginifica que a solicitação
             * com o servidor acabou, e temos que tratar a resposta
             */
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                // Vejamos se o código da resposta esta OK, se estiver então deu certo.
                if (response?.code() == HttpURLConnection.HTTP_OK) {
                    onSucess(Product.objectFromData((response.body().get("result").asJsonObject.toString())))
                } else {
                    // Se o código vinher diferente de 200, então tivemos um problema, na comunicação.
                    // Vamos pegar a resposta e mandar para o usuario visualiza-la
                    onError(WebConnectionException(response?.body()?.get("msg")?.asString,
                            response?.body(), response?.code(), null))
                }
            }
        })
    }

    /**
     * A interface Connection sera tratada pelo retrofit, para que posamos,
     * fazer nossos requests.
     *
     * Todas as chamadas ficaram nela, entõa acho bom vocês darem uma olhada:
     * @see Connection
     */
    fun getConnection(): Connection? = retrofit?.create(Connection::class.java)

    companion object {
        /*
            Vou usar o Design Patterns Singleton, servira
            para ter apenas uma instancia da mesma classe,
            em toda a aplicação
         */
        private var instance: WebConnection? = null

        // Este sera o método usado para recupera a instancia desta classe
        fun getInstances(): WebConnection {
            if (instance == null) {
                instance = WebConnection()
            }
            return instance as WebConnection
        }

    }

    /**
     * Show de Bola. Agora que fizemos nossos métodos de CRUD, para o servidor.
     * podemos nos concentrar em fazer a interface de nossa aplicação.
     *
     * Um adendo, poderiamos, se fosse o caso criar um Controller, para cada
     * request. Por que? Simples se quisermos salvar localmente apenas os produtos,
     * que o, usuario queira comprar usariamos, um outro método, que preferencialmente,
     * ficaria no Controller. Além é claro de outros critérios de manutibilidade.
     *
     * Pronto agora sim podemos ir para a:
     * @see MainActivity
     */

}