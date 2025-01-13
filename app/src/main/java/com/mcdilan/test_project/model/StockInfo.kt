package com.mcdilan.test_project.model

import com.google.gson.annotations.SerializedName

data class StockInfo(
    @SerializedName("c") val c: String?,
    @SerializedName("base_contract_code") val baseContractCode: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("ltr") val ltr: String?,
    @SerializedName("pcp") val pcp: Double,
    @SerializedName("ltp") val ltp: Double,
    @SerializedName("chg") val chg: Double,
    @SerializedName("min_step") val minStep: Double,
)

fun StockInfo.getDisplayLtrAndName(): String {
    return when {
        ltr== null && name != null->{ name }
        name == null&& ltr!= null->{ltr}
        ltr== null && name == null->""
        else ->"$ltr | $name"
    }
}