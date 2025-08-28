# 日语单词消消乐（JLPT N5→N1）

> 一个 **单词⇄意思匹配即消失** 的 Android 小游戏。基于 **Kotlin + Jetpack Compose**，单 Activity、单文件最小可运行。支持关卡（N5→N1）、计时、得分、连击、重新开始。可内置少量词库，也可后续接入 CSV/Room。

---

## 0. 如何快速跑起来（最省事）

1. **Android Studio** 新建 _Empty Views Activity (Jetpack Compose)_ 工程（任意包名）。
    
2. 打开 `app/src/main/java/<你的包名>/MainActivity.kt`，**将其全部替换**为本文的 `MainActivity.kt` 内容。
    
3. 在 `app/src/main/assets/` 下（若没有就新建）可选放入 `n5.csv`、`n4.csv` ...（格式见文末），首次先不用，直接跑内置词库即可。
    
4. 运行 App；选择关卡（N5/N4...），点击开始；点击卡片配对“日文⇄中文释义”，配对成功即消失；清空过关。
    

> 说明：本文提供 **单文件版本** 便于复制粘贴；后续可按你的代码风格拆包、引入 Hilt/Room、导入大词库等。

---

## 1) MainActivity.kt（完整可运行）

> 复制到你的 `MainActivity.kt` 覆盖即可。

