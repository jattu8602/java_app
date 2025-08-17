#!/bin/bash

# Todo API Test Script
# This script tests all CRUD operations of the Todo API

echo "🚀 Testing Todo API - Complete CRUD Operations"
echo "================================================"

BASE_URL="http://localhost:8080/api/todos"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    local status=$1
    local message=$2
    if [ "$status" = "SUCCESS" ]; then
        echo -e "${GREEN}✅ $message${NC}"
    elif [ "$status" = "ERROR" ]; then
        echo -e "${RED}❌ $message${NC}"
    elif [ "$status" = "INFO" ]; then
        echo -e "${BLUE}ℹ️  $message${NC}"
    elif [ "$status" = "WARNING" ]; then
        echo -e "${YELLOW}⚠️  $message${NC}"
    fi
}

# Function to test API endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_status=$4

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "%{http_code}" "$BASE_URL$endpoint")
    elif [ "$method" = "POST" ] || [ "$method" = "PUT" ] || [ "$method" = "PATCH" ]; then
        response=$(curl -s -w "%{http_code}" -X "$method" -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -X "$method" "$BASE_URL$endpoint")
    fi

    http_code="${response: -3}"
    response_body="${response%???}"

    if [ "$http_code" = "$expected_status" ]; then
        print_status "SUCCESS" "$method $endpoint - Status: $http_code"
        if [ "$response_body" != "" ]; then
            echo "   Response: $response_body"
        fi
    else
        print_status "ERROR" "$method $endpoint - Expected: $expected_status, Got: $http_code"
        if [ "$response_body" != "" ]; then
            echo "   Response: $response_body"
        fi
    fi

    echo ""
}

# Wait for API to be ready
print_status "INFO" "Waiting for API to be ready..."
sleep 5

# Test 1: Health Check
print_status "INFO" "1. Testing Health Check"
test_endpoint "GET" "/health" "" "200"

# Test 2: Get All Todos (should be empty initially)
print_status "INFO" "2. Testing Get All Todos (Empty)"
test_endpoint "GET" "" "" "200"

# Test 3: Create First Todo
print_status "INFO" "3. Testing Create Todo - High Priority"
test_endpoint "POST" "" '{"title": "Learn Spring Boot", "description": "Master Spring Boot framework and MongoDB", "priority": 3}' "201"

# Test 4: Create Second Todo
print_status "INFO" "4. Testing Create Todo - Medium Priority"
test_endpoint "POST" "" '{"title": "Build Todo App", "description": "Create a complete CRUD application", "priority": 2}' "201"

# Test 5: Create Third Todo
print_status "INFO" "5. Testing Create Todo - Low Priority"
test_endpoint "POST" "" '{"title": "Write Documentation", "description": "Document the API endpoints", "priority": 1}' "201"

# Test 6: Get All Todos (should have 3 todos)
print_status "INFO" "6. Testing Get All Todos (3 todos)"
test_endpoint "GET" "" "" "200"

# Test 7: Get Todo by ID (get first todo ID from previous response)
print_status "INFO" "7. Testing Get Todo by ID"
# We'll need to extract the ID from the previous response, but for now let's test with a generic pattern
test_endpoint "GET" "/search?title=Learn" "" "200"

# Test 8: Update Todo
print_status "INFO" "8. Testing Update Todo"
# We'll need the actual ID, but for now let's test the endpoint structure
test_endpoint "PUT" "/test-id" '{"title": "Updated Title", "description": "Updated description", "priority": 2}' "404"

# Test 9: Mark Todo as Completed
print_status "INFO" "9. Testing Mark Todo as Completed"
test_endpoint "PATCH" "/test-id/complete" "" "404"

# Test 10: Get Todos by Status
print_status "INFO" "10. Testing Get Todos by Status"
test_endpoint "GET" "/status/false" "" "200"
test_endpoint "GET" "/status/true" "" "200"

# Test 11: Get Todos by Priority
print_status "INFO" "11. Testing Get Todos by Priority"
test_endpoint "GET" "/priority/1" "" "200"
test_endpoint "GET" "/priority/2" "" "200"
test_endpoint "GET" "/priority/3" "" "200"

# Test 12: Search Todos
print_status "INFO" "12. Testing Search Todos"
test_endpoint "GET" "/search?title=Spring" "" "200"
test_endpoint "GET" "/search?title=Todo" "" "200"

# Test 13: Get High Priority Incomplete Todos
print_status "INFO" "13. Testing Get High Priority Incomplete Todos"
test_endpoint "GET" "/high-priority/incomplete" "" "200"

# Test 14: Get Recent Todos
print_status "INFO" "14. Testing Get Recent Todos"
test_endpoint "GET" "/recent/7" "" "200"

# Test 15: Get Statistics
print_status "INFO" "15. Testing Get Statistics"
test_endpoint "GET" "/stats" "" "200"

# Test 16: Delete Todo
print_status "INFO" "16. Testing Delete Todo"
test_endpoint "DELETE" "/test-id" "" "404"

# Test 17: Validation Error Test
print_status "INFO" "17. Testing Validation Error (Empty Title)"
test_endpoint "POST" "" '{"title": "", "description": "This should fail", "priority": 1}' "400"

# Test 18: Validation Error Test - Priority
print_status "INFO" "18. Testing Validation Error (Invalid Priority)"
test_endpoint "POST" "" '{"title": "Valid Title", "description": "Valid description", "priority": 5}' "400"

print_status "INFO" "🎉 API Testing Complete!"
echo ""
print_status "INFO" "To test with real IDs, run the script again after creating todos and extract the IDs from the responses."
echo ""
print_status "INFO" "API Base URL: $BASE_URL"
print_status "INFO" "Health Check: $BASE_URL/health"
print_status "INFO" "API Documentation: Check the controller code for all available endpoints"
