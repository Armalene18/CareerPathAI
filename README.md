# CareerPath AI

**1. Project Overview**

CareerPath AI is an Android mobile application designed to help users explore suitable career options based on their interests, skills and preferences.

The application provides a short career assessment and uses the user's answers to calculate suitable career options. The prototype also includes user registration and login, application settings, notifications, dark mode, REST API integration and database connectivity.

The project was developed as part of the App Prototype Development assessment for the Diploma in Information Technology (Software Development).


 **2. Project Objectives**

The main objectives of CareerPath AI are to:

* Help users identify possible career paths.
* Provide a simple and user-friendly mobile interface.
* Allow users to register and securely log in.
* Connect the Android application to a RESTful API.
* Store user information in a SQL Server database.
* Protect user passwords using secure password hashing.
* Validate user input and handle invalid information without crashing.
* Apply unit testing to important application functionality.
* Use GitHub for source-code management.
* Use GitHub Actions for automated testing and application building.


## 3. Main Features

 **User Registration**

Users can create an account by providing:

* Full name
* Email address
* Password
* Password confirmation

The application validates the information before sending the registration request to the API.

### User Login

Registered users can log into the application using their email address and password.

The Android application communicates with the ASP.NET Core REST API using Retrofit.

### Secure Password Storage

Passwords are not stored as plain text.

The backend uses ASP.NET Core's `PasswordHasher` to create a secure password hash before storing the user's password information in SQL Server.

During login, the submitted password is verified against the stored hash.

### Career Assessment

Users complete a four-question career assessment.

The answers are processed by the application and used to calculate career suitability scores.

The prototype currently provides career options including:

* Software Developer
* Data Analyst
* Business Analyst

### Career Results

After completing the assessment, the application displays the calculated career results.

The results are ordered according to the calculated score.

### Settings

The application includes a settings screen where users can manage:

* Notifications
* Dark Mode

Users can also return to the dashboard or log out.

### REST API Integration

The Android application connects to an ASP.NET Core REST API.

The API provides endpoints for:

* User registration
* User login

Retrofit is used in the Android application to communicate with the REST API.

### Database

The backend uses Microsoft SQL Server to store registered user information.

The database stores information such as:

* User ID
* Full name
* Email address
* Password hash

### Input Validation

The application validates important user input such as:

* Empty fields
* Invalid email addresses
* Password requirements
* Password confirmation
* Invalid login details

API responses such as unsuccessful login, duplicate registration and invalid requests are handled without crashing the application.


## 4. Technologies Used

### Android Application

* Kotlin
* Android Studio
* Jetpack Compose
* Material Design
* Retrofit
* Kotlin Coroutines

### Backend

* C#
* ASP.NET Core Web API
* Entity Framework Core
* ASP.NET Core PasswordHasher

### Database

* Microsoft SQL Server
* SQL Server Management Studio (SSMS)

### Testing and Development

* JUnit
* Git
* GitHub
* GitHub Actions
* Android Studio


## 5. Application Architecture

The project uses a client-server architecture.

The main communication flow is:

**Android Application → Retrofit → ASP.NET Core REST API → Entity Framework Core → SQL Server**

For example, during registration:

1. The user enters their details in the Android application.
2. The Android application validates the input.
3. Retrofit sends a POST request to the REST API.
4. The ASP.NET Core API validates the request.
5. The password is securely hashed.
6. The user is stored in SQL Server.
7. The API returns a response to the Android application.
8. The application displays the appropriate result to the user.


## 6. API Endpoints

### Register

**POST**

`/api/Auth/register`

Used to create a new user account.

### Login

**POST**

`/api/Auth/login`

Used to authenticate an existing user.

The API returns information such as:

* Login message
* User ID
* Full name
* Email address


## 7. Security

Security was considered during the development of the prototype.

The application does not store passwords as plain text.

The ASP.NET Core backend uses `PasswordHasher<User>` to hash passwords during registration.

