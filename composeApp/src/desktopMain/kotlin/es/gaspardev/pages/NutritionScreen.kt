package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.usecases.read.GetTrainerDietsPlans
import es.gaspardev.core.domain.usecases.read.GetTrainerDietsTemplates
import es.gaspardev.enums.DietType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietCreationDialog
import es.gaspardev.layout.nutrition.NutritionPlanCard
import es.gaspardev.layout.nutrition.NutritionTemplateCard
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.all
import fit_me.composeapp.generated.resources.filters
import org.jetbrains.compose.resources.stringResource

@Composable
fun NutritionScreen() {

   val controller = LocalRouter.current
   val tabs = listOf(
      DietType.ALL to stringResource(Res.string.all),
      DietType.BALANCED to "Balanceado",
      DietType.LOW_CARB to "Bajos Carbohidratos",
      DietType.MUSCLE_GAIN to "Ganancia Muscular",
      DietType.PERFORMANCE to "Rendimiento",
      DietType.VEGAN to "Vegano",
      DietType.VEGETARIAN to "Vegeratiano",
      DietType.WEIGHT_LOSS to "Perdida de Peso"
   )

   val scrollState = rememberScrollState()
   var searchQuery by remember { mutableStateOf("") }
   var activeTab by remember { mutableStateOf(DietType.ALL) }
   var isModalOpen by remember { mutableStateOf(false) }


   var nutritionPlans: List<DietPlan> = listOf()
   var nutritionTemplates: List<DietTemplate> = listOf()

   LaunchedEffect(Unit) {
      val trainer = LoggedTrainer.state.trainer!!

      GetTrainerDietsPlans().run(trainer).fold(
         { value -> nutritionPlans = value },
         { _ -> }
      )

      GetTrainerDietsTemplates().run(trainer).fold(
         { value -> nutritionTemplates = value },
         { _ -> }
      )
   }

   val filteredPlans = nutritionPlans.filter { plan ->
      val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
              plan.description.contains(searchQuery, ignoreCase = true)

      if (activeTab == DietType.ALL) {
         matchesSearch
      } else {
         matchesSearch && plan.type == activeTab
      }
   }

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)
         .verticalScroll(scrollState)
   ) {
      Card {
         Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
            ) {
               Column {
                  Text(
                     text = "Diets Plans",
                     style = MaterialTheme.typography.subtitle1,
                     fontWeight = FontWeight.Bold
                  )
                  Text(
                     text = "Create and manage nutrition plans for your athletes",
                     style = MaterialTheme.typography.body1
                  )
               }
               Button(onClick = {
                  DialogState.openWith { DietCreationDialog { } }
               }) {
                  Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                  Text("Create Nutrition Plan")
               }
            }

            Spacer(Modifier.height(24.dp))

            // Search + Filter
            Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               OutlinedTextField(
                  value = searchQuery,
                  onValueChange = { searchQuery = it },
                  placeholder = { Text("Search plans...") },
                  leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                  modifier = Modifier.weight(1f)
               )
               OutlinedButton(
                  onClick = { /* TODO: implement filter logic */ },
                  shape = RoundedCornerShape(10.dp)
               ) {
                  Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                  Text(stringResource(Res.string.filters))
               }
            }
         }
      }
      Spacer(Modifier.height(12.dp))

      Card(Modifier.fillMaxSize()) {
         Column {
            ScrollableTabRow(
               selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.takeIf { it >= 0 } ?: 0,
               edgePadding = 0.dp
            ) {
               tabs.forEachIndexed { _, (workoutType, title) ->
                  Tab(
                     selected = activeTab == workoutType,
                     onClick = { activeTab = workoutType },
                     text = { Text(title) }
                  )
               }
            }

            Spacer(Modifier.height(16.dp))

            // Nutrition Plans
            if (filteredPlans.isEmpty()) {
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 48.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  Icon(
                     FitMeIcons.Nutrition,
                     contentDescription = null,
                     modifier = Modifier.size(72.dp),
                     tint = Color.LightGray
                  )
                  Spacer(modifier = Modifier.height(16.dp))
                  Text("No workout plans found", style = MaterialTheme.typography.subtitle1)
                  Text("Try a different search term", style = MaterialTheme.typography.body2)
               }
            } else {
               LazyVerticalGrid(
                  columns = GridCells.Fixed(4),
                  modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(12.dp),
                  verticalArrangement = Arrangement.spacedBy(16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp)
               ) {
                  items(filteredPlans) { plan ->
                     NutritionPlanCard(plan)
                  }
               }
            }

            Spacer(Modifier.height(32.dp))

            // Templates section
            Text(
               text = "Nutrition Templates",
               style = MaterialTheme.typography.h2,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
            )

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
               columns = GridCells.Fixed(4),
               modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(12.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp),
               horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
               items(nutritionTemplates) { templatePlan ->
                  NutritionTemplateCard(templatePlan)
               }
            }
         }
      }
   }
}



