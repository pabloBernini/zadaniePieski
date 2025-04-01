package com.example.zadaniepieski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    var pinnedItems by remember { mutableStateOf(setOf<String>()) }
    var isDuplicate by remember { mutableStateOf(false) }

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
                onValueChange = {
                    searchText = it
                    isDuplicate = itemList.any { item -> item.equals(it, ignoreCase = true) }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Search or add doggo") },
                trailingIcon = {
                    Row {
                        IconButton(
                            onClick = {
                                if (searchText.isNotEmpty() && !isDuplicate) {
                                    itemList = itemList + searchText
                                    originalList = itemList
                                    searchText = ""
                                }
                            },
                            enabled = searchText.isNotEmpty() && !isDuplicate
                        ) {
                            Icon(painterResource(id = android.R.drawable.ic_input_add), contentDescription = "Add")
                        }
                        IconButton(
                            onClick = {
                                if (searchText.isNotEmpty()) {
                                    val foundItem = itemList.find { it.equals(searchText, ignoreCase = true) }
                                    if (foundItem != null) {
                                        itemList = listOf(foundItem) + itemList.filter { it != foundItem }
                                    }
                                }
                            },
                            enabled = searchText.isNotEmpty()
                        ) {
                            Icon(painterResource(id = android.R.drawable.ic_search_category_default), contentDescription = "Search")
                        }
                    }
                },
                colors = TextFieldDefaults.run {
                    colors(
                                focusedIndicatorColor = if (isDuplicate) Color.Red else Color.Unspecified,
                                unfocusedIndicatorColor = if (isDuplicate) Color.Red else Color.Unspecified
                            )
                }
            )
        }

        if (isDuplicate) {
            Text(text = "Dog with this name is already added", color = Color.Red)
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
                        IconButton(onClick = {
                            if (pinnedItems.contains(item)) {
                                pinnedItems = pinnedItems.minus(item)
                                itemList = originalList.filter { pinnedItems.contains(it) || it == item } + originalList.filter { !pinnedItems.contains(it) && it != item }
                            } else {
                                pinnedItems = pinnedItems.plus(item)
                                itemList = listOf(item) + itemList.filter { it != item }
                            }
                        }) {
                            Text(if (pinnedItems.contains(item)) "❤️‍🔥" else "❤️")
                        }
                        IconButton(onClick = {
                            itemList = itemList.filter { it != item }
                            originalList = originalList.filter { it != item }
                            pinnedItems = pinnedItems.minus(item)
                        }) {
                            Text("️🗑️")
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}