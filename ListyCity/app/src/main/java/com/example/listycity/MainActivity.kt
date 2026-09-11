package com.example.listycity

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



class CityRepository {
    //keep mutable app data private so other classes cannot change it directly
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin",
        "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )

    //Get a read-only list for the UI to display
    val cities: List<String>
        get() = _cities

    //add city to the list
    fun addCity(city: String){
        _cities.add(city)
    }

    //delete city from the list
    fun deleteCity(city: String){
        _cities.remove(city)
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
){
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)){
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name")},
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            //button to add a city
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }
            Spacer(modifier = Modifier.width(8.dp))

            //button to delete a city
            Button(
                onClick = {
                    if(selectedCity.isNotBlank()) {
                        onDeleteCity(selectedCity)
                        selectedCity = ""
                    }
                },
                enabled = selectedCity.isNotBlank() //make the delete button available only if a city is selected/clicked
            ) {
                Text("Delete City")
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(city = city,
                    isSelected = selectedCity == city,
                    onClick = {
                        selectedCity = if (selectedCity == city) "" else city
                    })

            }
        }
    }
}


@Composable
fun CityRow(city: String, isSelected: Boolean = false, onClick: () -> Unit){
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clickable(onClick = onClick)
            .background(
                if(isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface

            )

    )
}

@Preview
@Composable
fun CityListScreenPreview(){
    ListyCityTheme {
        CityListScreen(
            cities = listOf("Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin",
                "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"),
            onAddCity = {},
            onDeleteCity = {}
        )
    }

}

