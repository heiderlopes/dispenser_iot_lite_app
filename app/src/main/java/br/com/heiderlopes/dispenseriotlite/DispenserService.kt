package br.com.heiderlopes.dispenseriotlite

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DispenserService {

    @GET("device/{id}/level")
    fun getDispenser(@Path("id") id: String): Call<DispenserLevel>

    @POST("device/{id}/recharge")
    fun recharge(
        @Path("id") id: String,
        @Body dispenserRecharge: DispenserRecharge
    ): Call<Void>

}