package gogo.gogouser.domain.auth.application

import gogo.gogouser.domain.user.persistence.User
import gogo.gogouser.global.error.UserException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class AuthValidator {

    fun validGSMLogin(email: String) {
        val regex = Regex("^[A-Za-z0-9._%+-]+@gsm\\.hs\\.kr$")

        if (!regex.matches(email)) {
            throw UserException("요청한 이메일이 GSM 학생용 이메일이 아닙니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

    fun validSuspended(user: User) {
        if (user.isSuspended) {
            throw UserException("차단된 사용자입니다.", HttpStatus.FORBIDDEN.value())
        }
    }

}
