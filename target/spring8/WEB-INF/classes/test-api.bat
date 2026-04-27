@echo off
echo === Comprehensive REST API CRUD Test ===

echo 1. GET all bicycles (initial state):
curl http://localhost:8080/api/bicycles

echo.
echo 2. CREATE new bicycle:
curl -v -X POST -H "Content-Type: application/json" -u admin:qwerty -d "{\"model\":\"CRUD Test Bike\",\"producer\":\"Test\",\"producingCountry\":\"Test\",\"gearsNum\":24,\"cost\":750}" http://localhost:8080/api/bicycles

echo.
echo 3. READ - Verify creation:
curl http://localhost:8080/api/bicycles

echo.
echo 4. UPDATE - Note the ID from above and update:
set /p update_id="Enter ID to update: "
curl -v -u admin:qwerty -X PUT -H "Content-Type: application/json" -d "{\"model\":\"Updated Bike\",\"producer\":\"Updated\",\"producingCountry\":\"Updated\",\"gearsNum\":27,\"cost\":1000}" http://localhost:8080/api/bicycles/%update_id%

echo.
echo 5. Verify update:
curl http://localhost:8080/api/bicycles/%update_id%

echo.
echo 6. DELETE the test bicycle:
curl -v -u admin:qwerty -X DELETE http://localhost:8080/api/bicycles/%update_id%

echo.
echo 7. Final verification:
curl http://localhost:8080/api/bicycles

echo.
echo === CRUD Test completed ===
pause