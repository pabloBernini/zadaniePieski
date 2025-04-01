package com.example.zadaniepieski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchListApp()
        }
    }
}

@Composable
fun SearchListApp() {
    var searchText by remember { mutableStateOf("") }
    var itemList by remember { mutableStateOf(listOf<String>()) }
    var searchResult by remember { mutableStateOf<String?>(null) }
    var originalList by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Search") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                itemList = originalList
                if (searchText.isNotEmpty()) {
                    itemList = itemList + searchText
                    originalList = itemList
                    searchText = ""
                    searchResult = null
                }
            }) {
                Text("Add")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                itemList = originalList
                if (searchText.isNotEmpty()) {
                    val foundItem = itemList.find { it.equals(searchText, ignoreCase = true) }
                    if (foundItem != null) {
                        searchResult = foundItem
                        itemList = listOf(foundItem) + itemList.filter { it != foundItem }
                    } else {
                        searchResult = null
                    }
                } else {
                    searchResult = null
                }
            }) {
                Text("Find")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchResult != null) {
            Text("Found: $searchResult")
        }

        if (itemList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                itemList.forEach { item ->
                    Text(text = item, modifier = Modifier.padding(4.dp))
                    HorizontalDivider()
                }
            }
        } else {
            Text("List is empty.")
        }
    }
}