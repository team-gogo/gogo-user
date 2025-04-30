package gogo.gogouser.domain.auth.application

import gogo.gogouser.domain.auth.application.dto.AuthLoginDto
import gogo.gogouser.domain.auth.application.dto.AuthLoginReqDto
import gogo.gogouser.domain.auth.application.dto.AuthSignUpReqDto
import gogo.gogouser.domain.auth.application.dto.AuthTokenDto
import gogo.gogouser.domain.school.root.application.SchoolProcessor
import gogo.gogouser.domain.student.application.StudentValidator
import gogo.gogouser.domain.student.persistence.Student
import gogo.gogouser.domain.student.persistence.StudentRepository
import gogo.gogouser.domain.user.application.UserProcessor
import gogo.gogouser.domain.user.application.UserReader
import gogo.gogouser.domain.user.persistence.User
import gogo.gogouser.global.external.GoogleLoginFeignClientService
import gogo.gogouser.global.jwt.JwtGenerator
import gogo.gogouser.global.util.UserUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val userUtil: UserUtil,
    private val authReader: AuthReader,
    private val authProcessor: AuthProcessor,
    private val userMapper: AuthMapper,
    private val userProcessor: UserProcessor,
    private val schoolProcessor: SchoolProcessor,
    private val jwtGenerator: JwtGenerator,
    private val oauthService: GoogleLoginFeignClientService,
    private val userReader: UserReader,
    private val studentRepository: StudentRepository,
    private val studentValidator: StudentValidator,
    private val authMapper: AuthMapper,
    private val authValidator: AuthValidator
) : AuthService {

    @Transactional
    override fun login(dto: AuthLoginReqDto): AuthLoginDto {
        val email = oauthService.login(dto.oauthToken).email
        authValidator.validGSMLogin(email)
        val user = userProcessor.getUserOrCreate(email)
        authValidator.validSuspended(user)
        val tokenDto = generateToken(user)
        return authMapper.login(tokenDto, user)
    }

    @Transactional
    override fun refresh(token: String): AuthTokenDto {
        val removePrefixToken = token.replace("Bearer ", "").trim()
        val refreshToken = authReader.read(removePrefixToken)
        val user = userReader.read(refreshToken.userId)
        authValidator.validSuspended(user)
        return generateToken(user)
    }

    @Transactional
    override fun signup(dto: AuthSignUpReqDto): AuthTokenDto {
        val user = userUtil.getCurrentUser()
        val school = schoolProcessor.getSchoolOrCreate(dto)
        studentValidator.validDuplicate(school.id, dto.grade, dto.classNumber, dto.studentNumber)
        val student = Student.of(user, school, dto)
        studentRepository.save(student)
        val signedUser = userProcessor.signUp(user, dto)
        return generateToken(signedUser)
    }

    private fun generateToken(user: User): AuthTokenDto {
        val token = jwtGenerator.generateToken(
            userId = user.id.toString(),
            authority = user.authority,
        )
        authProcessor.saveRefreshToken(
            userId = user.id,
            token = token.refreshToken
        )
        return userMapper.map(token)
    }

}
