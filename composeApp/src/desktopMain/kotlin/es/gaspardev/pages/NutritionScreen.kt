package es.gaspardev.pages

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.rememberToasterState
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.usecases.create.CreateDietTemplate
import es.gaspardev.core.domain.usecases.create.CreateNewDiet
import es.gaspardev.core.domain.usecases.read.GetTrainerDietsPlans
import es.gaspardev.core.domain.usecases.read.GetTrainerDietsTemplates
import es.gaspardev.enums.DietType
import es.gaspardev.helpers.createDiet
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog
import es.gaspardev.layout.nutrition.NutritionPlanCard
import es.gaspardev.layout.nutrition.NutritionTemplateCard
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


@Composable
fun NutritionScreen(width: Dp) {

   val scope = rememberCoroutineScope()
   val toaster = rememberToasterState()
   val tabs = listOf(
      DietType.ALL to stringResource(Res.string.all),
      DietType.BALANCED to stringResource(Res.string.balanced),
      DietType.LOW_CARB to stringResource(Res.string.low_carb),
      DietType.MUSCLE_GAIN to stringResource(Res.string.muscle_gain),
      DietType.PERFORMANCE to stringResource(Res.string.performance_plan),
      DietType.VEGAN to stringResource(Res.string.vegan),
      DietType.VEGETARIAN to stringResource(Res.string.vegetarian),
      DietType.WEIGHT_LOSS to stringResource(Res.string.weight_loss)
   )

   var searchQuery by remember { mutableStateOf("") }
   var activeTab by remember { mutableStateOf(DietType.ALL) }

   var dietPlans by remember { mutableStateOf<List<DietPlan>>(emptyList()) }
   var dietTemplates by remember { mutableStateOf<List<DietTemplate>>(emptyList()) }

   LaunchedEffect(Unit) {
      val trainer = LoggedTrainer.state.trainer!!

      GetTrainerDietsPlans().run(trainer).fold(
         { value -> dietPlans = value },
         { _ -> }
      )

      GetTrainerDietsTemplates().run(trainer).fold(
         { value -> dietTemplates = value },
         { _ -> }
      )
   }

   var filteredPlans = dietPlans.filter { plan ->
      val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
              plan.description.contains(searchQuery, ignoreCase = true)

      if (activeTab == DietType.ALL) {
         matchesSearch
      } else {
         matchesSearch && plan.type == activeTab
      }
   }

   val lazyGridState1 = rememberLazyGridState()
   val lazyGridState2 = rememberLazyGridState()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)
   ) {
      Card {
         // Header section
         Column(modifier = Modifier.padding(16.dp)) {
            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
            ) {
               Column {
                  Text(
                     text = stringResource(Res.string.diets_plans),
                     style = MaterialTheme.typography.subtitle1,
                     fontWeight = FontWeight.Bold
                  )
                  Text(
                     text = stringResource(Res.string.nutrition_plans_description),
                     style = MaterialTheme.typography.body1
                  )
               }
               Button(
                  onClick = {
                     DialogState.openWith {
                        DietDialog { diet ->
                           scope.launch {
                              CreateNewDiet().run(Pair(diet, LoggedTrainer.state.trainer!!)).fold(
                                 { value ->
                                    dietPlans = dietPlans + (
                                            DietPlan(
                                               dietId = value,
                                               name = diet.name,
                                               description = diet.description,
                                               type = diet.dietType,
                                               duration = diet.duration,
                                               frequency = diet.dishes.keys.count().toString(),
                                               asignedCount = 0,
                                               dishes = diet.dishes
                                            )
                                            )
                                    toaster.show(
                                       "El plan se ha creado correctamente",
                                       type = ToastType.Success
                                    )
                                 },
                                 { err ->
                                    toaster.show(
                                       if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                       type = ToastType.Error
                                    )
                                 }
                              )
                           }
                        }
                     }
                  },
                  contentPadding = ButtonDefaults.ContentPadding
               ) {
                  Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                  Text(stringResource(Res.string.create_nutrition_plan))
               }
            }

            Spacer(Modifier.height(24.dp))

            // Search section
            Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               OutlinedTextField(
                  value = searchQuery,
                  onValueChange = { searchQuery = it },
                  placeholder = { Text(stringResource(Res.string.search_plans)) },
                  leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                  modifier = Modifier.weight(1f)
               )
            }
         }
      }
      Spacer(Modifier.height(12.dp))

      Card(Modifier.fillMaxSize().padding(bottom = 16.dp)) {
         Column {
            ScrollableTabRow(
               selectedTabIndex = 0,
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
            Box(Modifier.fillMaxHeight(.45f).fillMaxWidth()) {
               if (filteredPlans.isNotEmpty()) {
                  LazyVerticalGrid(
                     columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                     modifier = Modifier.fillMaxSize().padding(12.dp),
                     state = lazyGridState1,
                     verticalArrangement = Arrangement.spacedBy(16.dp),
                     horizontalArrangement = Arrangement.spacedBy(16.dp)
                  ) {
                     items(filteredPlans) { plan ->
                        NutritionPlanCard(
                           plan, scope,
                           onEditAction = { newDietPlan ->
                              filteredPlans = filteredPlans - plan + newDietPlan
                           },
                           onDuplicationAction = { newDiplicatePlan ->
                              filteredPlans = filteredPlans + newDiplicatePlan
                           },
                           onAsign = { newRevaluedPlan ->
                              filteredPlans = filteredPlans - plan + newRevaluedPlan
                           },
                           onDeleteAction = {
                              filteredPlans = filteredPlans - plan
                           })
                     }
                  }
                  Spacer(Modifier.height(8.dp))
                  VerticalScrollbar(
                     adapter = rememberScrollbarAdapter(lazyGridState1),
                     modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(vertical = 16.dp),
                     style = LocalScrollbarStyle.current.copy(
                        hoverColor = MaterialTheme.colors.primaryVariant,
                        unhoverColor = MaterialTheme.colors.primary
                     )
                  )
               } else {
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
                     Text(
                        stringResource(Res.string.no_nutrition_plans_found),
                        style = MaterialTheme.typography.subtitle1
                     )
                     Spacer(Modifier.height(8.dp))
                     Text(
                        text = if (searchQuery.isNotEmpty()) stringResource(Res.string.try_different_search)
                        else stringResource(Res.string.create_first_workout),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                     )
                     Spacer(Modifier.height(16.dp))
                     Button(
                        onClick = {
                           DialogState.openWith {
                              DietDialog { diet ->
                                 scope.launch {
                                    CreateNewDiet().run(Pair(diet, LoggedTrainer.state.trainer!!)).fold(
                                       { value ->
                                          dietPlans = dietPlans + (
                                                  DietPlan(
                                                     dietId = value,
                                                     name = diet.name,
                                                     description = diet.description,
                                                     type = diet.dietType,
                                                     duration = diet.duration,
                                                     frequency = diet.dishes.keys.count().toString(),
                                                     asignedCount = 0,
                                                     dishes = diet.dishes
                                                  )
                                                  )
                                          toaster.show(
                                             "El plan se ha creado correctamente",
                                             type = ToastType.Success
                                          )
                                       },
                                       { err ->
                                          toaster.show(
                                             if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                             type = ToastType.Error
                                          )
                                       }
                                    )
                                 }
                              }
                           }
                        },
                        contentPadding = ButtonDefaults.ContentPadding
                     ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(Res.string.create_nutrition_plan))
                     }
                  }
               }
            }
            Spacer(Modifier.height(32.dp))
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
               Text(
                  text = stringResource(Res.string.nutrition_templates),
                  style = MaterialTheme.typography.h2,
                  fontWeight = FontWeight.Bold
               )
               Button(onClick = {
                  createDiet() { diet ->
                     val template: DietTemplate = DietTemplate.fromDiet(diet)

                     scope.launch {
                        CreateDietTemplate().run(Pair(template, LoggedTrainer.state.trainer!!)).fold(
                           { newId -> dietTemplates = dietTemplates + template.copy(templateId = newId) },
                           { err -> ToastManager.showError(err.message!!) }
                        )
                     }
                  }
               }) {
                  Text("Crear nueva plantilla")
               }
            }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxSize()) {
               LazyVerticalGrid(
                  columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                  modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                  state = lazyGridState2,
                  verticalArrangement = Arrangement.spacedBy(16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp)
               ) {
                  items(dietTemplates) { template ->
                     NutritionTemplateCard(template, scope, { id ->
                        dietTemplates = dietTemplates - dietTemplates.first { it.getId() == id }
                     }) { diet ->
                        scope.launch {
                           CreateNewDiet().run(Pair(diet, LoggedTrainer.state.trainer!!))
                              .fold(
                                 { value ->
                                    dietPlans = dietPlans + (
                                            DietPlan(
                                               dietId = value,
                                               name = diet.name,
                                               description = diet.description,
                                               type = diet.dietType,
                                               duration = diet.duration,
                                               frequency = diet.dishes.keys.count().toString(),
                                               asignedCount = 0,
                                               dishes = diet.dishes
                                            )
                                            )
                                    toaster.show(
                                       "La rutina se ha creado correctamente",
                                       type = ToastType.Success
                                    )
                                 },
                                 { err -> // Error
                                    toaster.show(
                                       if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                       type = ToastType.Error
                                    )
                                 }
                              )
                        }
                     }
                  }
               }
               Spacer(Modifier.width(8.dp))
               VerticalScrollbar(
                  adapter = rememberScrollbarAdapter(lazyGridState2),
                  modifier = Modifier
                     .align(Alignment.CenterEnd)
                     .fillMaxHeight()
                     .padding(vertical = 16.dp),
                  style = LocalScrollbarStyle.current.copy(
                     hoverColor = MaterialTheme.colors.primaryVariant,
                     unhoverColor = MaterialTheme.colors.primary
                  )
               )
            }
         }
      }
   }
}

