package com.zoho.task.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zoho.task.todolist.ui.theme.TodoListTheme
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private val viewmodel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VerticalCardViewPreview(viewmodel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalCardView(viewModel: MainViewModel) {
    val list by viewModel.employees.observeAsState()
    val scaffoldState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchTodos()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {

        val (title, lazyColumn, fab) = createRefs()
        var bottomGuideLine = createGuidelineFromBottom(0.1f)

        Text(
            text = "Title",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        LazyColumn(
            modifier = Modifier.constrainAs(lazyColumn) {
                top.linkTo(title.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomGuideLine)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {
            items(list!!.todos) {
                CardItemView(
                    cardItem = it,
                    onEditClick = { /* Handle edit click */ },
                    onDeleteClick = { /* Handle delete click */ },
                    onFabClick = { isSheetOpen = true },
                )
            }
        }

        FloatingActionButton(
            onClick = { isSheetOpen = true },
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }

    }

    // Display the bottom sheet when isSheetOpen is true
    if (isSheetOpen) {
        BottomSheetContent(
            scaffoldState = scaffoldState,
            onCloseSheet = { isSheetOpen = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    scaffoldState: SheetState,
    onCloseSheet: () -> Unit
) {
    ModalBottomSheet(
        sheetState = scaffoldState,
        onDismissRequest = { onCloseSheet() },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            val (text, button) = createRefs()

            Text(
                text = "Bottom Sheet Content",
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Button(
                onClick = { onCloseSheet() },
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(text.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
            ) {
                Text("Close Bottom Sheet")
            }
        }
    }
}

@Composable
fun CardItemView(
    cardItem: TodoListData.TodoItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFabClick: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
    ) {
        val (cardName, delete, edit, fab) = createRefs()

        Card(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .constrainAs(cardName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (text, button) = createRefs()

                Image(
                    painter = painterResource(R.drawable.delete),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(delete) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                )

                Image(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(edit) {
                        top.linkTo(parent.top)
                        end.linkTo(delete.start, margin = 10.dp)
                    }
                )

                Text(
                    text = cardItem.todo,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1, // Limit to a single line
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(edit.start)
                        width = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}


@Composable
fun VerticalCardViewPreview(viewmodel: MainViewModel) {
    VerticalCardView(viewmodel)
}





