# 🧠 QuizApplication  
A Java-based GUI Quiz System with Multiple Scoring Modes and User Roles

## 📝 Overview  
**QuizApplication** is a desktop-based quiz system built using Java Swing. It supports two user roles: **Admin** and **Participant**. Participants can sign up, log in, edit profiles, and engage in quizzes with flexible scoring options. Admins can add new questions and monitor participant scores. The application also tracks historical data and displays real-time scoreboards.

---

## 🌟 Features  

### 👤 User Roles
- **Participant**
  - Signup, Login, and Profile Editing
  - Choose Scoring Modes:  
    - 🟢 Simple Scoring  
    - 🔴 Negative Marking  
    - ⏱️ Time-Based Scoring  
  - Play Quizzes and View Detailed Scores
  - Review Score Breakdown and See Where Marks Were Lost
  - Track Previous Scores and Quiz History  
  - Access Scoreboard  

- **Admin**
  - Login and Access Dashboard  
  - Add Quiz Questions  
  - View All Participant Scores  

### 💻 Interface
- Clean and interactive GUI developed using Java Swing  
- Background images and styled components for improved UX  

### 🔒 Authentication & Profiles
- Username and password-based login  
- Profile management with editing options  

### 📊 Scoreboard and History
- View leaderboard rankings  
- Track and review personal or overall performance  

---

## 💻 Technology  

| Technology | Purpose                        |
|------------|--------------------------------|
| Java       | Core development language      |
| Java Swing | GUI components and interaction |
| SQL        | Backend database (via db_code.sql) |

---

## ⚙️ Functional Requirements  

### ✅ Authentication  
- Secure login for Admin and Participants  
- User credentials stored and validated via SQL  

### ✅ Quiz Operations  
- Play quiz in different scoring modes  
- Add questions (Admin only)  
- Score tracking, history viewing, scoreboard access  

---

## 📐 Non-Functional Requirements  

- **Usability**: Intuitive and responsive GUI  
- **Performance**: Fast access to quiz data and history  
- **Security**: Role-based access to features and data  
- **Scalability**: Can add more questions and scoring types easily  

---

## 🔧 Setup  

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/your-username/QuizApplication.git
   cd QuizApplication
   
2. **Database Setup**
  Open db_code.sql in your SQL client (MySQL or SQLite)
  Execute the SQL script to set up user and quiz tables

4. **Run the Application**
  Open the project in your Java IDE (e.g., IntelliJ IDEA or Eclipse)
  Run LoginForm.java 

## ▶️ Usage
- Launch application and log in as Admin or Participant
- Participants:
    - Choose a quiz mode
    - Attempt questions and view score breakdown
    - See score history and scoreboard
- Admins:
    - Add new questions
    - View results of all users
 
## 🚨 Error Handling
- **Invalid Logins**: Prompt messages for incorrect credentials
- **Empty Fields**: Prevents actions with missing data
- **Duplicate Users**: Restricts registration of existing usernames

## 🤝 Contributors
- **Trishita Umapathi** – trishitaumapathi@gmail.com
- **Deepak Velmurugan** – imdeepakv@gmail.com
- **Vandana J** – vandanaj0110@gmail.com
- **Vignesh Palanirajan** – vignesh.palanirajan31@gmail.com

