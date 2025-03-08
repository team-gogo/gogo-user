package gogo.gogouser.domain.student.application

import gogo.gogouser.domain.student.application.dto.StudentBundleDto
import gogo.gogouser.domain.student.application.dto.StudentDto
import gogo.gogouser.domain.student.application.dto.StudentInfoDto
import gogo.gogouser.domain.student.persistence.Student
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun map(student: Student): StudentDto {
        val user = student.user
        val school = student.school

        return StudentDto(
            studentId = student.id,
            userId = user.id,
            schoolId = school.id,
            email = user.email,
            name = user.name!!,
            deviceToken = student.deviceToken,
            sex = user.sex!!,
            classNumber = student.classNumber,
            studentNumber = student.studentNumber,
            isActiveProfanityFilter = student.isActiveProfanityFilter,
            createdAt = user.createdAt
        )
    }

    fun mapStudents(students: List<Student>) =
        StudentBundleDto (
            students = students.map {
                StudentInfoDto(
                    studentId = it.id,
                    schoolId = it.school.id,
                    sex = it.user.sex!!,
                    name = it.user.name!!,
                    deviceToken = it.deviceToken,
                    classNumber = it.classNumber,
                    studentNumber = it.studentNumber,
                )
            }
        )
}
