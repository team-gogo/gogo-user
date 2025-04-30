package gogo.gogouser.domain.student.application

import gogo.gogouser.domain.auth.application.AuthValidator
import gogo.gogouser.domain.student.application.dto.StudentBundleDto
import gogo.gogouser.domain.student.application.dto.StudentInfoUpdateDto
import gogo.gogouser.domain.student.application.dto.StudentSearchDto
import gogo.gogouser.domain.student.persistence.Student
import gogo.gogouser.domain.user.application.UserReader
import gogo.gogouser.global.util.UserUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentServiceImpl(
    private val studentReader: StudentReader,
    private val studentMapper: StudentMapper,
    private val userUtil: UserUtil,
    private val studentProcessor: StudentProcessor,
    private val studentValidator: StudentValidator,
    private val userReader: UserReader,
    private val authValidator: AuthValidator
) : StudentService {

    @Transactional(readOnly = true)
    override fun queryByUserId(userId: Long): Student {
        val user = userReader.read(userId)
        authValidator.validSuspended(user)
        val student = studentReader.readByUserId(userId)
        return student
    }

    @Transactional(readOnly = true)
    override fun queryBundle(studentIds: List<Long>): StudentBundleDto {
        val students = studentReader.readByIds(studentIds)
        return studentMapper.mapStudents(students)
    }

    @Transactional(readOnly = true)
    override fun search(name: String): StudentSearchDto {
        val userId = userUtil.getCurrentUserId()
        val student = studentReader.readByUserId(userId)
        val searchStudents = studentReader.search(student.school.id, name)
        return StudentSearchDto(searchStudents)
    }

    @Transactional
    override fun update(userId: Long, dto: StudentInfoUpdateDto) {
        val student = studentReader.readByUserId(userId)
        studentValidator.validUpdate(student, dto.grade, dto.classNumber, dto.studentNumber)
        studentProcessor.update(student, dto)
    }

}