During authentication, the password entered by the user is checked against the stored password hash.

The application also avoids writing passwords to the Android log output.


## 8. Unit Testing

Unit testing was implemented using JUnit.

The current test suite includes five tests.

The tests verify:

1. Three career options are returned.
2. Programming answers increase the Software Developer score.
3. Data-related answers increase the Data Analyst score.
4. Business-related answers increase the Business Analyst score.
5. Career results are sorted from the highest score to the lowest score.

All five unit tests passed successfully during development.


## 9. GitHub Actions

GitHub Actions was implemented to automate the project testing and build process.

The workflow is called:

**Android CI**

The workflow automatically:

1. Checks out the project.
2. Sets up JDK 17.
3. Sets up Gradle.
4. Runs the unit tests.
5. Builds the Android debug APK.

The workflow completed successfully with a green build status.

This provides automated verification that the project can be tested and built after changes are pushed to GitHub.


## 10. GitHub Repository

The complete source code is available on GitHub:

**CareerPath AI Repository**

https://github.com/Armalene18/CareerPathAI

The repository contains the Android source code, testing files, Gradle configuration and GitHub Actions workflow.


## 11. How to Run the Project

### Requirements

The following software is required:

* Android Studio
* JDK 17
* Android SDK
* Internet connection
* Access to the CareerPath AI REST API

### Steps

1. Clone the repository from GitHub.
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Connect an Android device or start an Android emulator.
5. Run the application from Android Studio.
6. Register a new account.
7. Log in using the registered account.
8. Complete the career assessment.
9. View the career recommendations.
10. Test the settings and logout functionality.


## 12. Project Structure

The project contains the main Android application module:

`app`

Important components include:

* MainActivity
* API models
* Authentication API service
* Retrofit client
* Unit tests
* Android resources
* Gradle configuration
* GitHub Actions workflow


## 13. Error Handling

The application handles common errors including:

* Empty registration fields
* Invalid email addresses
* Incorrect passwords
* Duplicate email registration
* Invalid login credentials
* API request failures

Error messages are displayed to the user instead of allowing the application to crash.


## 14. User Interface Design

CareerPath AI uses a modern mobile interface designed using Jetpack Compose.

The application uses a blue-based visual design with clear navigation between:

* Login
* Registration
* Dashboard
* Career Assessment
* Career Results
* Settings

The interface was designed to be simple enough for users to complete the assessment without unnecessary navigation.


## 15. Demonstration Video

A demonstration video will be added to this section.

The video will demonstrate:

* User registration
* Login
* Password security
* Dashboard
* Career assessment
* Career results
* Settings
* Notifications
* Dark mode
* REST API communication
* Database information
* Error handling
* Application functionality

**Video link:** To be added before final submission.


## 16. Screenshots

Screenshots of the application will be added here to demonstrate the implemented features.

Recommended screenshots include:

1. Login screen
2. Registration screen
3. Dashboard
4. Career Assessment
5. Career Results
6. Settings
7. SQL Server user records
8. Swagger API registration/login
9. GitHub Actions successful build
10. GitHub repository


## 17. Version Control

Git and GitHub were used to manage the project source code.

The project repository contains the complete source code and development files.

GitHub Actions was also connected to the repository to automatically test and build the application when changes are pushed.


## 18. AI Usage

Artificial intelligence tools were used during development as a support resource for troubleshooting, code explanations, debugging and improving the structure of the project.

AI assistance was used as a development aid, while the final implementation was reviewed, tested and adapted to the requirements of the CareerPath AI application.

The developer remains responsible for understanding and testing the submitted implementation.


## 19. Conclusion

CareerPath AI provides a working prototype for career exploration through a mobile career assessment.

The prototype demonstrates Android development using Kotlin and Jetpack Compose, RESTful API integration, SQL Server database connectivity, secure password hashing, input validation, unit testing, version control and automated GitHub Actions builds.

The project demonstrates the integration of frontend mobile development with a backend API and database to create a functional software solution.
