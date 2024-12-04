package com.example.foodtime_compose0518

import ExpireScreen
import Foodexpiration_SettingScreen
import HolidayScreen
import Signal_Notification
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.secondaryContainerLight
import com.example.foodtime_compose0518.ui.theme.surfaceContainerLowLight
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.layout.ContentScale
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.foodtime_compose0518.MainActivity.Companion.CHANNEL_ID
import com.example.foodtime_compose0518.worker.NotificationWorker
import com.example.foodtime_compose0518.worker.StockNotification
import com.google.firebase.database.FirebaseDatabase
import setting
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    companion object {
        const val CHANNEL_ID = "default_channel_id"
    }
    private val holidayViewModel: HolidayViewModel by viewModels {
        val database = FoodDatabase.getInstance(application)
        HolidayViewModelFactory(
            dao = database.foodDao,
            holidayDetailDao = database.holidayDetailDao // 傳遞 holidayDetailDao
        )
    }
    private val stockViewModel: StockViewModel by viewModels {
        StockViewModelFactory(FoodDatabase.getInstance(application).stockDao)
    }
    private val normalViewModel: NormalViewModel by viewModels {
        NormalViewModelFactory(FoodDatabase.getInstance(application).normalDao)
    }
    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(FoodDatabase.getInstance(application).settingDao)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)

        val database = FirebaseDatabase.getInstance()
        val request = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("notificationWorker", ExistingPeriodicWorkPolicy.KEEP, request)
        database.setPersistenceEnabled(true)
        setContent {
            Foodtime0518_Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(holidayViewModel,normalViewModel,stockViewModel,settingViewModel)
                }
            }

        }
    }
}
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Default Channelhappy",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
}

