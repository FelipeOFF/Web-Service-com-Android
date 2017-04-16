package felipeoff.com.br.mobileapplication.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import felipeoff.com.br.mobileapplication.R
import felipeoff.com.br.mobileapplication.model.Product
import felipeoff.com.br.mobileapplication.web.WebConnection
import kotlinx.android.synthetic.main.edit_product.*

/**
 * Created by cronos on 15/04/17.
 */
class EditProduct : AppCompatActivity() {

    var mode: String? = null

    val conn: WebConnection = WebConnection.getInstances()
    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product)

        progress = ProgressDialog(this)

        mode = intent.getStringExtra("mode")

        if(mode == EDIT_MODE){
            val product: Product = intent.getParcelableExtra<Product>("product")
            edtNome.setText(product.name)
            edtPrice.setText(product.price.toString())
        }

        btnConcluir.setOnClickListener {
            progress?.setTitle("Aguarde...")
            progress?.setMessage("Espere até que seja atualizado")
            if(mode == ADD_MODE){
                saveProduct(edtNome.text.toString(), edtPrice.text.toString())
            }else{
                val product: Product = intent.getParcelableExtra<Product>("product")
                updateProduct(product.id.toString(), edtNome.text.toString(), edtPrice.text.toString())
            }
        }

        btnCancelar.setOnClickListener {
            if(progress?.isShowing as Boolean){
                progress?.dismiss()
            }
            setResult(0)
            finish()
        }

    }

    fun updateProduct(id: String, name: String, price: String){
        conn.updateProduct(id, name, price.toDouble(), {
            if(progress?.isShowing as Boolean){
                progress?.dismiss()
            }
            setResult(Activity.RESULT_OK)
            finish()
        }, {
            if(progress?.isShowing as Boolean){
                progress?.dismiss()
            }
            AlertDialog.Builder(this).setTitle("Erro")
                    .setMessage("Obtivemos um erro ao " +
                            "retornar a lista do server, " +
                            "mais detalhes:\n ${it.message} code ${it.code}")
                    .setPositiveButton("OK", { _: DialogInterface, _: Int ->

                    })
                    .setOnDismissListener {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .show()
        })
    }

    fun saveProduct(name: String, price: String){
        conn.saveProduct(name, price.toDouble(), {
            if(progress?.isShowing as Boolean){
                progress?.dismiss()
            }
            setResult(Activity.RESULT_OK)
            finish()
        }, {
            if(progress?.isShowing as Boolean){
                progress?.dismiss()
            }
            AlertDialog.Builder(this).setTitle("Erro")
                    .setMessage("Obtivemos um erro ao " +
                            "retornar a lista do server, " +
                            "mais detalhes:\n ${it.message} code ${it.code}")
                    .setPositiveButton("OK", { _: DialogInterface, _: Int ->

                    })
                    .setOnDismissListener {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .show()
        })
    }

    companion object {
        val EDIT_MODE: String = "edit"
        val ADD_MODE: String = "add"
    }
}