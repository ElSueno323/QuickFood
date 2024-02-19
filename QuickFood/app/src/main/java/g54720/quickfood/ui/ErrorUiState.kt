package g54720.quickfood.ui

sealed interface ErrorUiState{
    object Success : ErrorUiState
    data class Error(val message:String) : ErrorUiState
    object Loading : ErrorUiState
}