package io.realworld.web

import io.realworld.exception.InvalidException
import io.realworld.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest


/**
 * Generates an error with the following format:
 *
<pre>
{
"errors":{
"body": [
"can't be empty"
]
}
}
</pre>
 */
@Component
@RestControllerAdvice
class InvalidRequestHandler(val userService: UserService) {
    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun processValidationError(ex: InvalidException): Any {
        val errors = mutableMapOf<String, MutableList<String>>()
        ex.errors?.fieldErrors?.forEach {
            if (errors.containsKey(it.field))
                errors.get(it.field)!!.add(it.defaultMessage)
            else
                errors.put(it.field, mutableListOf(it.defaultMessage))
        }
        return mapOf("errors" to errors)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleError404(request: HttpServletRequest, e: Exception): ModelAndView {
        val mav = ModelAndView(if (userService.currentUser.get() == null) "/auth" else "/")
        mav.addObject("exception", e)
        return mav
    }
}