```kotlin
package com.example.jlptmatch

import android.os.CountDownTimer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { JLPTMatchApp() }
    }
}

// --------------------------- 数据模型 ---------------------------

enum class JLPTLevel(val label: String) { N5("N5"), N4("N4"), N3("N3"), N2("N2"), N1("N1") }

data class WordPair(
    val id: Int,
    val jp: String,
    val cn: String,
    val level: JLPTLevel
)

sealed interface TileType { object Word: TileType; object Meaning: TileType }

data class Tile(
    val pairId: Int,
    val text: String,
    val type: TileType,
    var revealed: Boolean = false,
    var matched: Boolean = false,
)

// --------------------------- 内置简易词库（示例） ---------------------------
// 说明：先给 N5/N4 少量词条可直接玩。后续可改为 CSV/Room 载入。
object BuiltinRepo {
    val words = buildList {
        var id = 1
        fun add(level: JLPTLevel, jp: String, cn: String) { add(WordPair(id++, jp, cn, level)) }
        // --- N5（示例 24 组）---
        add(JLPTLevel.N5, "水", "水、清水")
        add(JLPTLevel.N5, "火", "火、火焰")
        add(JLPTLevel.N5, "山", "山")
        add(JLPTLevel.N5, "川", "河流")
        add(JLPTLevel.N5, "人", "人")
        add(JLPTLevel.N5, "大きい", "大、大的")
        add(JLPTLevel.N5, "小さい", "小、小的")
        add(JLPTLevel.N5, "高い", "高的；贵的")
        add(JLPTLevel.N5, "安い", "便宜的")
        add(JLPTLevel.N5, "早い", "早的；快的")
        add(JLPTLevel.N5, "遅い", "晚的；慢的")
        add(JLPTLevel.N5, "新しい", "新的")
        add(JLPTLevel.N5, "古い", "旧的")
        add(JLPTLevel.N5, "青い", "蓝色的")
        add(JLPTLevel.N5, "赤い", "红色的")
        add(JLPTLevel.N5, "白い", "白色的")
        add(JLPTLevel.N5, "黒い", "黑色的")
        add(JLPTLevel.N5, "本", "书")
        add(JLPTLevel.N5, "猫", "猫")
        add(JLPTLevel.N5, "犬", "狗")
        add(JLPTLevel.N5, "学校", "学校")
        add(JLPTLevel.N5, "先生", "老师；先生")
        add(JLPTLevel.N5, "学生", "学生")
        add(JLPTLevel.N5, "友達", "朋友")
        // --- N4（示例 16 组）---
        add(JLPTLevel.N4, "必要", "必要；必须")
        add(JLPTLevel.N4, "経験", "经验")
        add(JLPTLevel.N4, "連絡", "联系")
        add(JLPTLevel.N4, "準備", "准备")
        add(JLPTLevel.N4, "約束", "约定、约会")
        add(JLPTLevel.N4, "運転", "驾驶、开车")
        add(JLPTLevel.N4, "案内", "向导、引导")
        add(JLPTLevel.N4, "故障", "故障")
        add(JLPTLevel.N4, "遠慮", "客气、推辞")
        add(JLPTLevel.N4, "相談", "商量、商谈")
        add(JLPTLevel.N4, "復習", "复习")
        add(JLPTLevel.N4, "発表", "发表、发布")
        add(JLPTLevel.N4, "経験する", "经历、体验")
        add(JLPTLevel.N4, "世話", "照顾、照料")
        add(JLPTLevel.N4, "支度", "准备、打点")
        add(JLPTLevel.N4, "見学", "参观学习")
        // 你可以继续补 N3/N2/N1
    }
}

// --------------------------- 游戏逻辑 ---------------------------

@Composable
fun JLPTMatchApp() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        var screen by remember { mutableStateOf<ScreenState>(ScreenState.Home) }
        Surface(Modifier.fillMaxSize()) {
            when (val s = screen) {
                is ScreenState.Home -> HomeScreen(onStart = { screen = ScreenState.Game(level = it) })
                is ScreenState.Game -> GameScreen(level = s.level) { screen = ScreenState.Home }
            }
        }
    }
}

sealed interface ScreenState {
    data class Game(val level: JLPTLevel): ScreenState
    object Home: ScreenState
}

@Composable
fun HomeScreen(onStart: (JLPTLevel) -> Unit) {
    var selected by remember { mutableStateOf(JLPTLevel.N5) }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("日语单词消消乐", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("匹配『日文⇄中文释义』即可消除。清空过关。", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JLPTLevel.values().forEach { lv ->
                FilterChip(
                    selected = selected == lv,
                    onClick = { selected = lv },
                    label = { Text(lv.label) }
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = { onStart(selected) }) { Text("开始 ${selected.label}") }
    }
}

// 每关参数可调：时间、网格列数、词条数等
private data class LevelConfig(val pairs: Int, val columns: Int, val seconds: Int)

private fun levelConfig(level: JLPTLevel): LevelConfig = when(level) {
    JLPTLevel.N5 -> LevelConfig(pairs = 8, columns = 4, seconds = 90)
    JLPTLevel.N4 -> LevelConfig(pairs = 10, columns = 4, seconds = 90)
    JLPTLevel.N3 -> LevelConfig(pairs = 12, columns = 4, seconds = 100)
    JLPTLevel.N2 -> LevelConfig(pairs = 14, columns = 4, seconds = 110)
    JLPTLevel.N1 -> LevelConfig(pairs = 16, columns = 4, seconds = 120)
}

@Composable
fun GameScreen(level: JLPTLevel, onExit: () -> Unit) {
    val config = remember(level) { levelConfig(level) }

    // 关卡词库抽样
    val pool = remember(level) { BuiltinRepo.words.filter { it.level == level } }
    val chosen = remember(level) {
        if (pool.size < config.pairs) pool else pool.shuffled().take(config.pairs)
    }
    var tiles by remember(level) { mutableStateOf(buildTiles(chosen)) }

    // 选中状态
    var firstIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    // 计时
    var timeLeft by remember(level) { mutableIntStateOf(config.seconds) }
    var timer: CountDownTimer? by remember { mutableStateOf<CountDownTimer?>(null) }

    LaunchedEffect(level) {
        timer?.cancel()
        timer = object: CountDownTimer(config.seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) { timeLeft = (millisUntilFinished / 1000).toInt() }
            override fun onFinish() { timeLeft = 0 }
        }.start()
    }

    DisposableEffect(Unit) { onDispose { timer?.cancel() } }

    // 全部匹配完成
    LaunchedEffect(tiles) {
        if (tiles.all { it.matched }) finished = true
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        // 顶栏
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("关卡：${level.label}", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text("时间：${timeLeft}s", fontSize = 18.sp)
            Text("得分：$score", fontSize = 18.sp)
        }
        Spacer(Modifier.height(8.dp))

        // 网格
        LazyVerticalGrid(columns = GridCells.Fixed(config.columns), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(tiles.size) { index ->
                val t = tiles[index]
                FlipTile(
                    text = t.text,
                    revealed = t.revealed || t.matched,
                    matched = t.matched,
                    onClick = {
                        if (finished || timeLeft <= 0) return@FlipTile
                        if (t.matched || t.revealed) return@FlipTile
                        // 翻开逻辑
                        val new = tiles.toMutableList()
                        new[index] = new[index].copy(revealed = true)
                        tiles = new

                        if (firstIndex == null) {
                            firstIndex = index
                        } else {
                            val fi = firstIndex!!
                            if (fi == index) return@FlipTile
                            val a = tiles[fi]
                            val b = tiles[index]
                            val isMatch = a.pairId == b.pairId && a.type != b.type
                            if (isMatch) {
                                // 匹配成功
                                new[fi] = new[fi].copy(matched = true)
                                new[index] = new[index].copy(matched = true)
                                tiles = new
                                combo += 1
                                val bonus = 100 * combo
                                score += bonus
                            } else {
                                // 匹配失败：短暂展示后盖回
                                combo = 0
                                score = (score - 50).coerceAtLeast(0)
                                // 延迟盖回
                                LaunchedEffect("flipback-$fi-$index-${Random.nextInt()}") {
                                    kotlinx.coroutines.delay(550)
                                    val n2 = tiles.toMutableList()
                                    if (!n2[fi].matched) n2[fi] = n2[fi].copy(revealed = false)
                                    if (!n2[index].matched) n2[index] = n2[index].copy(revealed = false)
                                    tiles = n2
                                }
                            }
                            firstIndex = null
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 底部操作栏
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onExit) { Text("返回") }
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(visible = combo > 1) {
                    Text("连击 x$combo ！", color = Color(0xFF00897B), fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 12.dp))
                }
                Button(onClick = {
                    // 重新开始本关
                    firstIndex = null
                    combo = 0
                    score = 0
                    timeLeft = levelConfig(level).seconds
                    tiles = buildTiles(chosen)
                    timer?.cancel()
                    timer = object: CountDownTimer(levelConfig(level).seconds * 1000L, 1000L) {
                        override fun onTick(millisUntilFinished: Long) { timeLeft = (millisUntilFinished / 1000).toInt() }
                        override fun onFinish() { timeLeft = 0 }
                    }.start()
                    finished = false
                }) { Text("重新开始") }
            }
        }

        // 结算
        if (finished || timeLeft == 0) {
            ResultBar(success = finished && timeLeft > 0, score = score) {
                // 下一次开始同关重置
                firstIndex = null
                combo = 0
                score = 0
                timeLeft = levelConfig(level).seconds
                tiles = buildTiles(chosen)
                finished = false
                timer?.cancel()
                timer = object: CountDownTimer(levelConfig(level).seconds * 1000L, 1000L) {
                    override fun onTick(millisUntilFinished: Long) { timeLeft = (millisUntilFinished / 1000).toInt() }
                    override fun onFinish() { timeLeft = 0 }
                }.start()
            }
        }
    }
}

private fun buildTiles(pairs: List<WordPair>): List<Tile> {
    val tiles = mutableListOf<Tile>()
    for (p in pairs) {
        tiles += Tile(pairId = p.id, text = p.jp, type = TileType.Word)
        tiles += Tile(pairId = p.id, text = p.cn, type = TileType.Meaning)
    }
    return tiles.shuffled()
}

@Composable
private fun ResultBar(success: Boolean, score: Int, onPlayAgain: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        color = if (success) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
        tonalElevation = 1.dp
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (success) "通关成功！" else "时间到～", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("得分：$score", fontSize = 16.sp)
            Button(onClick = onPlayAgain) { Text("再来一局") }
        }
    }
}

// 简易“翻牌”样式
@Composable
private fun FlipTile(
    text: String,
    revealed: Boolean,
    matched: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (revealed) 0f else 180f, label = "rot")

    val bg = when {
        matched -> Color(0xFFB2DFDB)       // 命中：青绿
        revealed -> Color(0xFFFFF9C4)      // 翻开：浅黄
        else -> Color(0xFFE0E0E0)          // 盖住：灰
    }

    Box(
        modifier = Modifier
            .height(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (revealed || matched) text else "?",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
```
```