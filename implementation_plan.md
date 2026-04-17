# 🛡️ SocialConnect - Spring Security Demo App

Ứng dụng mạng xã hội đơn giản hiển thị rõ kiến trúc **MVC** và các tính năng **Spring Security** phục vụ thuyết trình.

## Mục tiêu
Xây dựng app `SocialConnect` bằng **Spring Boot + Spring Security + Thymeleaf** với H2 in-memory database để không cần cài đặt thêm.

---

## Kiến trúc MVC rõ ràng

```
                      REQUEST
                         │
                    ┌────▼────┐
                    │ Security│   ← Spring Security Filter Chain
                    │  Filter │   (Authentication, Authorization, CSRF)
                    └────┬────┘
                         │
                    ┌────▼────────────┐
              ┌─────│   CONTROLLER   │─────┐
              │     │  (C in MVC)    │     │
              │     └────┬───────────┘     │
              │          │ calls           │
              │     ┌────▼────────────┐    │
              │     │    SERVICE      │    │
              │     │ (Business Logic)│    │
              │     └────┬────────────┘    │
              │          │ CRUD            │
              │     ┌────▼────────────┐    │
              │     │   REPOSITORY    │    │
              │     │ (Data Access)   │    │
              │     └────┬────────────┘    │
              │          │                 │
              │     ┌────▼────────────┐    │
              │     │     MODEL       │    │  → Thymeleaf VIEW
              │     │  (M in MVC)    │    │    (V in MVC)
              │     └─────────────────┘    │
              └────────────────────────────┘
```

---

## Tính năng Spring Security được demo

| Tính năng | Mô tả |
|-----------|-------|
| **Authentication** | Login form tùy chỉnh, UserDetailsService |
| **Authorization** | Role-based: ROLE_USER, ROLE_ADMIN |
| **Password Encoding** | BCryptPasswordEncoder |
| **CSRF Protection** | Tự động với Thymeleaf |
| **Remember Me** | "Ghi nhớ đăng nhập 7 ngày" |
| **Session Management** | Giới hạn concurrent sessions |
| **Custom Login/Logout** | Trang login đẹp, redirect sau logout |
| **Method Security** | `@PreAuthorize` trên Service |
| **H2 Console Secured** | Cấu hình bảo vệ dev tool |

---

## Cấu trúc Project

```
Anti/
├── pom.xml
└── src/main/
    ├── java/com/social/anti/
    │   ├── AntiApplication.java          ← Entry point
    │   │
    │   ├── config/
    │   │   └── SecurityConfig.java       ← Spring Security Configuration
    │   │
    │   ├── model/                        ← M (Model)
    │   │   ├── User.java
    │   │   ├── Role.java
    │   │   └── Post.java
    │   │
    │   ├── repository/                   ← Data Access Layer
    │   │   ├── UserRepository.java
    │   │   ├── RoleRepository.java
    │   │   └── PostRepository.java
    │   │
    │   ├── service/                      ← Business Logic Layer
    │   │   ├── UserService.java
    │   │   ├── PostService.java
    │   │   └── CustomUserDetailsService.java ← UserDetailsService impl
    │   │
    │   └── controller/                   ← C (Controller)
    │       ├── HomeController.java
    │       ├── AuthController.java
    │       ├── PostController.java
    │       └── AdminController.java
    │
    └── resources/
        ├── application.properties
        ├── data.sql                      ← Sample data
        └── templates/                    ← V (View - Thymeleaf)
            ├── layout/
            │   └── base.html
            ├── auth/
            │   ├── login.html
            │   └── register.html
            ├── home/
            │   └── feed.html
            ├── post/
            │   └── create.html
            └── admin/
                └── dashboard.html
```

---

## Proposed Changes

### [NEW] pom.xml
### [NEW] SecurityConfig.java - Cấu hình Security Filter Chain
### [NEW] Model classes (User, Role, Post)
### [NEW] Repository interfaces (JPA)
### [NEW] Service + CustomUserDetailsService
### [NEW] Controllers (Home, Auth, Post, Admin)
### [NEW] Thymeleaf templates (login đẹp, feed, admin dashboard)
### [NEW] application.properties + data.sql

---

## Verification Plan
- Chạy `mvn spring-boot:run` 
- Truy cập http://localhost:8080
- Kiểm tra login với user/admin
- Kiểm tra phân quyền ROLE_ADMIN vs ROLE_USER
- Demo các tính năng security
