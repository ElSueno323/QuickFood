package g54720.quickfood.ui.screen

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import g54720.quickfood.R
import g54720.quickfood.ui.theme.QuickFoodTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
            navigateUp:()->Unit,
           canNavigateUp:Boolean
){
    TopAppBar(
        title = {},
        navigationIcon = {
            if(canNavigateUp){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        modifier = Modifier
            .height(50.dp)
        ,colors=TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}


@Preview(showBackground=true)
@Composable
fun TopBarPreview(){
    QuickFoodTheme {
        TopBar(navigateUp = {}, canNavigateUp = true)
    }
}