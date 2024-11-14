package com.example.phftapp

data class ActivityContents(
    val activityType: String,
    var timer: Float = 0.0f,
    var caloriesBurned: Float = 0.0f,
    var heartRate: Int = 80
) {
    fun trackCalories(user: User): Float {
        val met = when (activityType.lowercase()) {
            "running" -> 10.0f
            "walking" -> 2.5f
            "cycling" -> 4.0f
            "yoga" -> 2.0f
            "hiit" -> 10.0f
            "weightlifting" -> 5.0f
            "swimming" -> 7.0f
             else -> 1.0f
        }

        caloriesBurned = (timer * met * user.weight) / 200
        return caloriesBurned
    }
}
