package felipeoff.com.br.mobileapplication.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import felipeoff.com.br.mobileapplication.R
import felipeoff.com.br.mobileapplication.model.Product
import felipeoff.com.br.mobileapplication.utils.Constants.Companion.nf
import kotlinx.android.synthetic.main.product_adapter.view.*

/**
 * Created by cronos on 15/04/17.
 */
class ProductsAdapter(val listener: (product: Product, view: View, position: Int) -> Unit,
                      val onBtnExcluirListener: (view: View, product: Product, position: Int) -> Unit,
                      val onBtnEditListener: (view: View, product: Product, position: Int) -> Unit)
    : RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    val products: MutableList<Product> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) =
            (holder?.binding(products[position])) as Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_adapter, parent, false),
                    listener,
                    onBtnExcluirListener,
                    onBtnEditListener)

    override fun getItemCount(): Int = products.size

    class ViewHolder(itemView: View?,
                     val listener: (product: Product, view: View, position: Int) -> Unit,
                     val onBtnExcluirListener: (view: View, product: Product, position: Int) -> Unit,
                     val onBtnEditListener: (view: View, product: Product, position: Int) -> Unit) :
            RecyclerView.ViewHolder(itemView) {

        fun binding(product: Product) = with(itemView){
            itemView.setBackgroundColor(Color.WHITE)
            txtPrice.visibility = View.VISIBLE
            btnEditar.visibility = View.GONE
            btnExcluir.visibility = View.GONE

            txtProductName.text = product.name
            txtPrice.text = nf.format(product.price)

            setOnClickListener {
                listener(product, itemView, adapterPosition)
            }

            setOnLongClickListener {
                changeVisibleButtons()
                true
            }

            btnExcluir.setOnClickListener {
                changeVisibleButtons()
                onBtnExcluirListener(it, product, adapterPosition)
            }

            btnEditar.setOnClickListener {
                changeVisibleButtons()
                onBtnEditListener(it, product, adapterPosition)
            }
        }

        private fun changeVisibleButtons(): Unit = with(itemView) {
            if(txtPrice.visibility == View.VISIBLE){
                itemView.setBackgroundColor(Color.parseColor("#4f41e2"))
                txtPrice.visibility = View.GONE
                btnEditar.visibility = View.VISIBLE
                btnExcluir.visibility = View.VISIBLE
            }else{
                itemView.setBackgroundColor(Color.WHITE)
                txtPrice.visibility = View.VISIBLE
                btnEditar.visibility = View.GONE
                btnExcluir.visibility = View.GONE
            }
        }
    }

    fun addProducts(list: List<Product>){
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }

    fun clearProducts(){
        products.clear()
        notifyDataSetChanged()
    }

}