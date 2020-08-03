package com.shaun.weatherapp

class WeatherData(
    var description: String,
    var icon: String,
    var temp: String,
    var feelsLike: String,
    var pressure: String,
    var humidity: String,
    var tempMax: String,
    var tempMin: String,
    var countryCode: String,
    var cityName: String,
    var windSpeed:String,
    var windDeg:String,
    var sunriseTime:String,
    var sunsetTime:String
) {
    override fun toString(): String {
        return """ 
                
                description : $description
                icon :  $icon
                temp :  $temp
                feelslike : $feelsLike
                pressure :  $pressure
                humidity :  $humidity
                tempMax :   $tempMax
                tempMin :   $tempMin
                countryCode :   $countryCode
                city :  $cityName
        """.trimIndent()
    }
}