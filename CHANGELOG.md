/**
 * CHANGELOG - Study Buddy
 * 
 * Version 1.0.0 - Initial Release
 * ================================
 * 
 * Features:
 * - User Registration (/api/auth/register)
 * - User Login (/api/auth/login)
 * - Profile Creation and Update (/api/profile)
 * - Profile Retrieval (/api/profile/{userId})
 * - Responsive UI with Light Green Theme (#90EE90)
 * - Session-based Authentication with localStorage
 * - Message Notifications System
 * - H2 Database Integration
 * - CORS Support
 * 
 * Backend:
 * - Spring Boot 3.1.5
 * - JPA/Hibernate with H2 Database
 * - REST API Controller Pattern
 * - Service and Repository Layers
 * - Input Validation
 * - Logging Support
 * 
 * Frontend:
 * - Vite Build Tool
 * - Vanilla JavaScript (ES6 Modules)
 * - Responsive CSS Grid/Flexbox Layout
 * - Fetch API for REST Calls
 * - Session Management
 * 
 * Database:
 * - H2 In-Memory/File Mode
 * - Automatic Schema Creation
 * - User and Profile Entities
 * 
 * 
 * Known Limitations:
 * - Passwords stored in plain text (use BCrypt in production)
 * - No JWT implementation
 * - Session based on localStorage
 * - Email verification not implemented
 * 
 * Future Enhancements:
 * - Password hashing with BCrypt
 * - JWT token authentication
 * - Email verification
 * - Password reset functionality
 * - Profile picture upload
 * - User search
 * - Friend system
 * - Messaging system
 * - Study groups
 * 
 * Installation & Running:
 * 
 * 1. Backend:
 *    cd backend
 *    mvn clean install
 *    mvn spring-boot:run
 *    Runs on: http://localhost:8080
 * 
 * 2. Frontend:
 *    cd frontend
 *    npm install
 *    npm run dev
 *    Runs on: http://localhost:5173
 * 
 * Testing:
 * 1. Register new user
 * 2. Login with registered credentials
 * 3. Create/Update profile
 * 4. View profile data
 * 5. Logout
 */

