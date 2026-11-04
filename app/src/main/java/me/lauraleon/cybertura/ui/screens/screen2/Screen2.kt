package me.lauraleon.cybertura.ui.screens.screen2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

enum class Gender { HOMBRE, MUJER }

data class Character(
    val id: String,
    val name: String,
    val imageRes: Int,
    val gender: Gender,
)

@Composable
fun Screen2(
    onContinue: (String, Int, String, String) -> Unit = { _, _, _, _ -> }
) {
    var alias by remember { mutableStateOf("") }
    var age by remember { mutableFloatStateOf(10f) }
    var selectedGender by remember { mutableStateOf(Gender.HOMBRE) }
    var selectedCharacterId by remember { mutableStateOf("spider") }
    var showWarning by remember { mutableStateOf(false) }

    Screen2Content(
        alias = alias,
        onAliasChange = { alias = it },
        age = age,
        onAgeChange = { age = it },
        selectedGender = selectedGender,
        onGenderSelect = { 
            selectedGender = it
        },
        selectedCharacterId = selectedCharacterId,
        onCharacterSelect = { selectedCharacterId = it },
        showWarning = showWarning,
        onDismissWarning = { showWarning = false },
        onContinue = {
            if (alias.isBlank()) {
                showWarning = true
            } else {
                onContinue(alias, age.toInt(), selectedGender.name, selectedCharacterId)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2Content(
    alias: String,
    onAliasChange: (String) -> Unit,
    age: Float,
    onAgeChange: (Float) -> Unit,
    selectedGender: Gender,
    onGenderSelect: (Gender) -> Unit,
    selectedCharacterId: String,
    onCharacterSelect: (String) -> Unit,
    showWarning: Boolean,
    onDismissWarning: () -> Unit,
    onContinue: () -> Unit
) {
    val characters = listOf(
        Character(
            "spider",
            if (selectedGender == Gender.HOMBRE) "SPIDERGATO" else "SPIDERGATA",
            if (selectedGender == Gender.HOMBRE) R.drawable.spidergato else R.drawable.spidergata,
            selectedGender
        ),
        Character(
            "bat",
            if (selectedGender == Gender.HOMBRE) "BATIGATO" else "BATIGATA",
            if (selectedGender == Gender.HOMBRE) R.drawable.batigato else R.drawable.batigata,
            selectedGender
        ),
        Character(
            "super",
            if (selectedGender == Gender.HOMBRE) "SUPERGATO" else "SUPERGATA",
            if (selectedGender == Gender.HOMBRE) R.drawable.supergato else R.drawable.supergata,
            selectedGender
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Alias
            CyberSection(title = "1. ELIGE TU ALIAS") {
                OutlinedTextField(
                    value = alias,
                    onValueChange = onAliasChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Escribe tu alias o apodo", color = Color.Gray) },
                    trailingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = CyberViolet) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = CyberCyan
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Age
            CyberSection(title = "2. SELECCIONA TU EDAD") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("5", color = Color.White, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(2.dp, CyberViolet, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(age.toInt().toString(), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Text("18", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = age,
                        onValueChange = onAgeChange,
                        valueRange = 5f..18f,
                        colors = SliderDefaults.colors(
                            thumbColor = CyberViolet,
                            activeTrackColor = CyberCyan,
                            inactiveTrackColor = CyberBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Character
            CyberSection(title = "3. ELIGE TU PERSONAJE") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "GÉNERO",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GenderButton(
                            label = "HOMBRE",
                            icon = Icons.Default.Male,
                            selected = selectedGender == Gender.HOMBRE,
                            onClick = { onGenderSelect(Gender.HOMBRE) },
                            modifier = Modifier.weight(1f)
                        )
                        GenderButton(
                            label = "MUJER",
                            icon = Icons.Default.Female,
                            selected = selectedGender == Gender.MUJER,
                            onClick = { onGenderSelect(Gender.MUJER) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "PERSONAJE",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        characters.forEach { character ->
                            CharacterCard(
                                character = character,
                                selected = selectedCharacterId == character.id,
                                onClick = { onCharacterSelect(character.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(2.dp, CyberCyan, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(CyberBlueLight, CyberBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "CONTINUAR",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Warning Dialog Popup Overlay
        if (showWarning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(CyberBackground, RoundedCornerShape(20.dp))
                        .border(2.dp, CyberDanger, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Text(
                        text = "⚠️ ¡Atención!",
                        color = CyberDanger,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Por favor, escribe tu alias o apodo para continuar",
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismissWarning,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberViolet),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "ENTENDIDO",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CyberSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, CyberViolet.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun GenderButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) CyberCyan else CyberViolet.copy(alpha = 0.5f)
    val bgColor = if (selected) CyberBlue.copy(alpha = 0.5f) else Color.Transparent

    Column(
        modifier = modifier
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(bgColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) CyberCyan else Color.White,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CharacterCard(
    character: Character,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) CyberCyan else CyberViolet.copy(alpha = 0.5f)

    Column(
        modifier = modifier
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = character.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = character.name,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Screen2Preview() {
    CyberTuraTheme {
        Screen2()
    }
}

@Preview(showBackground = true, device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun Screen2TabletPreview() {
    CyberTuraTheme {
        Screen2()
    }
}
