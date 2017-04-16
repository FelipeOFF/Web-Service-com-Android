package felipeoff.com.br.mobileapplication.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import felipeoff.com.br.mobileapplication.R
import felipeoff.com.br.mobileapplication.adapter.ProductsAdapter
import felipeoff.com.br.mobileapplication.model.Product
import felipeoff.com.br.mobileapplication.web.WebConnection
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by Felipe on 09/04/17.
 *
 * Olá a todos que estejam começando a fazer aplicações android
 * e estão intusiasmado como eu a usar o Kotlin em suas aplicações
 *
 * Bom para começar vamos preciar definir o nosso projeto, para
 * aceitar o kotlin alén de adicionar as depedencias que iremos preciar.
 * Estou usando o Android Studio para fazer esta aplicação, e se você
 * estiver também, você pode adicionar um plugin chamado Kotlin
 * com este plugin é possivel converter classes Java para Kotlin,
 * além de outras vantagens, tais como configuração do projeto para
 * aceitar Kotlin.
 *
 * De uma plhada no arquivo build.gradle localizado em /build.gradle.
 * Logo após vamos dar uma olhada nas depedencias localizadas em
 * /app/build.gradle
 *
 * Esta vai ser nossa tela principal, nela iram conter uma lista de
 * produtos, quando o usuario precionar e manter ele precionado
 * apareserão duas opções excluir ou editar. Simples.
 *
 * Bom antes de começamos a fazer esta tela, vamos criar nossa
 * comunicação com o servidor.
 *
 * Vamos dar uma olhada no arquivo
 * @see felipeoff.com.br.mobileapplication.web.WebConnection
 *
 * Logo após esta checagen do WebConnection. Vamos fazer nosso layout.
 * de uma olhada no arquivo main_activity
 *
 * Logo após isso, vamos dar uma olhada em:
 *
 */
class MainActivity : AppCompatActivity() {

    val conn: WebConnection = WebConnection.getInstances()
    val REQUEST_CODE: Int = 100

    var adapter: ProductsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        adapter = ProductsAdapter({ product: Product, view: View, position: Int ->

        }, { view: View, product: Product, position: Int ->
            onExcludeItem(product)
        }, { view: View, product: Product, position: Int ->
            val data: Bundle = Bundle()
            val intent: Intent = Intent(this, EditProduct::class.java)
            data.putString("mode", EditProduct.EDIT_MODE)
            data.putParcelable("product", product)
            intent.putExtras(data)
            startActivityForResult(intent, REQUEST_CODE)
        })

        listProducts.adapter = adapter
        listProducts.layoutManager = LinearLayoutManager(this)

        onLoadList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.addProduct -> {
                val intent: Intent = Intent(this, EditProduct::class.java)
                intent.putExtra("mode", EditProduct.ADD_MODE)
                startActivityForResult(intent, REQUEST_CODE)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            adapter?.clearProducts()
            onLoadList()
        }
    }

    fun onLoadList(){
        onProgressLoad(true)
        conn.getProducts({
            onProgressLoad(false)
            if(it.isEmpty()){
                changeVisibility(false)
            }else{
                changeVisibility(true)
                adapter?.addProducts(it)
            }
        }, {
            onProgressLoad(false)
            changeVisibility(false)
            AlertDialog.Builder(this).setTitle("Erro")
                    .setMessage("Obtivemos um erro ao " +
                            "retornar a lista do server, " +
                            "mais detalhes:\n ${it.message} code ${it.code}")
                    .setPositiveButton("OK", {d: DialogInterface, i: Int ->

                    })
                    .show()
        })
    }

    fun onExcludeItem(product: Product){
        onProgressLoad(true)
        adapter?.clearProducts()
        product.id?.let {
            conn.deleteProduct(it, {
                onLoadList()
            }, {
                onProgressLoad(false)
                changeVisibility(false)
                AlertDialog.Builder(this).setTitle("Erro")
                        .setMessage("Obtivemos um erro ao " +
                                "retornar a lista do server, " +
                                "mais detalhes:\n ${it.message} code ${it.code}")
                        .setPositiveButton("OK", {d: DialogInterface, i: Int ->

                        })
                        .show()
            })
        }
    }

    fun changeVisibility(visibility: Boolean){
        if(!visibility){
            listProducts.visibility = View.GONE
            txtMsgEmpty.visibility = View.VISIBLE
        }else{
            listProducts.visibility = View.VISIBLE
            txtMsgEmpty.visibility = View.GONE
        }
    }

    fun onProgressLoad(visibility: Boolean){
        if(visibility){
            progress.visibility = View.VISIBLE
        }else{
            progress.visibility = View.GONE
        }
    }
}