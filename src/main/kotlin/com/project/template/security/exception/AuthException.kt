package com.project.template.security.exception

import com.project.template.security.exception.enum.AuthFailEnum

class AuthException(message: String) : RuntimeException(message) {
    constructor(enum: AuthFailEnum) : this(enum.buildMessage())
}