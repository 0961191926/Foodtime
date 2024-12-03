package com.example.foodtime_compose0518

object ImageMapper {
    private val nameToImageMap = mapOf(
        "apple" to R.drawable.apple,
        "bell pepper" to R.drawable.bell_pepper,
        "broccoli" to R.drawable.broccoli,
        "cabbage" to R.drawable.cabbage,
        "carrot" to R.drawable.carrot,
        "egg" to R.drawable.egg,
        "eggplant" to R.drawable.eggplant,
        "fish" to R.drawable.fish,
        "meat" to R.drawable.newmeat,
        "radish" to R.drawable.radish,
        "salmon" to R.drawable.salmon,
        "sausage" to R.drawable.sausage,
        "shellfish" to R.drawable.shellfish,
        "sprout" to R.drawable.sprout,
    )

    fun getImageResourceByName(name: String): Int {
        return nameToImageMap[name.lowercase()] ?: R.drawable.background
    }
}