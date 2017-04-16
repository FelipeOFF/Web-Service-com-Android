package felipeoff.com.br.mobileapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

import org.json.JSONException
import org.json.JSONObject

import java.lang.reflect.Type
import java.util.ArrayList

/**
 * Created by cronos on 14/04/17.
 *
 * Esta class servira como um modelo para as solicitações,
 * Web.
 */
class Product : Parcelable {
    /**
     * Aqui vai ficar o request dele
     *
     * _id : 58f0fc534152e019551142ef
     * name : Mais um teste
     * price : 12.12
     * __v : 0
     * create_at : 2017-04-14T16:44:03.170Z
     */

    @SerializedName("_id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("price")
    var price: Double = 0.toDouble()

    @SerializedName("create_at")
    var createAt: String? = null

    companion object { // Estes métodos são gerados automaticamente
        fun objectFromData(str: String): Product {

            return Gson().fromJson(str, Product::class.java)
        }

        fun objectFromData(str: String, key: String): Product? {

            try {
                val jsonObject = JSONObject(str)

                return Gson().fromJson(jsonObject.getString(str), Product::class.java)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return null
        }

        fun arrayProductFromData(str: String): List<Product> {

            val listType = object : TypeToken<ArrayList<Product>>() {

            }.type

            return Gson().fromJson<List<Product>>(str, listType)
        }

        fun arrayProductFromData(str: String, key: String): ArrayList<*> {

            try {
                val jsonObject = JSONObject(str)
                val listType = object : TypeToken<ArrayList<Product>>() {

                }.type

                return Gson().fromJson<ArrayList<*>>(jsonObject.getString(str), listType)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return ArrayList<Product>()
        }

        @JvmField val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this() {
        this.id = source.readString()
        this.name = source.readString()
        this.price = source.readValue(Double::class.java.classLoader) as Double
        this.createAt = source.readString()
    }

    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.id)
        dest?.writeString(this.name)
        dest?.writeValue(this.price)
        dest?.writeString(this.createAt)
    }
}
