package com.example.myapplication.presentation.Screen

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Added clickable for IconButton in TopAppBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack // Changed to filled
import androidx.compose.material.icons.filled.Call // Changed to filled
import androidx.compose.material.icons.filled.Email // Changed to filled
import androidx.compose.material.icons.filled.Person // Changed to filled
import androidx.compose.material3.Button // Changed to M3 Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // Changed to M3 Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.presentation.ContactState
import com.example.myapplication.presentation.Utils.ImageCompress
import com.example.myapplication.presentation.utils.CustomTextField
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Removed 'it: Uri' from parameters
fun AddEditScreen(state: ContactState, onEvent: () -> Unit, navController: NavHostController) {
    val context = LocalContext.current
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? -> // Allow nullable Uri
            if (uri != null) {
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close() // Close stream
                    if (bytes != null) {
                        val compressImage = ImageCompress(bytes)
                        // Consider making max size a constant or configurable
                        if (compressImage.size > 1 * 1024 * 1024) { // 1MB limit
                            Toast.makeText(context, "Image size is too large (max 1MB)", Toast.LENGTH_SHORT).show()
                        } else {
                            state.image.value = compressImage
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                    // Log exception e
                }
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Add Contact")
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) { // Added IconButton for click handling
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // Changed to Filled
                        contentDescription = "Back",
                    )
                }
            })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageBitmap = state.image.value?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
            }
            Box(
                modifier = Modifier.size(150.dp), contentAlignment = Alignment.BottomEnd
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "contact image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person, // Changed to Filled
                        contentDescription = "contact image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .padding(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }, modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add, // Changed to Filled
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.name.value,
                onValueChange = { state.name.value = it },
                label = "Name",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = Icons.Filled.Person, // Changed to Filled
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = state.phone.value,
                onValueChange = { state.phone.value = it },
                label = "PhoneNumber",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = Icons.Filled.Call, // Changed to Filled
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = state.email.value,
                onValueChange = { state.email.value = it },
                label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = Icons.Filled.Email, // Changed to Filled
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button( // Changed to M3 Button
                onClick = {
                    onEvent() // This is correct, as onEvent is () -> Unit
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save", fontSize = 18.sp)
            }
        }
    }
}
