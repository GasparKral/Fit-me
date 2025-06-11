package es.gaspardev.pages

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.usecases.create.diet.CreateDietTemplate
import es.gaspardev.core.domain.usecases.create.diet.CreateNewDiet
import es.gaspardev.core.domain.usecases.read.user.trainer.GetTrainerDietsPlans
import es.gaspardev.core.domain.usecases.read.user.trainer.GetTrainerDietsTemplates
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
    // States
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(DietType.ALL) }
    var dietPlans by remember { mutableStateOf<List<DietPlan>>(emptyList()) }
    var dietTemplates by remember { mutableStateOf<List<DietTemplate>>(emptyList()) }

    val lazyGridState1 = rememberLazyGridState()
    val lazyGridState2 = rememberLazyGridState()

    // Constants
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

    // Computed values
    val filteredPlans = remember(dietPlans, searchQuery, activeTab) {
        dietPlans.filter { plan ->
            val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
                    plan.description.contains(searchQuery, ignoreCase = true)

            if (activeTab == DietType.ALL) matchesSearch
            else matchesSearch && plan.type == activeTab
        }
    }

    // Data loading
    LaunchedEffect(Unit) {
        LoggedTrainer.state.trainer?.let { trainer ->
            GetTrainerDietsPlans().run(trainer).fold(
                onSuccess = { dietPlans = it },
                onFailure = { /* Handle error if needed */ }
            )

            GetTrainerDietsTemplates().run(trainer).fold(
                onSuccess = { dietTemplates = it },
                onFailure = { /* Handle error if needed */ }
            )
        }
    }

    // Event handlers
    fun createNewDiet() {
        DialogState.openWith {
            DietDialog { diet ->
                scope.launch {
                    LoggedTrainer.state.trainer?.let { trainer ->
                        CreateNewDiet().run(Pair(diet, trainer)).fold(
                            onSuccess = { dietId ->
                                val newPlan = DietPlan(
                                    dietId = dietId,
                                    name = diet.name,
                                    description = diet.description,
                                    type = diet.dietType,
                                    duration = diet.duration,
                                    frequency = diet.dishes.keys.count().toString(),
                                    asignedCount = 0,
                                    dishes = diet.dishes
                                )
                                dietPlans = dietPlans + newPlan
                                ToastManager.show("El plan se ha creado correctamente")
                            },
                            onFailure = { error ->
                                val message = error.message ?: "Error al crear la dieta"
                                ToastManager.show(message)
                            }
                        )
                    }
                }
            }
        }
    }

    fun createNewTemplate() {
        createDiet { diet ->
            val template = DietTemplate.fromDiet(diet)
            scope.launch {
                LoggedTrainer.state.trainer?.let { trainer ->
                    CreateDietTemplate().run(Pair(template, trainer)).fold(
                        onSuccess = { templateId ->
                            dietTemplates = dietTemplates + template.copy(templateId = templateId)
                            ToastManager.show("Plantilla creada correctamente")
                        },
                        onFailure = { error ->
                            ToastManager.showError(error.message ?: "Error al crear la plantilla")
                        }
                    )
                }
            }
        }
    }

    fun createDietFromTemplate(diet: Diet) {
        scope.launch {
            LoggedTrainer.state.trainer?.let { trainer ->
                CreateNewDiet().run(Pair(diet, trainer)).fold(
                    onSuccess = { dietId ->
                        val newPlan = DietPlan(
                            dietId = dietId,
                            name = diet.name,
                            description = diet.description,
                            type = diet.dietType,
                            duration = diet.duration,
                            frequency = diet.dishes.keys.count().toString(),
                            asignedCount = 0,
                            dishes = diet.dishes
                        )
                        dietPlans = dietPlans + newPlan
                        ToastManager.show("Dieta creada correctamente")
                    },
                    onFailure = { error ->
                        val message = error.message ?: "Error al crear la dieta"
                        ToastManager.show(message)
                    }
                )
            }
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Card
        HeaderSection(
            onCreateDietClick = ::createNewDiet,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )

        Spacer(Modifier.height(12.dp))

        // Main Content Card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            Column {
                // Tabs
                DietTypeTabs(
                    tabs = tabs,
                    activeTab = activeTab,
                    onTabSelected = { activeTab = it }
                )

                Spacer(Modifier.height(16.dp))

                // Diet Plans Section
                DietPlansSection(
                    plans = filteredPlans,
                    width = width,
                    lazyGridState = lazyGridState1,
                    searchQuery = searchQuery,
                    onCreateDietClick = ::createNewDiet,
                    onEditPlan = { oldPlan, newPlan ->
                        dietPlans = dietPlans - oldPlan + newPlan
                    },
                    onDuplicatePlan = { newPlan ->
                        dietPlans = dietPlans + newPlan
                    },
                    onAssignPlan = { oldPlan, newPlan ->
                        dietPlans = dietPlans - oldPlan + newPlan
                    },
                    onDeletePlan = { plan ->
                        dietPlans = dietPlans - plan
                    }
                )

                Spacer(Modifier.height(32.dp))

                // Templates Section
                TemplatesSection(
                    templates = dietTemplates,
                    width = width,
                    lazyGridState = lazyGridState2,
                    onCreateTemplateClick = ::createNewTemplate,
                    onDeleteTemplate = { templateId ->
                        dietTemplates = dietTemplates.filterNot { it.templateId == templateId }
                    },
                    onCreateFromTemplate = ::createDietFromTemplate
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onCreateDietClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Card {
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
                    onClick = onCreateDietClick,
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(Res.string.create_nutrition_plan))
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(stringResource(Res.string.search_plans)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DietTypeTabs(
    tabs: List<Pair<DietType, String>>,
    activeTab: DietType,
    onTabSelected: (DietType) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.takeIf { it >= 0 } ?: 0,
        edgePadding = 0.dp
    ) {
        tabs.forEach { (type, title) ->
            Tab(
                selected = activeTab == type,
                onClick = { onTabSelected(type) },
                text = { Text(title, color = MaterialTheme.colors.onPrimary) }
            )
        }
    }
}

@Composable
private fun DietPlansSection(
    plans: List<DietPlan>,
    width: Dp,
    lazyGridState: LazyGridState,
    searchQuery: String,
    onCreateDietClick: () -> Unit,
    onEditPlan: (DietPlan, DietPlan) -> Unit,
    onDuplicatePlan: (DietPlan) -> Unit,
    onAssignPlan: (DietPlan, DietPlan) -> Unit,
    onDeletePlan: (DietPlan) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight(.45f)
            .fillMaxWidth()
    ) {
        if (plans.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                modifier = Modifier.fillMaxSize().padding(12.dp),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(plans) { plan ->
                    NutritionPlanCard(
                        plan = plan,
                        scope = rememberCoroutineScope(),
                        onEditAction = { newPlan -> onEditPlan(plan, newPlan) },
                        onDuplicationAction = onDuplicatePlan,
                        onAssignAction = { newPlan -> onAssignPlan(plan, newPlan) },
                        onDeleteAction = { onDeletePlan(plan) }
                    )
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(lazyGridState),
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
            EmptyPlansState(
                searchQuery = searchQuery,
                onCreateDietClick = onCreateDietClick
            )
        }
    }
}

@Composable
private fun EmptyPlansState(
    searchQuery: String,
    onCreateDietClick: () -> Unit
) {
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
            text = if (searchQuery.isNotEmpty()) {
                stringResource(Res.string.try_different_search)
            } else {
                stringResource(Res.string.create_first_workout)
            },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onCreateDietClick,
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(Res.string.create_nutrition_plan))
        }
    }
}

@Composable
private fun TemplatesSection(
    templates: List<DietTemplate>,
    width: Dp,
    lazyGridState: LazyGridState,
    onCreateTemplateClick: () -> Unit,
    onDeleteTemplate: (Int) -> Unit,
    onCreateFromTemplate: (Diet) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.nutrition_templates),
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Bold
        )

        Button(onClick = onCreateTemplateClick) {
            Text("Crear nueva plantilla")
        }
    }

    Spacer(Modifier.height(16.dp))

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(templates) { template ->
                NutritionTemplateCard(
                    template = template,
                    scope = rememberCoroutineScope(),
                    onDeleteAction = onDeleteTemplate,
                    onAcceptAction = onCreateFromTemplate
                )
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(lazyGridState),
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