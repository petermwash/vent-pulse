package com.nyoike.ventpulse.feature.profile.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nyoike.ventpulse.ui.theme.BrandPurple
import com.nyoike.ventpulse.ui.theme.MainBackground
import com.nyoike.ventpulse.ui.theme.MintCalm
import com.nyoike.ventpulse.ui.theme.SecondaryBackground

@Composable
fun ExpertCallRoot(
    displayName: String,
    role: String,
    onBack: () -> Unit,
    onOpenChat: () -> Unit
) {
    val callBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF2D294D), Color(0xFF17162D))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(callBackground)
            .padding(horizontal = 32.dp, vertical = 42.dp)
    ) {
        SessionPill(
            text = "05:23",
            modifier = Modifier.align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(width = 118.dp, height = 146.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MintCalm.copy(alpha = 0.92f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        SessionPill(
            text = "⋮",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(142.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(BrandPurple.copy(alpha = 0.96f), Color(0xFF9F90FF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialsFor(displayName),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = role,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.66f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallControl(label = "Mic", icon = CallIcon.Mic, onClick = {})
                CallControl(label = "Video", icon = CallIcon.Video, onClick = {})
                CallControl(
                    label = "End",
                    icon = CallIcon.End,
                    color = Color(0xFFFF756D),
                    size = 78.dp,
                    onClick = onBack
                )
                CallControl(label = "Chat", icon = CallIcon.Chat, onClick = onOpenChat)
            }
            Text(
                text = "End-to-end encrypted session",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.72f),
                modifier = Modifier.padding(top = 28.dp, bottom = 26.dp)
            )
        }
    }
}

@Composable
fun ExpertChatRoot(
    displayName: String,
    role: String,
    onBack: () -> Unit,
    onStartCall: () -> Unit
) {
    val messages = remember {
        mutableStateListOf(
            ExpertChatMessage(
                id = "intro",
                sender = displayName,
                body = "Hi, I am here with you. What would feel most helpful to talk through first?",
                isMine = false
            )
        )
    }
    var draft by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MainBackground, SecondaryBackground.copy(alpha = 0.78f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            ExpertChatHeader(
                displayName = displayName,
                role = role,
                onBack = onBack,
                onStartCall = onStartCall
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 18.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    ExpertMessageBubble(message = message)
                }
            }
            ExpertChatComposer(
                value = draft,
                onValueChange = { draft = it },
                onSend = {
                    val body = draft.trim()
                    if (body.isNotBlank()) {
                        messages += ExpertChatMessage(
                            id = "mine-${messages.size}",
                            sender = "You",
                            body = body,
                            isMine = true
                        )
                        draft = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ExpertChatHeader(
    displayName: String,
    role: String,
    onBack: () -> Unit,
    onStartCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White.copy(alpha = 0.92f),
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "‹",
                style = MaterialTheme.typography.headlineMedium,
                color = BrandPurple,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onBack)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(BrandPurple.copy(alpha = 0.84f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialsFor(displayName),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = BrandPurple.copy(alpha = 0.1f),
                modifier = Modifier.clickable(onClick = onStartCall)
            ) {
                Text(
                    text = "Call",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrandPurple,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpertMessageBubble(message: ExpertChatMessage) {
    Column(
        horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = if (message.isMine) 24.dp else 8.dp,
                bottomEnd = if (message.isMine) 8.dp else 24.dp
            ),
            color = if (message.isMine) BrandPurple.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.94f),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth(0.84f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = message.sender,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (message.isMine) Color.White.copy(alpha = 0.82f) else BrandPurple
                )
                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isMine) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpertChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Share what you need...") },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.92f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.84f),
                focusedBorderColor = BrandPurple.copy(alpha = 0.62f),
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
        Surface(
            shape = CircleShape,
            color = BrandPurple,
            modifier = Modifier
                .size(54.dp)
                .clickable(onClick = onSend)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "Send", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SessionPill(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.Black.copy(alpha = 0.52f),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp)
        )
    }
}

@Composable
private fun CallControl(
    label: String,
    icon: CallIcon,
    onClick: () -> Unit,
    color: Color = Color.White.copy(alpha = 0.14f),
    size: Dp = 64.dp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = color,
            tonalElevation = 0.dp,
            shadowElevation = if (color.alpha > 0.9f) 14.dp else 0.dp,
            modifier = Modifier
                .size(size)
                .clickable(onClick = onClick)
        ) {
            CallIconGlyph(
                icon = icon,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (icon == CallIcon.End) 22.dp else 19.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.58f)
        )
    }
}

@Composable
private fun CallIconGlyph(
    icon: CallIcon,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.1f
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        when (icon) {
            CallIcon.Mic -> {
                drawRoundRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = size.width * 0.34f,
                        y = size.height * 0.08f
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        width = size.width * 0.32f,
                        height = size.height * 0.5f
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = size.width * 0.16f,
                        y = size.width * 0.16f
                    ),
                    style = stroke
                )
                drawArc(
                    color = Color.White,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = size.width * 0.18f,
                        y = size.height * 0.34f
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        width = size.width * 0.64f,
                        height = size.height * 0.5f
                    ),
                    style = stroke
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.82f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.96f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.34f, size.height * 0.96f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.66f, size.height * 0.96f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            CallIcon.Video -> {
                drawRoundRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.08f, size.height * 0.28f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.58f, size.height * 0.44f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = size.width * 0.08f,
                        y = size.width * 0.08f
                    ),
                    style = stroke
                )
                val lens = Path().apply {
                    moveTo(size.width * 0.7f, size.height * 0.42f)
                    lineTo(size.width * 0.94f, size.height * 0.28f)
                    lineTo(size.width * 0.94f, size.height * 0.72f)
                    lineTo(size.width * 0.7f, size.height * 0.58f)
                    close()
                }
                drawPath(path = lens, color = Color.White, style = stroke)
            }

            CallIcon.Chat -> {
                drawRoundRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.16f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.8f, size.height * 0.62f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = size.width * 0.14f,
                        y = size.width * 0.14f
                    ),
                    style = stroke
                )
                val tail = Path().apply {
                    moveTo(size.width * 0.32f, size.height * 0.78f)
                    lineTo(size.width * 0.22f, size.height * 0.94f)
                    lineTo(size.width * 0.48f, size.height * 0.78f)
                }
                drawPath(path = tail, color = Color.White, style = stroke)
            }

            CallIcon.End -> {
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.18f, size.height * 0.18f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.82f, size.height * 0.82f),
                    strokeWidth = strokeWidth * 1.1f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.82f, size.height * 0.18f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.18f, size.height * 0.82f),
                    strokeWidth = strokeWidth * 1.1f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private enum class CallIcon {
    Mic,
    Video,
    Chat,
    End
}

private data class ExpertChatMessage(
    val id: String,
    val sender: String,
    val body: String,
    val isMine: Boolean
)

private fun initialsFor(name: String): String {
    return name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifBlank { "VP" }
}
