package com.example.weatherapp


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diegodobelo.expandingview.ExpandingList
import com.mancj.materialsearchbar.MaterialSearchBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val link =
    "https://api.openweathermap.org/data/2.5/weather?q=ranchi&units=metric&appid=28ba07144b9c93eaf00924daaeed86f2"

private const val TAG = "Main Activity"

class MainActivity : AppCompatActivity(), getRawData.OndownloadComplete,
    MaterialSearchBar.OnSearchActionListener,
    parseWeatherData.onDataParsed {
    val rawData = getRawData(this)

    private var mExpandingList: ExpandingList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        not_found.visibility= GONE
        mExpandingList = findViewById<ExpandingList>(R.id.expanding_list_main)
        val search_vieW= findViewById<MaterialSearchBar>(R.id.search_view);
        search_vieW.setOnSearchActionListener(this)
        lottie.visibility= VISIBLE

        rawData.download(link)



    }


    override fun onDownloadComplete(rawdata: String) {

        val json = parseWeatherData(this)
        json.parseData(rawdata)

    }

    override fun OnDataParsed(data: WeatherData) {
        UpdateUI(data)
        lottie.visibility= GONE

        not_found.visibility= GONE
        city_name.visibility= VISIBLE
        countryCode.visibility= VISIBLE
    }

    private fun UpdateUI(weatherData: WeatherData) {
        Log.d(TAG, "UpdateUI: $weatherData")
        city_name.text = weatherData.cityName
        countryCode.text = weatherData.countryCode
        Picasso.get().load(weatherData.icon)
            .into(icon)

        addItem(
            "Temperature",
            arrayOf(
                weatherData.temp,
                weatherData.feelsLike,
                weatherData.tempMin,
                weatherData.tempMax,
                weatherData.pressure,
                weatherData.humidity
            ),
            R.color.blue,
            R.drawable.ic_cloud
        )
        addItem(
            "Wind",
            arrayOf(weatherData.windSpeed, weatherData.windDeg),
            R.color.green,
            R.drawable.ic_wind
        )

        addItem(
            "sun",
            arrayOf(weatherData.sunriseTime,weatherData.sunsetTime),
            R.color.yellow
        ,R.drawable.ic_sun
        )

    }

    private fun addItem(s: String, data: Array<String>, colorRes: Int, iconRes: Int) {
        val item = mExpandingList!!.createNewItem(R.layout.expanding_layout)
        if (item != null) {
            item.setIndicatorColorRes(colorRes)
            item.setIndicatorIconRes(iconRes)
            (item.findViewById<View>(R.id.title) as TextView).text = s

            item.createSubItems(data.size)
            if (s == "Temperature") {

                var view = item.getSubItemView(0)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[0]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Temperature"

                view = item.getSubItemView(1)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[1]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Feels Like"

                view = item.getSubItemView(2)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[2]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Minimum"

                view = item.getSubItemView(3)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[3]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Maximum"

                view = item.getSubItemView(4)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[4]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Pressure"

                view = item.getSubItemView(5)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[5]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Humidity"

            } else if(s=="Wind") {
                var view = item.getSubItemView(0)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[0]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Speed"

                view = item.getSubItemView(1)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[1]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Degree"

            }else{

                var view = item.getSubItemView(0)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[0]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Sunrise Time"

                view = item.getSubItemView(1)
                (view.findViewById<View>(R.id.sub_title) as TextView).text = data[1]
                (view.findViewById<View>(R.id.detail) as TextView).text = "Sunset Time"

            }

        }

    }

    override fun onError(exception: Exception) {
        runOnUiThread {
            lottie.visibility= GONE
            not_found.visibility= VISIBLE
            city_name.visibility= GONE
            countryCode.visibility= GONE
        }

    }




    override fun onButtonClicked(buttonCode: Int) {

    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {

        val query=
            "https://api.openweathermap.org/data/2.5/weather?q=$text&units=metric&appid=28ba07144b9c93eaf00924daaeed86f2"
        mExpandingList?.removeAllViews()
        lottie.visibility= VISIBLE

               rawData.download(query)

      search_view.closeSearch()

    }
}