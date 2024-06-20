package com.stechbindra.myshoppinglistapp

import android.graphics.Paint.Align
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ShoppingItems(
    val Id:Int,
    var name : String,
    var quantity : Int,
    var isEditing : Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppinglistApp(){

    var sItems by remember { mutableStateOf(listOf<ShoppingItems>()) }
    var showDialog by remember{ mutableStateOf(false) }
    var itemName  by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)) {

            Text("Add items")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems){
                items ->

                if(items.isEditing){
                    ShoppingItemEditor(items = items, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem = sItems.find { it.Id == items.Id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }
                else{
                    ShoppingListItems(items = items, onEditClick = {
                        //Finding out which elements we are editing and changing "isEditing" boolean to true
                        sItems = sItems.map{it.copy(isEditing = it.Id == items.Id)}
                    },
                        onDeleteClick = {
                            sItems = sItems - items
                    })
                }

            }
        }
        if (showDialog){
            AlertDialog(onDismissRequest = {  showDialog = false },
                confirmButton = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                                ) {
                                    Button(onClick = {
                                        if (itemName.isNotBlank()){
                                            val newItem = ShoppingItems(
                                                Id = sItems.size+1,
                                                name = itemName,
                                                quantity = itemQuantity.toInt()
                                            )
                                            sItems = sItems + newItem
                                            showDialog = false
                                            itemName = ""
                                        }
                                    }) {
                                        Text("Add")
                                        
                                    }
                                    Button(onClick = { showDialog = true }) {
                                        Text("Cancel")
                                    }
                                }
                },
                title = { Text(text = "Shopping List App")},
                text = {
                    Column {
                        OutlinedTextField(value = itemName , onValueChange = {itemName = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(value = itemQuantity , onValueChange = {itemQuantity = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                })
    }

    }
}
@Composable
fun ShoppingItemEditor(items: ShoppingItems, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(items.name) }
    var editedQuantity by remember { mutableStateOf(items.quantity.toString()) }
    var isEdited by remember { mutableStateOf(items.isEditing) }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = editedName, onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }
        Button(onClick = {
            isEdited = false
            onEditComplete(editedName, editedQuantity.toIntOrNull()?:1)
        }) {
            Text(text = "Save")
        }

    }
}
@Composable
fun ShoppingListItems(
    items : ShoppingItems,
    onEditClick : () -> Unit,
    onDeleteClick : ()-> Unit,
    ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(

                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            )
    ) {
        Text(text = items.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${items.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)

            }


        }
    }

}