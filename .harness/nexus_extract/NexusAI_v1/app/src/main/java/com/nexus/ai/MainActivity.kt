package com.nexus.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray

private val Black = Color(0xFF000000)
private val Surface = Color(0xFF111111)
private val Surface2 = Color(0xFF1B1B1B)
private val Line = Color(0xFF292929)
private val White = Color(0xFFF5F5F5)
private val Muted = Color(0xFF969696)
private val Accent = Color(0xFFE7E7E7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NexusTheme { NexusApp() } }
    }
}

@Composable
private fun NexusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = White, onPrimary = Black, background = Black, onBackground = White,
            surface = Surface, onSurface = White, secondary = Muted, outline = Line
        ), content = content
    )
}

private enum class Destination(val label: String, val icon: ImageVector) {
    Chat("Chat", Icons.Filled.ChatBubbleOutline),
    Research("Research", Icons.Filled.Search),
    Studio("Studio", Icons.Filled.AutoAwesome),
    Developer("Developer", Icons.Filled.Code),
    Knowledge("Knowledge", Icons.Filled.AccountTree)
}

@Composable
private fun NexusApp() {
    var destination by remember { mutableStateOf(Destination.Chat) }
    var showSettings by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Black,
        topBar = {
            Row(
                Modifier.fillMaxWidth().background(Black).padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(34.dp).background(White, RoundedCornerShape(11.dp)), Alignment.Center) {
                    Text("N", color = Black, fontSize = 21.sp, fontWeight = FontWeight.Black)
                }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    Text("NEXUS", color = White, fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = 14.sp)
                    Text("THE QUIET MIND", color = Muted, fontSize = 9.sp, letterSpacing = 2.sp)
                }
                IconButton(onClick = { showSettings = true }) {
                    Icon(Icons.Filled.Settings, "Settings", tint = Muted)
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Black, contentColor = White, tonalElevation = 0.dp) {
                Destination.values().forEach { item ->
                    NavigationBarItem(
                        selected = destination == item,
                        onClick = { destination = item },
                        icon = { Icon(item.icon, item.label) },
                        label = { Text(item.label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = White, selectedTextColor = White,
                            indicatorColor = Surface2, unselectedIconColor = Muted, unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (destination) {
                Destination.Chat -> ChatScreen()
                Destination.Research -> FeatureScreen("Research", "Find, compare and understand information.", Icons.Filled.Search,
                    listOf("Web research", "Source comparison", "Research reports"))
                Destination.Studio -> FeatureScreen("Creative Studio", "A workspace for ideas across media.", Icons.Filled.AutoAwesome,
                    listOf("Image Studio", "Video Studio", "Audio Studio", "AMV Studio", "Manga Editor", "Reel Cloner"))
                Destination.Developer -> DeveloperScreen()
                Destination.Knowledge -> KnowledgeScreen()
            }
        }
    }
    if (showSettings) SettingsDialog(onDismiss = { showSettings = false })
}

@Composable
private fun ChatScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("nexus_local", 0) }
    var input by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(runCatching {
            val arr = JSONArray(prefs.getString("messages", "[]"))
            List(arr.length()) { arr.getString(it) }
        }.getOrDefault(emptyList()))
    }
    fun persist(list: List<String>) {
        val arr = JSONArray(); list.forEach { arr.put(it) }
        prefs.edit().putString("messages", arr.toString()).apply()
    }
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))
        Text("A little more than a chatbot.", color = White, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, lineHeight = 32.sp)
        Text("Your personal intelligence workspace.", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 7.dp))
        Spacer(Modifier.height(20.dp))
        if (messages.isEmpty()) {
            Column(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(66.dp).background(Surface2, CircleShape), Alignment.Center) {
                    Icon(Icons.Filled.AutoAwesome, null, tint = White, modifier = Modifier.size(27.dp))
                }
                Spacer(Modifier.height(18.dp))
                Text("What are we building today?", color = White, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                Text("Your conversations are saved on this device.", color = Muted, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 7.dp))
                Spacer(Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Suggestion("Plan a project") { input = "Help me plan a project" }
                    Suggestion("Explore an idea") { input = "Help me explore an idea" }
                }
            }
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(messages) { msg ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(msg, color = White, modifier = Modifier.background(Surface2, RoundedCornerShape(18.dp)).padding(14.dp))
                    }
                }
                item {
                    Text("Local conversation saved. Connect an AI provider to enable generated responses.", color = Muted, fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(bottom = 14.dp).background(Surface, RoundedCornerShape(28.dp)).padding(start = 16.dp, end = 7.dp, top = 7.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Add, null, tint = Muted)
            Spacer(Modifier.width(9.dp))
            TextField(
                value = input, onValueChange = { input = it }, modifier = Modifier.weight(1f),
                placeholder = { Text("Message Nexus...", color = Muted, fontSize = 14.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = White, unfocusedTextColor = White, cursorColor = White
                ), maxLines = 4
            )
            IconButton(onClick = {
                val clean = input.trim()
                if (clean.isNotEmpty()) { messages = messages + clean; persist(messages); input = "" }
            }, modifier = Modifier.background(White, CircleShape).size(42.dp)) {
                Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = Black, modifier = Modifier.size(19.dp))
            }
        }
    }
}