val imageMapping = mapOf(
    "絲瓜" to R.drawable.ingredients_loofah,
    "青花椒" to R.drawable.ingredinets_sichuan_peppercorn,
    "荔枝" to R.drawable.ingredients_lychee,
    "奶油" to R.drawable.ingredients_butter,
    "冬瓜" to R.drawable.ingredients_winter_melon,
    "黃油" to R.drawable.ingredients_butter,
    "扁桃" to R.drawable.ingredients_almond,
    "杏仁" to R.drawable.ingredients_almond,
    "蘋果" to R.drawable.ingredients_apple,
    "蘆筍" to R.drawable.ingredients_asparagus,
    "酪梨" to R.drawable.ingredients_avocado,
    "培根" to R.drawable.ingredients_bacon,
    "竹筍" to R.drawable.ingredients_bamboo_shoots,
    "甜椒" to R.drawable.ingredients_bell_pepper,
    "彩椒" to R.drawable.ingredients_bell_pepper,
    "青椒" to R.drawable.ingredients_green_pepper,
    "苦瓜" to R.drawable.ingredients_bitter_gourd,
    "黑森林蛋糕" to R.drawable.ingredients_black_forest,
    "黑胡椒" to R.drawable.ingredients_black_pepper,
    "藍莓" to R.drawable.ingredients_blueberry,
    "麵包" to R.drawable.ingredients_bread,
    "花椰菜" to R.drawable.ingredients_broccoli,
    "青花菜" to R.drawable.ingredients_broccoli, // 也叫綠花椰 
    "棕米" to R.drawable.ingredients_brown_rice,
    "糙米" to R.drawable.ingredients_brown_rice,
    "黑米" to R.drawable.ingredients_black_rice,
    "紅糖" to R.drawable.ingredients_brown_sugar,
    "布朗尼" to R.drawable.ingredients_brownie,
    "羽衣甘藍" to R.drawable.ingredients_loofah,
    "甘藍" to R.drawable.ingredients_cabbage,
    "高麗菜" to R.drawable.ingredients_cabbage,// 甘藍
    "結球萵苣" to R.drawable.ingredients_cabbage,//就是高麗菜
    "紅蘿蔔" to R.drawable.ingredients_carrot,
    "胡蘿蔔" to R.drawable.ingredients_carrot,
    "芹菜" to R.drawable.ingredients_celery,
    "西洋芹" to R.drawable.ingredients_celery, // 芹菜
    "穀物" to R.drawable.ingredients_cereal,
    "起司" to R.drawable.ingredients_cereal,
    "起司" to R.drawable.ingredients_cheese,
    "奶酪蛋糕" to R.drawable.ingredients_cheesecake,
    "起司蛋糕" to R.drawable.ingredients_cheesecake,
    "櫻桃" to R.drawable.ingredients_cherry,
    "辣椒" to R.drawable.ingredients_chili_pepper,
    "辣椒醬" to R.drawable.ingredients_chili_sauce,
    "巧克力岩漿蛋糕" to R.drawable.ingredients_chocolate_lava_cake,
    "巧克力" to R.drawable.ingredients_chocolate,
    "香菜" to R.drawable.ingredients_cilantro,
    "芫荽" to R.drawable.ingredients_coriander,
    "可可粉" to R.drawable.ingredients_cocoa_powder,
    "椰子" to R.drawable.ingredients_coconut,
    "咖啡" to R.drawable.ingredients_coffe_buttle,
    "咖啡罐" to R.drawable.ingredients_coffe_buttle,
    "咖啡粉" to R.drawable.ingredients_coffe_buttle,
    "咖啡豆" to R.drawable.ingredients_coffe_buttle,
    "香菜籽" to R.drawable.ingredients_coriander,
    "玉米" to R.drawable.ingredients_corn,
    "螃蟹" to R.drawable.ingredients_crab,
    "蟹肉" to R.drawable.ingredients_crab,
    "臘肉" to R.drawable.ingredients_cured_meat,
    "大福" to R.drawable.ingredients_daifuku,
    "麻糬" to R.drawable.ingredients_daifuku,
    "鴨蛋" to R.drawable.ingredients_duck_egg,
    "榴槤" to R.drawable.ingredients_durian,
    "蛋撻" to R.drawable.ingredients_egg_tart,
    "蛋塔" to R.drawable.ingredients_egg_tart,
    "雞蛋" to R.drawable.ingredients_egg,
    "茄子" to R.drawable.ingredients_eggplant,
    "魚丸" to R.drawable.ingredients_fish_ball,
    "鯖魚" to R.drawable.ingredients_fish,
    "鱸魚" to R.drawable.ingredients_fish,
    "鱸魚輪切" to R.drawable.ingredients_fish,
    "鱸魚切片" to R.drawable.ingredients_fish,
    "鮭魚" to R.drawable.ingredients_fish,
    "鯛魚" to R.drawable.ingredients_fish,
    "秋刀魚" to R.drawable.ingredients_fish,
    "鱈魚" to R.drawable.ingredients_fish,
    "吳郭魚" to R.drawable.ingredients_fish,
    "旗魚" to R.drawable.ingredients_fish,
    "沙丁魚" to R.drawable.ingredients_fish,
    "香料" to R.drawable.ingredients_flavor,
    "麵粉" to R.drawable.ingredients_flour,
    "蒜蓉" to R.drawable.ingredients_garlic_paste,
    "大蒜" to R.drawable.ingredients_garlic,
    "蒜頭" to R.drawable.ingredients_garlic,
    "蒜苗" to R.drawable.ingredients_garlic,
    "薑" to R.drawable.ingredients_ginger,
    "薑絲" to R.drawable.ingredients_ginger,
    "嫩薑" to R.drawable.ingredients_ginger,
    "老姜" to R.drawable.ingredients_ginger,
    "葡萄" to R.drawable.ingredients_grape,
    "青豆" to R.drawable.ingredients_green_bean,
    "綠豆" to R.drawable.ingredients_green_bean,
    "青蔥" to R.drawable.ingredients_green_onion,
    "蔥" to R.drawable.ingredients_green_onion,
    "番石榴" to R.drawable.ingredients_guava,
    "芭樂" to R.drawable.ingredients_guava, // 番石榴
    "蜂蜜" to R.drawable.ingredients_honey,
    "冰淇淋" to R.drawable.ingredients_ice_cream,
    "紅棗" to R.drawable.ingredients_jujube,
    "海帶" to R.drawable.ingredients_kelp,
    "番茄醬" to R.drawable.ingredients_ketchup,
    "奇異果" to R.drawable.ingredients_kiwi,
    "金桔" to R.drawable.ingredients_kumquat,
    "金橘" to R.drawable.ingredients_kumquat,
    "韭菜" to R.drawable.ingredients_leek,
    "韮菜" to R.drawable.ingredients_leek,
    "檸檬" to R.drawable.ingredients_lemon,
    "萊姆" to R.drawable.ingredients_lemon,
    "龍蝦" to R.drawable.ingredients_lobster,
    "螯蝦" to R.drawable.ingredients_lobster,
    "蓮子" to R.drawable.ingredients_lotus,
    "瑪德蓮" to R.drawable.ingredients_madeleine,
    "芒果" to R.drawable.ingredients_mango,
    "瑪格琳" to R.drawable.ingredients_margerine,
    "瑪琪琳" to R.drawable.ingredients_margerine, // 瑪格琳
    "棉花糖" to R.drawable.ingredients_marshmallow,
    "蛋黃醬" to R.drawable.ingredients_mayonnaise,
    "美乃滋" to R.drawable.ingredients_mayonnaise, // 蛋黃醬
    "肉丸" to R.drawable.ingredients_meatballs,
    "貢丸" to R.drawable.ingredients_meatballs, // 肉丸
    "丸子" to R.drawable.ingredients_meatballs,
    "哈密瓜" to R.drawable.ingredients_melon, // 蜜瓜
    "甜瓜" to R.drawable.ingredients_melon,
    "蜜瓜" to R.drawable.ingredients_melon,
    "牛奶" to R.drawable.ingredients_milk,
    "鮮奶" to R.drawable.ingredients_milk,
    "鮮乳" to R.drawable.ingredients_milk,
    "味噌" to R.drawable.ingredients_miso,
    "桑葚" to R.drawable.ingredients_mulberry,
    "桑椹" to R.drawable.ingredients_mulberry, // 桑葚
    "蘑菇" to R.drawable.ingredients_mushroom,

    "海苔" to R.drawable.ingredients_nori,
    "燕麥" to R.drawable.ingredients_oats,
    "章魚" to R.drawable.ingredients_octopus,
    "秋葵" to R.drawable.ingredients_okra,
    "橄欖油" to R.drawable.ingredients_olive_oil,
    "洋蔥" to R.drawable.ingredients_onion,
    "柳橙汁" to R.drawable.ingredients_orange_juice,
    "橙子" to R.drawable.ingredients_orange,
    "橘子" to R.drawable.ingredients_orange,
    "椪柑" to R.drawable.ingredients_orange,
    "香吉士" to R.drawable.ingredients_orange,
    "牡蠣" to R.drawable.ingredients_oyster,
    "百香果" to R.drawable.ingredients_passion_fruit,
    "意大利麵" to R.drawable.ingredients_pasta,
    "桃子" to R.drawable.ingredients_peach,
    "水蜜桃" to R.drawable.ingredients_peach,
    "花生" to R.drawable.ingredients_peanut,
    "土豆" to R.drawable.ingredients_peanut,
    "落花生" to R.drawable.ingredients_peanut,
    "豌豆" to R.drawable.ingredients_peas,
    "鳳梨" to R.drawable.ingredients_pineapple,
    "開心果" to R.drawable.ingredients_pistachio,
    "李子" to R.drawable.ingredients_plum,
    "柚子" to R.drawable.ingredients_pomelo,
    "葡萄柚" to R.drawable.ingredients_grapefruit,
    "豬油" to R.drawable.ingredients_pork_oil,
    "馬鈴薯" to R.drawable.ingredients_potato,
    "南瓜籽" to R.drawable.ingredients_pumpkin_seed,
    "南瓜" to R.drawable.ingredients_pumpkin,
    "蘿蔔" to R.drawable.ingredients_radish,
    "白蘿蔔" to R.drawable.ingredients_radish,
    "菜頭" to R.drawable.ingredients_radish,
    "覆盆子" to R.drawable.ingredients_raspberry,
    "樹梅" to R.drawable.ingredients_raspberry,
    "紅豆" to R.drawable.ingredients_red_beans,
    "排骨" to R.drawable.ingredients_ribs,
    "米" to R.drawable.ingredients_rice,
    "白米" to R.drawable.ingredients_rice,
    "米飯" to R.drawable.ingredients_rice,
    "烤火腿" to R.drawable.ingredients_roasted_ham,
    "火腿" to R.drawable.ingredients_roasted_ham,
    "冰糖" to R.drawable.ingredients_rock_sugar,
    "羅馬生菜" to R.drawable.ingredients_romainelettuce,
    "迷迭香" to R.drawable.ingredients_rosemary,
    "三文魚" to R.drawable.ingredients_salmon,
    "鮭魚" to R.drawable.ingredients_salmon,
    "鹽" to R.drawable.ingredients_salt,
    "鹽巴" to R.drawable.ingredients_salt,
    "香腸" to R.drawable.ingredients_sausage,
    "干貝" to R.drawable.ingredients_scallop,
    "海膽" to R.drawable.ingredients_sea_urchin,
    "蛤蜊" to R.drawable.ingredients_shellfish,
    "香菇" to R.drawable.ingredients_shiitake_mushroom,
    "菇" to R.drawable.ingredients_shiitake_mushroom,
    "蝦" to R.drawable.ingredients_shrimp,
    "蝦子" to R.drawable.ingredients_shrimp,
    "草蝦" to R.drawable.ingredients_shrimp,
    "明蝦" to R.drawable.ingredients_shrimp,
    "泰國蝦" to R.drawable.ingredients_shrimp,
    "豆漿" to R.drawable.ingredients_soy_milk,
    "醬油" to R.drawable.ingredients_soy_sauce,
    "菠菜" to R.drawable.ingredients_spinach,
    "芽菜" to R.drawable.ingredients_sprout,
    "豆芽菜" to R.drawable.ingredients_sprout,
    "魷魚" to R.drawable.ingredients_squid,
    "楊桃" to R.drawable.ingredients_starfruit,
    "草莓蛋糕" to R.drawable.ingredients_strawberry_cake,
    "草莓" to R.drawable.ingredients_strawberry,
    "糖" to R.drawable.ingredients_sugar,
    "砂糖" to R.drawable.ingredients_sugar,
    "地瓜" to R.drawable.ingredients_sweet_potato,
    "蕃薯" to R.drawable.ingredients_sweet_potato,
    "芝麻醬" to R.drawable.ingredients_tahini,
    "芋頭" to R.drawable.ingredients_taro,
    "茶葉" to R.drawable.ingredients_tea,
    "茶" to R.drawable.ingredients_tea,
    "提拉米蘇" to R.drawable.ingredients_tiramisu,
    "吐司" to R.drawable.ingredients_toast,
    "番茄" to R.drawable.ingredients_tomato,
    "核桃" to R.drawable.ingredients_walnut,
    "空心菜" to R.drawable.ingredients_water_spinach,
    "西瓜" to R.drawable.ingredients_watermelon,
    "大西瓜" to R.drawable.ingredients_watermelon,
    "小玉西瓜" to R.drawable.ingredients_watermelon,
    "優格" to R.drawable.ingredients_yogurt,
    "優酪乳" to R.drawable.ingredients_yogurt,
    "西葫蘆" to R.drawable.ingredients_zucchini,
    "櫛瓜" to R.drawable.ingredients_zucchini, // 西葫蘆
    "司康" to R.drawable.ingredients_scone,
    "香蕉" to R.drawable.ingredients_banana,
    "芭蕉" to R.drawable.ingredients_banana,
    "布丁" to R.drawable.ingredients_pudding,
    //11/27之後新增
    //蔬菜
    "location" to R.drawable.kaixuine,
    "地瓜葉" to R.drawable.ingredients_vegetable, // 蕃薯葉、甘薯葉
    "蕃薯葉" to R.drawable.ingredients_vegetable, // 蕃薯葉、甘薯葉
    "甘薯葉" to R.drawable.ingredients_vegetable, // 蕃薯葉、甘薯葉
    "清江菜" to R.drawable.ingredients_vegetable, // 湯匙菜
    "湯匙菜" to R.drawable.ingredients_vegetable, // 湯匙菜
    "小白菜" to R.drawable.ingredients_chinese_cabbage,
    "芥蘭" to R.drawable.ingredients_vegetable, // 芥蘭菜
    "芥蘭菜" to R.drawable.ingredients_vegetable, // 芥蘭菜
    "芥菜" to R.drawable.ingredients_vegetable,
    "小白菜" to R.drawable.ingredients_vegetable,
    "娃娃菜" to R.drawable.ingredients_vegetable,
    "油菜" to R.drawable.ingredients_vegetable, // 白菜
    "白菜" to R.drawable.ingredients_vegetable, // 白菜
    "萵苣" to R.drawable.ingredients_vegetable,
    "甜菜根" to R.drawable.ingredients_vegetable,
    "莧菜" to R.drawable.ingredients_vegetable, // 紅菜
    "紅菜" to R.drawable.ingredients_vegetable, // 紅菜
    "茼蒿" to R.drawable.ingredients_vegetable,
    "A菜" to R.drawable.ingredients_vegetable,
    "a菜" to R.drawable.ingredients_vegetable,
    "大陸妹" to R.drawable.ingredients_vegetable,
    "水蓮" to R.drawable.ingredients_vegetable,
    "雪裡蕻" to R.drawable.ingredients_vegetable,
    "玉米筍" to R.drawable.ingredients_vegetable,//玉黍蜀
    "玉黍蜀" to R.drawable.ingredients_vegetable,//
    "蓮藕" to R.drawable.ingredients_vegetable,
    "蓮子" to R.drawable.ingredients_vegetable,
    "甜玉米" to R.drawable.ingredients_vegetable,
    "水果玉米" to R.drawable.ingredients_vegetable,
    "九層塔" to R.drawable.ingredients_vegetable,
    "皎白筍" to R.drawable.ingredients_vegetable,
    "生菜" to R.drawable.ingredients_vegetable,
    "紅薯" to R.drawable.ingredients_vegetable, // 地瓜
    "甘蔗" to R.drawable.ingredients_sugar_cane,
    "松露" to R.drawable.ingredients_truffle,
    //水果們
    "無花果" to R.drawable.ingredients_fig,
    "柿子" to R.drawable.ingredients_fruits,
    "枇杷" to R.drawable.ingredients_fruits,
    "橄欖" to R.drawable.ingredients_fruits,
    "波羅蜜" to R.drawable.ingredients_fruits,
    "楊桃" to R.drawable.ingredients_fruits,
    "山竹" to R.drawable.ingredients_fruits,
    "凍梨" to R.drawable.ingredients_fruits,
    "龍眼" to R.drawable.ingredients_fruits,
    //麵類
    "細麵" to R.drawable.ingredients_noodles,
    "烏龍麵" to R.drawable.ingredients_noodles,
    "黃麵" to R.drawable.ingredients_noodles,
    "意麵" to R.drawable.ingredients_noodles,
    "冬粉" to R.drawable.ingredients_noodles,
    "米粉" to R.drawable.ingredients_noodles,
    "板條" to R.drawable.ingredients_noodles,
    "粄條" to R.drawable.ingredients_noodles,
    "拉麵" to R.drawable.ingredients_noodles,
    "雞絲麵" to R.drawable.ingredients_noodles,
    "關廟麵" to R.drawable.ingredients_noodles,
    "寬粉" to R.drawable.ingredients_noodles,
    "寬粉絲" to R.drawable.ingredients_noodles,
    //堅果們
    "腰果" to R.drawable.ingredients_nuts,
    "開心果" to R.drawable.ingredients_nuts,
    "巴西堅果" to R.drawable.ingredients_nuts,
    "夏威夷豆" to R.drawable.ingredients_nuts,
    "松子" to R.drawable.ingredients_nuts,
    "木耳" to R.drawable.ingredients_mushrooms,
    "白木耳" to R.drawable.ingredients_mushrooms,
    "黑木耳" to R.drawable.ingredients_mushrooms,
    "杏包菇" to R.drawable.ingredients_mushrooms,
    "鴻禧菇" to R.drawable.ingredients_mushrooms,
    "蘑菇" to R.drawable.ingredients_mushrooms,
    "草菇" to R.drawable.ingredients_mushrooms,
    "猴頭菇" to R.drawable.ingredients_mushrooms,
    "雪白菇" to R.drawable.ingredients_mushrooms,
    "花菇" to R.drawable.ingredients_mushrooms,
    "冬菇" to R.drawable.ingredients_mushrooms,
    "乾香菇" to R.drawable.ingredients_mushrooms,
    "雪燕" to R.drawable.ingredients_mushrooms,
    "金針菇" to R.drawable.ingredients_flammulina_enoki,
    //調味料
    "醬油" to R.drawable.ingredients_seasoning,
    "薄鹽醬油" to R.drawable.ingredients_seasoning,
    "薄塩醬油" to R.drawable.ingredients_seasoning,
    "醋" to R.drawable.ingredients_seasoning,
    "白醋" to R.drawable.ingredients_seasoning,
    "烏醋" to R.drawable.ingredients_seasoning,
    "米酒" to R.drawable.ingredients_seasoning,
    "蠔油" to R.drawable.ingredients_seasoning,
    "威士忌" to R.drawable.ingredients_seasoning,
    "味霖" to R.drawable.ingredients_seasoning,
    "玫瑰鹽" to R.drawable.ingredients_seasoning,
    "孜然粉" to R.drawable.ingredients_seasoning,
    "胡椒粉" to R.drawable.ingredients_seasoning,
    "胡椒鹽" to R.drawable.ingredients_seasoning,
    "十三香" to R.drawable.ingredients_seasoning,
    "椒鹽" to R.drawable.ingredients_seasoning,
    "咖哩粉" to R.drawable.ingredients_seasoning,
    "五香粉" to R.drawable.ingredients_seasoning,
    "紅辣椒" to R.drawable.ingredients_seasoning,
    "唐辛子" to R.drawable.ingredients_seasoning,
    "肉桂粉" to R.drawable.ingredients_seasoning,
    "肉桂" to R.drawable.ingredients_seasoning,
    "迷迭香" to R.drawable.ingredients_seasoning,
    "義大利香草" to R.drawable.ingredients_seasoning,
    "香蒜粒" to R.drawable.ingredients_seasoning,
    "紅椒" to R.drawable.ingredients_seasoning,
    "法式香草風味料" to R.drawable.ingredients_seasoning,
    "八角" to R.drawable.ingredients_seasoning,
    "花椒粉" to R.drawable.ingredients_seasoning,
    "薑黃粉" to R.drawable.ingredients_seasoning,
    "玫瑰鹽" to R.drawable.ingredients_seasoning,
    "白胡椒" to R.drawable.ingredients_seasoning,
    "百里香葉" to R.drawable.ingredients_seasoning,
    "蔥油" to R.drawable.ingredients_seasoning,
    "蒜油" to R.drawable.ingredients_seasoning,
    "花生油" to R.drawable.ingredients_seasoning,
    "植物油" to R.drawable.ingredients_seasoning,
    "葵花油" to R.drawable.ingredients_seasoning,
    "沙拉油" to R.drawable.ingredients_seasoning,
    "調和油" to R.drawable.ingredients_seasoning,
    "橄欖油" to R.drawable.ingredients_seasoning,
    "太白粉" to R.drawable.ingredients_seasoning,
    "木薯粉" to R.drawable.ingredients_seasoning,
    "樹薯粉" to R.drawable.ingredients_seasoning,
    "蓮藕粉" to R.drawable.ingredients_seasoning,
    "花生粉" to R.drawable.ingredients_seasoning,
    "玉米粉" to R.drawable.ingredients_seasoning,
    "片栗粉" to R.drawable.ingredients_seasoning,
    "雞粉" to R.drawable.ingredients_seasoning,
    "生抽" to R.drawable.ingredients_seasoning,
    "老抽" to R.drawable.ingredients_seasoning,
    "零卡糖" to R.drawable.ingredients_seasoning,
    "玉米澱粉" to R.drawable.ingredients_seasoning,
    "桂花" to R.drawable.ingredients_seasoning,
    "味精" to R.drawable.ingredients_seasoning,
    //海產
    "生魚片" to R.drawable.ingredients_sashimi,
    "油魚" to R.drawable.ingredients_seafood,
    "比目魚" to R.drawable.ingredients_seafood,
    "安康魚" to R.drawable.ingredients_seafood,
    "透抽" to R.drawable.ingredients_seafood,
    "軟絲" to R.drawable.ingredients_seafood,
    "花枝" to R.drawable.ingredients_seafood,
    "魷魚" to R.drawable.ingredients_seafood,
    "小卷" to R.drawable.ingredients_seafood,
    "秋刀魚" to R.drawable.ingredients_seafood,
    "香魚" to R.drawable.ingredients_seafood,
    "鯛魚" to R.drawable.ingredients_seafood,
    "虱目魚" to R.drawable.ingredients_seafood,
    "魚翅" to R.drawable.ingredients_seafood,
    "阿根廷魷魚" to R.drawable.ingredients_seafood,
    "鮑魚" to R.drawable.ingredients_seafood,
    "九孔" to R.drawable.ingredients_seafood,
    "淡菜" to R.drawable.ingredients_seafood,
    "干貝" to R.drawable.ingredients_seafood,
    "大草蝦" to R.drawable.ingredients_seafood,
    "鯛魚片" to R.drawable.ingredients_seafood,
    "生蠔" to R.drawable.ingredients_seafood,
    "帝王蟹" to R.drawable.ingredients_seafood,
    "大閘蟹" to R.drawable.ingredients_seafood,
    "三點蟹" to R.drawable.ingredients_seafood,
    "沙公" to R.drawable.ingredients_seafood,
    "紅蟳" to R.drawable.ingredients_seafood,
    "天使紅蝦" to R.drawable.ingredients_seafood,
    "藍鑽蝦" to R.drawable.ingredients_seafood,
    //雞肉們
    "雞肉" to R.drawable.ingredients_chicken,
    "雞肉切盤" to R.drawable.ingredients_chicken,
    "烤全雞" to R.drawable.ingredients_chicken,
    "烤雞" to R.drawable.ingredients_chicken,
    "雞屁股" to R.drawable.ingredients_chicken,
    "雞冠" to R.drawable.ingredients_chicken,
    "雞里肌" to R.drawable.ingredients_chicken,
    "雞柳" to R.drawable.ingredients_chicken,
    "雞排" to R.drawable.ingredients_chicken,
    "雞皮" to R.drawable.ingredients_chicken,
    "雞軟骨" to R.drawable.ingredients_chicken,
    "雞腳" to R.drawable.ingredients_chicken,
    "雞爪" to R.drawable.ingredients_chicken,
    "鳳爪" to R.drawable.ingredients_chicken,
    "雞腳凍" to R.drawable.ingredients_chicken,
    "雞脖子" to R.drawable.ingredients_chicken,
    "棒棒腿" to R.drawable.ingredients_chicken,
    "雞睪丸" to R.drawable.ingredients_chicken,
    "雞腿肉" to R.drawable.ingredients_chicken_leg,
    "雞腿骨" to R.drawable.ingredients_chicken_leg,
    "雞腿" to R.drawable.ingredients_chicken_leg,
    "雞胸肉" to R.drawable.ingredients_chicken_breast,
    "雞胸" to R.drawable.ingredients_chicken_breast,
    "雞翅" to R.drawable.ingredients_chicken_wings,
    "雞翅膀" to R.drawable.ingredients_chicken_wings,
    "雞胗" to R.drawable.ingredients_chicken_gizzards,
    "雞心" to R.drawable.ingredients_chicken_heart,
    //鵝肉
    "鵝肉" to R.drawable.ingredients_goose,
    "鵝肉切盤" to R.drawable.ingredients_goose,
    //鹿肉
    "鹿肉" to R.drawable.ingredients_vension,
    "鹿肉乾" to R.drawable.ingredients_vension,
    "鹿肉條" to R.drawable.ingredients_vension,
    //鴨肉
    "鴨肉" to R.drawable.ingredients_duck,
    "鴨肝" to R.drawable.ingredients_duck,
    "鴨腿" to R.drawable.ingredients_duck,
    "鴨掌" to R.drawable.ingredients_duck,
    "鴨胗" to R.drawable.ingredients_duck,
    "鴨腸" to R.drawable.ingredients_duck,
    "鴨胸" to R.drawable.ingredients_duck,
    "鴨翅" to R.drawable.ingredients_duck,
    //羊肉
    "羊肉" to R.drawable.ingredients_mutton,
    "羊肉片" to R.drawable.ingredients_mutton,
    "羊肉爐" to R.drawable.ingredients_mutton,
    "羊肋排" to R.drawable.ingredients_mutton,
    "羊腿" to R.drawable.ingredients_mutton,
    //牛肉
    "牛肉" to R.drawable.ingredients_beef,
    "牛肉片" to R.drawable.ingredients_beef,
    "牛舌" to R.drawable.ingredients_beef,
    "牛肋條" to R.drawable.ingredients_beef,
    "牛肚" to R.drawable.ingredients_beef,
    "牛排" to R.drawable.ingredients_beef,
    "牛小排" to R.drawable.ingredients_beef,
    "板腱" to R.drawable.ingredients_beef,
    "牛胸肉" to R.drawable.ingredients_beef,
    "翼板" to R.drawable.ingredients_beef,
    "肋排" to R.drawable.ingredients_beef,
    "牛小排" to R.drawable.ingredients_beef,
    "紐約克" to R.drawable.ingredients_beef,
    "腰內肉" to R.drawable.ingredients_beef,
    "牛筋" to R.drawable.ingredients_beef,
    "牛腱" to R.drawable.ingredients_beef,
    "牛腩" to R.drawable.ingredients_beef,
    //豬肉
    "豬肉" to R.drawable.ingredients_pig,
    "豬肉片" to R.drawable.ingredients_pig,
    "豬五花" to R.drawable.ingredients_pig,
    "五花" to R.drawable.ingredients_pig,
    "五花肉" to R.drawable.ingredients_pig,
    "里脊肉" to R.drawable.ingredients_pig,
    "豬里肌" to R.drawable.ingredients_pig,
    "豬肉乾" to R.drawable.ingredients_pig,
    "肉鬆" to R.drawable.ingredients_pig,
    "肩胛肉" to R.drawable.ingredients_pig,
    "梅花肉" to R.drawable.ingredients_pig,
    "胛心肉" to R.drawable.ingredients_pig,
    "前腿肉" to R.drawable.ingredients_pig,
    "小里肌" to R.drawable.ingredients_pig,
    "腹脅肉" to R.drawable.ingredients_pig,
    "蹄膀" to R.drawable.ingredients_pig,
    "豬腳" to R.drawable.ingredients_pig,
    "豬蹄" to R.drawable.ingredients_pig,
    "豬蹄筋" to R.drawable.ingredients_pig,
    "肝連肉" to R.drawable.ingredients_pig,
    "老鼠肉" to R.drawable.ingredients_pig,
    "豬血" to R.drawable.ingredients_pig,
    //魚
    "魚鬆" to R.drawable.ingredients_fish,
    //加工食品
    "百頁豆腐" to R.drawable.ingredients_process_food,
    "雞蛋豆腐" to R.drawable.ingredients_process_food,
    "豆腐" to R.drawable.ingredients_process_food,
    "嫩豆腐" to R.drawable.ingredients_process_food,
    "魚板" to R.drawable.ingredients_process_food,
    "米血" to R.drawable.ingredients_process_food,
    "米血糕" to R.drawable.ingredients_process_food,
    "豬血糕" to R.drawable.ingredients_process_food,
    "鑫鑫腸" to R.drawable.ingredients_process_food,
    "蝦滑" to R.drawable.ingredients_process_food,
    "蝦漿" to R.drawable.ingredients_process_food,
    "蝦餃" to R.drawable.ingredients_process_food,
    "魚餃" to R.drawable.ingredients_process_food,
    "燕餃" to R.drawable.ingredients_process_food,
    "腐竹" to R.drawable.ingredients_process_food,
    "響鈴卷" to R.drawable.ingredients_process_food,
    "年糕" to R.drawable.ingredients_process_food,
    "油條" to R.drawable.ingredients_process_food,
    "葛根粉" to R.drawable.ingredients_process_food,
    "鳳爪" to R.drawable.ingredients_process_food,
    "鵪鶉蛋" to R.drawable.ingredients_process_food,
    "蟹肉棒" to R.drawable.ingredients_process_food,
    "水晶餃" to R.drawable.ingredients_process_food,
    "甜不辣" to R.drawable.ingredients_process_food,
    "粉圓" to R.drawable.ingredients_process_food,
    "珍珠" to R.drawable.ingredients_process_food,
    "波霸" to R.drawable.ingredients_process_food,
    "豆豉" to R.drawable.ingredients_process_food,
    "豆棗" to R.drawable.ingredients_process_food,
    "牛蒡絲" to R.drawable.ingredients_process_food,
    "牛蒡" to R.drawable.ingredients_process_food,
    "泡菜" to R.drawable.ingredients_process_food,
    "辛奇" to R.drawable.ingredients_process_food,
    "魚包蛋" to R.drawable.ingredients_process_food,
    "湯圓" to R.drawable.ingredients_process_food,
    "鹹湯圓" to R.drawable.ingredients_process_food,
    "芋圓" to R.drawable.ingredients_process_food,
    "芝心丸" to R.drawable.ingredients_process_food,
    "芝士丸" to R.drawable.ingredients_process_food,
    "起司丸" to R.drawable.ingredients_process_food,
    "起士丸" to R.drawable.ingredients_process_food,
    "酸菜" to R.drawable.ingredients_process_food,
    "榨菜" to R.drawable.ingredients_process_food,
    //vegetable
    "山藥" to R.drawable.ingredients_vegetable,
    "枸杞" to R.drawable.ingredients_vegetable,
    "紅棗" to R.drawable.ingredients_vegetable,
    "黃耆" to R.drawable.ingredients_vegetable,
    "綠豆芽" to R.drawable.ingredients_vegetable,
    "苜蓿芽" to R.drawable.ingredients_vegetable,
    "黃豆芽" to R.drawable.ingredients_vegetable,
    "金針花" to R.drawable.ingredients_vegetable,
    "豌豆" to R.drawable.ingredients_vegetable,
    "毛豆" to R.drawable.ingredients_vegetable,
    "四季豆" to R.drawable.ingredients_vegetable,
    "紅蔥頭" to R.drawable.ingredients_vegetable,
    "青豆" to R.drawable.ingredients_vegetable
)



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
    "HolidayAddFragment" to "新增食材",
    "NormalListAddFragment" to "常備清單新增食材",
    "setting" to "設定",
    "Signal_Notification" to "燈號通知提醒",
    "Foodexpiration_setting" to "食材到期設定",
)
val drawerMenuItems = listOf(
    DrawerMenuItem("ingredients", Icons.Default.Menu, "食材庫"),
    DrawerMenuItem("holidays", Icons.Default.FavoriteBorder, "節日清單"),
    DrawerMenuItem("NormalList", Icons.AutoMirrored.Filled.List, "常備清單"),
    DrawerMenuItem("Expired_food", Icons.Default.Delete, "過期食材"),
    DrawerMenuItem("setting", Icons.AutoMirrored.Filled.ExitToApp, "設定")
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    holidayViewModel: HolidayViewModel,
    normalViewModel: NormalViewModel,
    stockViewModel: StockViewModel,
    settingViewModel: SettingViewModel
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val items = drawerMenuItems
    val selectedItem = remember { mutableStateOf(items[0]) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: "ingredients"

    // 检查 route 是否包含特定的参数
    val currentTitle = when {
        currentRoute.contains("FoodDetail/") -> "食材資訊"
        currentRoute.contains("HolidayDetail/") -> "所需食材"
        currentRoute.contains("HolidayAddFragment/") -> "新增食材"
        else -> routeTitleMap[currentRoute] ?: "食材庫"
    }



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
                    composable("ingredients") { IngredientsScreen(navController,stockViewModel) }
                    composable("holidays") { HolidayScreen(navController,holidayViewModel) }
                    composable("Addholiday") { HolidayAdd(navController, holidayViewModel) }
                    composable("HolidayDetail/{holidayId}") { backStackEntry ->
                        val holidayId = backStackEntry.arguments?.getString("holidayId")?.toIntOrNull()
                        if (holidayId != null) {
                            HolidayDetailScreen(navController, holidayId, holidayViewModel)
                        }
                    }
                    composable("NormalList") { Normallist(navController, normalViewModel) }
                    composable("HolidayAddFragment/{holidayId}") { backStackEntry ->
                        val holidayId = backStackEntry.arguments?.getString("holidayId")?.toIntOrNull()
                        if (holidayId != null) {
                            HolidayAddFragmentScreen(navController, holidayId, holidayViewModel)
                        }
                    }
                    composable("Expired_food") { ExpireScreen(navController,stockViewModel) }
                    composable("home_page") { Home_pageScreen(navController) }
                    composable("logout") { LoginScreen(navController) }
                    composable("addFragment") { AddFragmentScreen(navController, stockViewModel) }
                    composable("FoodDetail/{stockitemId}") { backStackEntry ->
                        val stockitemId = backStackEntry.arguments?.getString("stockitemId")?.toIntOrNull()
                        if (stockitemId != null) {
                            DetailFragment(navController, stockitemId = stockitemId, stockViewModel)
                        }
                    }
                    composable("NormalListAddFragment") { NormalAddFragment(navController,normalViewModel) }
                    composable("setting") { setting(navController) }
                    composable("Signal_Notification"){Signal_Notification(navController)}
                    composable("Foodexpiration_setting"){Foodexpiration_SettingScreen(navController,settingViewModel)}
                    composable("Addfoodexpiration"){Addfoodexpiration(navController,settingViewModel)}

                }
            }

        }
    )
}


@Composable
fun Home_pageScreen(navController: NavController) {
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
                painter = painterResource(id = R.drawable.new_cover_8),
                contentDescription = "Example Image",
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 占据宽度的80%，您可以根据需求调整比例
                    .height(450.dp) // 具体高度，按需求调整
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("ingredients") // 点击跳转到 ingredients 页面
            },
                contentScale = ContentScale.Fit,
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

