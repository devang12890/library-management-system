#!/bin/bash

echo "========================================"
echo "Pushing Library Management System to GitHub"
echo "========================================"
echo ""

echo "Step 1: Adding all files..."
git add .
echo ""

echo "Step 2: Committing changes..."
git commit -m "Enhanced: Library Management System with comprehensive features

- Added RESTful API with 17+ endpoints
- Implemented Book, Member, and Loan management
- Added Strategy pattern for fine calculation
- Integrated Swagger API documentation
- Added comprehensive testing (Unit, Integration, Controller)
- Implemented global exception handling
- Added Bean Validation
- Created DTOs for clean API design
- Added pagination support
- Integrated JPA with H2 database
- Added comprehensive documentation"
echo ""

echo "Step 3: Pushing to GitHub..."
git push origin main
echo ""

echo "========================================"
echo "Done! Check your GitHub repository"
echo "========================================"
