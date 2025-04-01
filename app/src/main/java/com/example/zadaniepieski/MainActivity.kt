package com.example.zadaniepieski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
    var originalList by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
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
                        itemList = listOf(foundItem) + itemList.filter { it != foundItem }
                    }
                }
            }) {
                Text("Find")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("🐶: ${itemList.size}")

        if (itemList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                itemList.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🐶")
                        Text(text = item, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
                        IconButton(onClick = { /* tu bedzie przypiecie */ }) {
                            Text("️❤️")
                        }
                        IconButton(onClick = { /* tu bedzie delete */ }) {
                            Text("🗑️")
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}