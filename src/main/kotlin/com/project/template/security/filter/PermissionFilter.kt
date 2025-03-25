//package com.project.template.security.filter
//
//import com.project.template.common.cache.CacheManager
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.stereotype.Component
//import org.springframework.util.AntPathMatcher
//import org.springframework.web.filter.OncePerRequestFilter
//
///**
// * security过滤器链
// * 包含了一系列登录与权限的校验
// * TODO date: 2025年3月21日    description: 说明待补充
// */
//@Component
//open class PermissionFilter(
//    cacheManager: CacheManager
//) : OncePerRequestFilter() {
//
//    private val matcher = AntPathMatcher()
//
//    private val cacheTemplate = cacheManager.createTemplate()
//
//    /**
//     * 核心过滤方法
//     */
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//        filterChain.doFilter(request, response)
//    }
//
//}