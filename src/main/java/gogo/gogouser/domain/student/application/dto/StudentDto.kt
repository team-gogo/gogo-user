package gogo.gogouser.domain.student.application.dto

import gogo.gogouser.domain.user.persistence.Sex
import java.time.LocalDateTime

data class StudentDto(
    val userId: Long,
    val studentId: Long,
    val schoolId: Long,
    val email: String,
    val name: String,
    val deviceToken: String?,
    val sex: Sex,
    val classNumber: Int,
    val studentNumber: Int,
    val isActiveProfanityFilter: Boolean,
    val createdAt: LocalDateTime
)

data class StudentBundleDto(
    val students: List<StudentBundleInfoDto>
)

data class StudentBundleInfoDto(
    val studentId: Long,
    val schoolId: Long,
    val sex: Sex,
    val name: String,
    val deviceToken: String?,
    val classNumber: Int,
    val studentNumber: Int,
)

data class StudentSearchDto(
    val students: List<StudentSearchInfoDto>
)

data class StudentSearchInfoDto(
    val studentId: Long,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int,
)

data class StudentInfoDto(
    val studentId: Long,
    val schoolId: Long,
    val schoolName: String,
    val sex: Sex,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int,
    val isFiltered: Boolean,
)
