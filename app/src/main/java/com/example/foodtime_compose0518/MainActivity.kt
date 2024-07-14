package com.example.foodtime_compose0518

import HolidayScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange

import androidx.lifecycle.ViewModelProvider

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.secondaryContainerLight
import com.example.foodtime_compose0518.ui.theme.surfaceContainerLowLight


import androidx.activity.viewModels
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.HolidayViewModel
import com.example.foodtime_compose0518.HolidayViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val holidayViewModel: HolidayViewModel by viewModels {
        HolidayViewModelFactory(FoodDatabase.getInstance(application).foodDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Foodtime0518_Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(holidayViewModel)
                }
            }

        }
    }
}

data class DrawerMenuItem(
    val route: String,
    val icon: ImageVector,
    val title: String
)
val routeTitleMap = mapOf(
    "ingredients" to "食材庫",
    "holidays" to "節日清單",
    "NormalList" to "常備清單",
    "Expired_food" to "過期食材",
    "logout" to "登出",
    "home_page" to "首頁",
    "addFragment" to "新增食材",
    "Addholiday" to "新增節日",
    "FoodDetail" to "食材資訊",
    "HolidayDetail" to "所需食材",
    "AddFood" to "新增食材"
)

val drawerMenuItems = listOf(
    DrawerMenuItem("ingredients", Icons.Default.Menu, "食材庫"),
    DrawerMenuItem("holidays", Icons.Default.Add, "節日清單"),
    DrawerMenuItem("NormalList", Icons.Default.DateRange, "常備清單"),
    DrawerMenuItem("Expired_food", Icons.Default.Email, "過期食材"),
    DrawerMenuItem("logout", Icons.Default.Search, "登出")
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(holidayViewModel: HolidayViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val items = drawerMenuItems
    val selectedItem = remember { mutableStateOf(items[0]) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: "ingredients"
    val currentTitle = routeTitleMap[currentRoute] ?: "食材庫"


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(20.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = item.route == currentRoute,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            selectedItem.value = item
                            navController.navigate(item.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(

                        title = { Text(currentTitle) },

                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                            }

                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("home_page") }) {
                                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = secondaryContainerLight,
                        )
                    )
                },
            ) { paddingValues ->
                NavHost(navController = navController, startDestination = "home_page", Modifier.padding(paddingValues)) {
                    composable("ingredients") { IngredientsScreen(navController) }
                    composable("holidays") { HolidayScreen(navController,holidayViewModel) }
                    composable("Addholiday") { HolidayAdd(navController, holidayViewModel) }
                    composable("HolidayDetail") { HolidayDetailScreen(navController) }
                    composable("NormalList") { Normallist(navController) }
                    composable("AddFood") { AddScreen(navController) }
                    composable("Expired_food") { ExpireScreen(navController) }
                    composable("home_page") { Home_pageScreen() }
                    composable("logout") { LoginScreen(navController) }
                    composable("addFragment") { AddFragmentScreen(navController) }
                    composable("FoodDetail") { DetailFragment(navController) }
                }
            }

        }
    )
}


@Preview
@Composable
private fun TRY() {
    Foodtime0518_Theme {
        Column() {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Button")
            }
            Text(
                text = "Hello M3 theming",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}







@Composable
fun FrequentlyUsedScreen() {
    TemplateScreen(
        title = "常備清單"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "這是常備清單頁面")
            LazyColumn {
                items(listOf("常用項目1", "常用項目2", "常用項目3")) { item ->
                    Text(text = item, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun AboutScreen() {
    TemplateScreen(
        title = "關於自己"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "這是關於自己頁面")
            Text(text = "這是一些關於自己的描述。")
        }
    }
}


@Composable
fun Home_pageScreen() {
    TemplateScreen(
        title = "首頁"

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_refrigerator),
                contentDescription = "Example Image",
                modifier = Modifier
                    .padding(20.dp)
                    .size(450.dp) // 设置固定大小为 200dp
            )
        }
    }
}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateScreen(
    title: String,

    backgroundColor: Color = surfaceContainerLowLight,

    content: @Composable () -> Unit
) {
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(title) }
//            )
//        },
        content = {

            Box(modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
                contentAlignment = Alignment.Center) {

                content()
            }
        }
    )
}


