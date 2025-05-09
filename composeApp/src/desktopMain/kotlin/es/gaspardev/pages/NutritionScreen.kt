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
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.Nutrition.NutritionPlanCard
import es.gaspardev.layout.Nutrition.NutritionTemplateCard

// Data classes
data class NutritionPlan(
   val id: String,
   val name: String,
   val description: String,
   val type: String,
   val duration: String,
   val mealsPerDay: Int,
   val caloriesPerDay: Int,
   val difficulty: String,
   val assignedCount: Int,
   val lastUpdated: String,
   val recipes: Int
)

data class NutritionTemplate(
   val id: String,
   val name: String,
   val description: String,
   val type: String,
   val difficulty: String,
   val recipes: Int
)

@Composable
fun NutritionScreen(controller: RouterController) {
   val scrollState = rememberScrollState()
   var searchQuery by remember { mutableStateOf("") }
   var activeTab by remember { mutableStateOf("all") }
   var isModalOpen by remember { mutableStateOf(false) }

   // Sample data
   val nutritionPlans = listOf(
      NutritionPlan(
         id = "1",
         name = "Weight Loss Meal Plan",
         description = "Calorie-controlled meal plan designed for sustainable weight loss",
         type = "weight-loss",
         duration = "12 weeks",
         mealsPerDay = 5,
         caloriesPerDay = 1800,
         difficulty = "intermediate",
         assignedCount = 10,
         lastUpdated = "3 days ago",
         recipes = 35
      ),
      // Add other plans...
   )

   val nutritionTemplates = listOf(
      NutritionTemplate(
         id = "t1",
         name = "Mediterranean Diet",
         description = "Heart-healthy eating plan based on Mediterranean cuisine",
         type = "balanced",
         difficulty = "beginner",
         recipes = 18
      ),
      // Add other templates...
   )

   val filteredPlans = nutritionPlans.filter { plan ->
      val matchesSearch =
         plan.name.contains(searchQuery, ignoreCase = true) &&
                 (activeTab == "all" || plan.type.equals(activeTab, ignoreCase = true))
      if (activeTab == "all") matchesSearch else matchesSearch && plan.type == activeTab
   }

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)
         .verticalScroll(scrollState)
   ) {
      // Header
      Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceBetween,
         verticalAlignment = Alignment.CenterVertically
      ) {
         Column {
            Text(
               text = "Nutrition Plans",
               style = MaterialTheme.typography.subtitle1,
               fontWeight = FontWeight.Bold
            )
            Text(
               text = "Create and manage nutrition plans for your athletes",
               style = MaterialTheme.typography.body1
            )
         }
         Button(onClick = { isModalOpen = true }) {
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
            Text("Filters")
         }
      }

      Spacer(Modifier.height(16.dp))

      // Tabs
      val tabs = listOf("All", "Weight Loss", "Muscle Gain", "Maintenance")
      ScrollableTabRow(
         selectedTabIndex = tabs.indexOfFirst { it.equals(activeTab, ignoreCase = true) },
         edgePadding = 0.dp,
         modifier = Modifier.fillMaxWidth()
      ) {
         tabs.forEachIndexed { index, tab ->
            Tab(
               selected = tab.equals(activeTab, ignoreCase = true),
               onClick = { activeTab = tab.lowercase() },
               text = { Text(tab) }
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
               tint = Color.LightGray,
               modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No plans found", style = MaterialTheme.typography.subtitle1)
            Text("Try adjusting your search or filters.", style = MaterialTheme.typography.body2)
         }
      } else {
         LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(vertical = 12.dp),
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
         style = MaterialTheme.typography.subtitle1,
         fontWeight = FontWeight.Bold
      )

      Spacer(Modifier.height(16.dp))

      LazyVerticalGrid(
         columns = GridCells.Fixed(4),
         modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(vertical = 12.dp),
         verticalArrangement = Arrangement.spacedBy(16.dp),
         horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         items(nutritionTemplates) { templatePlan ->
            NutritionTemplateCard(templatePlan)
         }
      }
   }

}



