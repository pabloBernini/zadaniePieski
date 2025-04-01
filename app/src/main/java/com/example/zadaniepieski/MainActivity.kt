package com.example.zadaniepieski

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchListApp()
        }
    }
}

@Composable
fun MyImage() {
    Image(
        painter = painterResource(id = R.drawable.image1),
        contentDescription = "Dog Picture"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListApp() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doggos")},
                navigationIcon = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_preferences), contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, ProfileActivity::class.java))
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_myplaces), contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            var searchText by remember { mutableStateOf("") }
            var itemList by remember { mutableStateOf(listOf<String>()) }
            var originalList by remember { mutableStateOf(listOf<String>()) }
            var pinnedItems by remember { mutableStateOf(setOf<String>()) }
            var isDuplicate by remember { mutableStateOf(false) }

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
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = if (isDuplicate) Color.Red else Color.Unspecified,
                        unfocusedIndicatorColor = if (isDuplicate) Color.Red else Color.Unspecified
                    )
                )
            }

            if (isDuplicate) {
                Text(text = "Dog with this name is already on the list", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))


        Text("🐶: ${itemList.size}" ,
            modifier = Modifier.padding(start = 8.dp))

            if (itemList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    itemList.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                                    putExtra("dogName", item)
                                })
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                        MyImage()
                        Text(text = item,modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            fontSize = 5.em)
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
}}
class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert), contentDescription = "Return")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("User Name", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert), contentDescription = "Return")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        }
    }
}
class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailsScreen(intent.getStringExtra("dogName") ?: "")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(dogName: String) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert), contentDescription = "Return")
                    }
                },
                actions = {
                    IconButton(onClick = {
                    }) {
                        Icon(painterResource(id = android.R.drawable.ic_delete), contentDescription = "Delete")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dogName, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}