# Quizlet Web Service

A simple web service akin to "Quizlet," providing an interactive quiz-taking experience for users and administrative insights for management. This application leverages **Server-Side Rendering (SSR)** with **JavaServer Pages (JSP)** for dynamic content generation, utilizes **MySQL** as its database, and connects using **raw JDBC**.

---

## ✨ Features

This web service offers distinct functionalities for both users and administrators:

### For Users:

* **Quiz Initiation:** Users can start a new quiz session at any time.
* **Random Question Assignment:** Each quiz dynamically assigns three random questions from the database.
* **Timed Quizzes:** Every quiz session has a strict 5-minute time limit.
* **Live Countdown:** A clear countdown timer is displayed on the quiz-taking page, helping users manage their time effectively.
* **Quiz Submission:** Users can submit their answers within the time limit.

### For Administrators:

* **Comprehensive Quiz Overview:** Admins can view a list of all quizzes taken by users.
* **Quiz Statistics:** Access to basic statistics related to quiz performance and completion. (e.g., total quizzes taken, average scores, etc.)

---

## 🛠️ Tech Stack

* **Backend Language:** Java
* **Server-Side Rendering:** JavaServer Pages (JSP)
* **Web Server/Servlet Container:** (e.g., Apache Tomcat - *you might want to specify this if you use one*)
* **Database:** MySQL
* **Database Connectivity:** Raw JDBC (Java Database Connectivity)
* **Build Tool:** Maven

---

## 📂 Project Structure

├── src/  
│   ├── main/  
│   │   ├── java/                   # Java source code (Servlets, DAOs, Models)  
│   │   │   └── com/yourcompany/quizlet/  
│   │   │       ├── controller/     # Servlets handling requests  
│   │   │       ├── service/        # Services handling the business logic  
│   │   │       ├── dao/            # Data Access Objects (JDBC logic)  
│   │   │       ├── domain/         # Java POJOs (Quiz, Question, User etc.)  
│   │   │       ├── dto/            # Data Transfer Objects  
│   │   │       └── orm/            # Object RowMappers  
│   │   ├── webapp/                 # Web application root  
│   │   │   ├── WEB-INF/  
│   │   │   │   ├── web.xml         # Deployment Descriptor  
│   │   │   │   └── lib/            # External JARs (e.g., MySQL JDBC Driver)  
│   │   │   ├── jsp/                # JSP files (e.g., quiz.jsp, admin.jsp)  
│   │   │   └── css/                # CSS files  
│   │   │   └── js/                 # JavaScript files (for countdown, form handling)  
│   └── test/  
├── pom.xml (or build.gradle)       # Build configuration  
└── README.md  
---

## 🚀 Getting Started

Follow these instructions to set up and run the Quizlet web service on your local machine.

### Prerequisites

* **Java Development Kit (JDK) 8 or higher**
* **Apache Maven** or **Gradle** (if using a build tool)
* **MySQL Server** (version 8.0 recommended)
* **Apache Tomcat** (or another Servlet Container like Jetty)

### Database Setup

1.  **Create a MySQL Database:**

2.  **Create Tables and Insert Sample Data:**
    You will need to create tables for users, questions, quizzes, and quiz attempts. (It's highly recommended to include your SQL schema in a `schema.sql` file in the project).

3.  **Update Database Configuration:**
    Locate your database connection details in your Java code (e.g., a `DBConnection.java` or `AppConfig.java` file within your `dao` or `util` package) and update them to match your MySQL setup.

### Building and Deployment

1.  **Build the Project:**
    Navigate to the project's root directory in your terminal and build the WAR file.

    *(For Maven)*
    ```bash
    mvn clean install
    ```
    This will generate a `.war` file (e.g., `quizlet.war`) in your `target/` or `build/libs/` directory.

2.  **Deploy to Tomcat (or preferred Servlet Container):**
    * Copy the generated `.war` file to the `webapps` directory of your Apache Tomcat installation.
    * Start or restart your Tomcat server.

### Accessing the Application

Once deployed and Tomcat is running, open your web browser and navigate to:

* **User Interface:** `http://localhost:8080/quizlet/` (or whatever context path your WAR file deploys to)

---

## 🔒 Security Notes

* **Password Storage:** Ensure all user passwords in the `users` table are stored as **hashed values** (e.g., using BCrypt). Do **not** store plain text passwords.
* **JDBC Security:** While raw JDBC is used, be mindful of **SQL injection vulnerabilities**. Always use **`PreparedStatement`** with parameterized queries to prevent this.
* **Session Management:** For production, secure session management (e.g., using secure cookies, invalidating sessions on logout) is crucial.

---

## 📄 License

MIT, Apache 2.0
