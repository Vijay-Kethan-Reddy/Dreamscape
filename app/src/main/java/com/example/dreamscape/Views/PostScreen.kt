package com.example.dreamscape.Views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dreamscape.Models.DreamCategory
import com.example.dreamscape.ViewModels.CategoriesViewModel
import com.example.dreamscape.ViewModels.CategoryDreamsViewModel
import com.example.dreamscape.ViewModels.PostDreamViewModel
import com.example.dreamscape.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,
    viewModel: PostDreamViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    categoryDreamsViewModel: CategoryDreamsViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    val categories by categoriesViewModel.categories.collectAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<DreamCategory?>(null) }


    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("New Dream", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                ),
                windowInsets = WindowInsets(0)
            )
        },
        contentWindowInsets = WindowInsets(0),
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)

    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Dream Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = InputBorder,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = TextTertiary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = InputFieldBackground,
                    unfocusedContainerColor = InputFieldBackground
                )
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content =  it },
                label = { Text("Describe your dream") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = InputBorder,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = TextTertiary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = InputFieldBackground,
                    unfocusedContainerColor = InputFieldBackground
                )
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded}
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedCategory?.name ?: "Select a category",
                    onValueChange = {},
                    label = { Text("Category") },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = InputBorder,
                        cursorColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = TextTertiary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = InputFieldBackground,
                        unfocusedContainerColor = InputFieldBackground
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name, color = TextPrimary) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                                Log.d("PostScreen", "Selected category: ${category.id} / ${category.name}")
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedCategory == null) return@Button
                    isPosting = true
                    viewModel.postDream(title, content, selectedCategory!!.id) { newDream ->
                        isPosting = false
                        categoryDreamsViewModel.addDream(newDream)
                        navController.navigate("main"){
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = !isPosting && title.isNotBlank() && content.isNotBlank() && selectedCategory != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text(if (isPosting) "Posting..." else "Post Dream")
            }
        }
    }
}