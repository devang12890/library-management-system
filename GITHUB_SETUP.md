# GitHub Setup Guide

Follow these steps to push your Library Management System to GitHub.

## Step 1: Create a GitHub Repository

1. Go to [GitHub.com](https://github.com) and sign in
2. Click the **"+"** icon in the top right corner
3. Select **"New repository"**
4. Fill in the details:
   - **Repository name**: `library-management-system` (or your preferred name)
   - **Description**: `Library Management System - RESTful API built with Spring Boot`
   - **Visibility**: Choose Public (for portfolio) or Private
   - **DO NOT** initialize with README, .gitignore, or license (we already have these)
5. Click **"Create repository"**

## Step 2: Initialize Git (if not already done)

Open terminal/command prompt in your project directory and run:

```bash
cd library-spring
git init
```

## Step 3: Add All Files

```bash
git add .
```

## Step 4: Create Initial Commit

```bash
git commit -m "Initial commit: Library Management System with Spring Boot

- RESTful API with 17+ endpoints
- Book, Member, and Loan management
- Strategy pattern for fine calculation
- Swagger API documentation
- Comprehensive testing
- Global exception handling
- Bean validation
- JPA integration with H2 database"
```

## Step 5: Add Remote Repository

Replace `YOUR_USERNAME` with your GitHub username:

```bash
git remote add origin https://github.com/YOUR_USERNAME/library-management-system.git
```

Or if you prefer SSH:

```bash
git remote add origin git@github.com:YOUR_USERNAME/library-management-system.git
```

## Step 6: Push to GitHub

```bash
git branch -M main
git push -u origin main
```

## Step 7: Add Repository Description

1. Go to your repository on GitHub
2. Click the **⚙️ Settings** icon (or go to repository settings)
3. Scroll down to **"About"** section
4. Add description: `Library Management System - RESTful API built with Spring Boot`
5. Add topics: `java`, `spring-boot`, `rest-api`, `jpa`, `swagger`, `library-management`, `maven`

## Step 8: Update README (Optional)

Your README.md is already comprehensive! You can add a badge at the top:

```markdown
# Library Management System

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue)
```

## Step 9: Create a Release (Optional but Recommended)

1. Go to your repository
2. Click **"Releases"** → **"Create a new release"**
3. Tag version: `v1.0.0`
4. Release title: `Library Management System v1.0.0`
5. Description:
   ```
   Initial release of Library Management System
   
   Features:
   - RESTful API with 17+ endpoints
   - Book, Member, and Loan management
   - Strategy pattern for fine calculation
   - Swagger API documentation
   - Comprehensive testing
   ```

## Troubleshooting

### If you get "remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/YOUR_USERNAME/library-management-system.git
```

### If you need to update existing repository
```bash
git add .
git commit -m "Update: Enhanced project with comprehensive features"
git push origin main
```

### If you need to force push (use carefully)
```bash
git push -u origin main --force
```

## Quick Commands Summary

```bash
# Initialize (if needed)
git init

# Add files
git add .

# Commit
git commit -m "Your commit message"

# Add remote (first time only)
git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git

# Push
git push -u origin main
```

## Next Steps After Pushing

1. ✅ Add the GitHub link to your resume
2. ✅ Update your LinkedIn profile with the project
3. ✅ Be ready to explain the project in interviews
4. ✅ Consider adding more features to show continuous improvement

## GitHub Repository Badge (Add to README)

After pushing, you can add this to your README:

```markdown
[![GitHub](https://img.shields.io/badge/GitHub-Repository-blue)](https://github.com/YOUR_USERNAME/library-management-system)
```

Good luck with your job search! 🚀
