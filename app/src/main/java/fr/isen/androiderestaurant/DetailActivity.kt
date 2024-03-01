package fr.isen.androiderestaurant

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import fr.isen.androiderestaurant.network.Dish
import fr.isen.androiderestaurant.network.Ingredient
import androidx.compose.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.res.painterResource
import fr.isen.androiderestaurant.basket.Basket
import fr.isen.androiderestaurant.basket.BasketActivity
import android.content.Intent
import kotlin.math.max
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import android.widget.Toast
class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DetailActivity.DISH_EXTRA_KEY)  as? Dish
        val imageUri = intent.getStringExtra(DetailActivity.IMAGE_URI_EXTRA_KEY)

        setContent {
            val context = LocalContext.current
            val count = remember {
                mutableIntStateOf(1)
            }

            Column (
                modifier = Modifier
                    .padding(bottom=60.dp)
            ){
                Header()

            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier=Modifier
                .padding(top = 70.dp)) {
                Row {
                    // Nom du plat
                    Text(
                        text = dish?.name ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Black,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            val intent = Intent(context, BasketActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = IconButtonColors( contentColor = Color.White,containerColor = Color.Red,disabledContainerColor = Color.Gray, disabledContentColor = Color.Gray)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart, // Remplacez ici par l'icône du panier
                            contentDescription = "Add to Cart",
                            tint = Color.White
                        )
                    }
                }

            }
            Spacer(modifier = Modifier
                .height(16.dp)
                )


            Column(modifier =
            Modifier
                .padding(top=50.dp,bottom=60.dp)) {

                imageUri?.let { uri ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uri)
                            .build(),
                        contentDescription = null,
                        error = painterResource(R.drawable.restaurant),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .padding(top = 80.dp)
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                val ingredient = dish?.ingredients?.map { ingredient -> ingredient.name }?.joinToString(", ") ?: ""
                    Column(modifier=
                    Modifier
                        .padding(top=10.dp)
                    ) {
                        Text(text = "Ingredients:")
                    }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Affichage des ingrédients

                    Text(
                        text = ingredient,
                        modifier = Modifier.weight(3f),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        ),
                    )


                }
                Column {

                    var quantity by remember { mutableStateOf(1) }
                    var unitPrice = dish?.prices?.first()?.price?.toDouble() ?:0.0 // Prix unitaire du plat
                    var totalPrice = quantity.toDouble() * unitPrice

                    Row (modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                    ) {
                        IconButton(
                            onClick = {  if (quantity > 0){
                                quantity--
                                count.value=quantity
                                totalPrice = quantity * unitPrice}
                                      },
                            modifier = Modifier.padding(8.dp),
                            colors = IconButtonColors( contentColor = Color.White,containerColor = Color.Red,disabledContainerColor = Color.Gray, disabledContentColor = Color.Gray)

                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Quantity",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = quantity.toString(), // Remplacer par la quantité réelle
                            modifier = Modifier.padding(top=16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        // Bouton pour incrémenter la quantité
                        IconButton(
                            onClick = {
                                quantity++
                                count.value=quantity
                                totalPrice = quantity * unitPrice},
                            modifier = Modifier.padding(8.dp),
                            colors = IconButtonColors( contentColor = Color.White,containerColor = Color.Red,disabledContainerColor = Color.Gray, disabledContentColor = Color.Gray)

                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Quantity",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                                text = "Total : ${totalPrice.toString()} £",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.Red,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ))

                    }


                }
                Column(modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                         onClick = {
                            if (dish != null) {
                                Basket.current(context).add(dish,count.value , context)
                            }
                             Toast.makeText(context, "added to basket", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonColors(contentColor = Color.White, containerColor = Color.Red, disabledContainerColor = Color.Gray, disabledContentColor = Color.Gray)
                    ) {
                        Text(text = "Add Basket")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }

        Log.d("lifeCycle", "Detail Activity - OnCreate")
    }

    companion object{
        val DISH_EXTRA_KEY ="DISH_EXTRA_KEY"
        val IMAGE_URI_EXTRA_KEY="IMAGE_URI_EXTRA_KEY"

    }
}