@Composable
private fun Suggestion(text: String, onClick: () -> Unit) {
    Text(text, color = White, fontSize = 12.sp, modifier = Modifier.background(Surface2, RoundedCornerShape(20.dp)).clickable { onClick() }.padding(horizontal = 13.dp, vertical = 10.dp))
}

@Composable
private fun DeveloperScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Developer", color = White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
        Text("Build, inspect and improve your projects.", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp, bottom = 20.dp))
        FeatureCard("Project Workspace", "Project files, decisions and progress", Icons.Filled.Folder)
        FeatureCard("Infinite Build", "Long-running builds with checkpoints", Icons.Filled.AllInclusive, "Planned")
        FeatureCard("Terminal", "Command execution workspace", Icons.Filled.Terminal, "Planned")
        FeatureCard("Error Intelligence", "Verified fixes and error history", Icons.Filled.BugReport, "Planned")
        FeatureCard("Four-Agent Review", "Independent code and security reviews", Icons.Filled.Groups, "Planned")
        FeatureCard("GitHub", "Repositories, branches and pull requests", Icons.Filled.AccountTree, "Planned")
    }
}

@Composable
private fun KnowledgeScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Knowledge", color = White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
        Text("A separate memory space for every project.", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp, bottom = 20.dp))
        FeatureCard("Project Memory", "Requirements, decisions and context", Icons.Filled.FolderOpen)
        FeatureCard("Error Knowledge Base", "Solutions verified by tests", Icons.Filled.LibraryBooks, "Planned")
        FeatureCard("Skills Library", "Reusable capabilities and workflows", Icons.Filled.Extension, "Planned")
        FeatureCard("Dynamic Applets", "Your saved interactive tools", Icons.Filled.Widgets, "Planned")
    }
}

@Composable
private fun FeatureScreen(title: String, subtitle: String, icon: ImageVector, features: List<String>) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(title, color = White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp, bottom = 22.dp))
        features.forEach { FeatureCard(it, "Designed for a future Nexus module", icon, "Planned") }
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, icon: ImageVector, status: String = "Foundation") {
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dp).background(Surface, RoundedCornerShape(18.dp)).padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).background(Surface2, RoundedCornerShape(13.dp)), Alignment.Center) {
            Icon(icon, null, tint = White, modifier = Modifier.size(21.dp))
        }
        Column(Modifier.weight(1f).padding(start = 13.dp)) {
            Text(title, color = White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
        }
        Text(status, color = Muted, fontSize = 9.sp)
    }
}

@Composable
private fun SettingsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text("Settings", color = White) },
        text = { Column {
            Text("Nexus AI 1.0", color = White, fontWeight = FontWeight.SemiBold)
            Text("Hybrid architecture · Safe autonomy · Project memory", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            Text("AI provider: Not connected", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 14.dp))
            Text("This starter build stores chat messages locally. AI generation and advanced tools are not enabled yet.", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
        } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = White) } }
    )
}
