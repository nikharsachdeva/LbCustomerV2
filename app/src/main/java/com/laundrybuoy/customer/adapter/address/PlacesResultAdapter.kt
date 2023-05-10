package com.laundrybuoy.customer.adapter.address

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.laundrybuoy.customer.databinding.RowItemLocationSearchBinding


class PlacesResultAdapter(
    var mContext: Context,
    val onClick: (prediction: AutocompletePrediction,place :Place) -> Unit,
) : RecyclerView.Adapter<PlacesResultAdapter.PlacesViewHolder>(), Filterable {
    private var mResultList: ArrayList<AutocompletePrediction>? = arrayListOf()
    private val placesClient: PlacesClient = Places.createClient(mContext)
    private var sessToken: AutocompleteSessionToken? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {

        val binding =
            RowItemLocationSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
//            onClick(mResultList?.get(position)!!)

            val prediction = mResultList?.get(position)!!

            val placeFields: List<Place.Field> = listOf(Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS)
            val request =
                FetchPlaceRequest.builder(prediction.placeId, placeFields)
                    .setSessionToken(sessToken).build()
            placesClient.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
                Log.d("LocationPickActvity",
                    "AutocompleteSessionToken: " + request.sessionToken)
                val place = response.place
                onClick(prediction,place)
            }.addOnFailureListener { exception: java.lang.Exception ->
                if (exception is ApiException) {
                    Toast.makeText(mContext, "" + exception.message + "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class PlacesViewHolder(private val binding: RowItemLocationSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val res = mResultList?.get(position)
            itemView.apply {
                binding.itemLocationHeadingTv.text = res?.getPrimaryText(null)
                binding.itemLocationSubheadingTv.text = res?.getSecondaryText(null)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                mResultList = getPredictions(constraint)
                if (mResultList != null) {
                    results.values = mResultList
                    results.count = mResultList!!.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {

            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        val result: ArrayList<AutocompletePrediction> = arrayListOf()
        val token = AutocompleteSessionToken.newInstance()
        sessToken = token
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                .setCountries("IN")
//                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()


        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                result.addAll(response.autocompletePredictions)
                notifyDataSetChanged()
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    /*
                    Toast.makeText(mContext,
                        "Place not found: " + exception.statusCode,
                        Toast.LENGTH_SHORT).show()
                     */
                    Log.e("error->", "getPredictions: ", exception)
                }
            }
        return result
    }

}
