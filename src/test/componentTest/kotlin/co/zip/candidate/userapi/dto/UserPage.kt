package co.zip.candidate.userapi.dto

data class UserPage(
    val numberOfElements: Int,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int,
    val content: List<UserDTO>
)
