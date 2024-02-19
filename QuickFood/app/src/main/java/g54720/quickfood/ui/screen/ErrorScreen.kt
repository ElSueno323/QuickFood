package g54720.quickfood.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import g54720.quickfood.R

@Composable
fun ViewLoading(){
    Image(
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading),
        modifier= Modifier.size(200.dp)
    )
}

@Composable
fun ViewError(message: String){
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painterResource(
                id = R.drawable.ic_connection_error,
            ),
            contentDescription = stringResource(R.string.error_connection_description)
        )
        if(message == "Error:HTTP 402 "){
            Text(text = stringResource(R.string.api_empty_credits),
                modifier= Modifier.padding(16.dp)
            )
        }else{
            Text(text = message,
                modifier= Modifier.padding(16.dp)
            )
        }
    }
}