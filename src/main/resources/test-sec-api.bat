@echo off
echo === Testing Security Restrictions ===

echo 1. Try to DELETE without authentication (should fail):
curl -v -X DELETE http://localhost:8080/api/bicycles/1

echo.
echo 2. Try to DELETE with user role (should fail):
curl -v -u user:password -X DELETE http://localhost:8080/api/bicycles/1

echo.
echo 3. Try to DELETE with admin role (should succeed for existing ID):
curl -v -u admin:qwerty -X DELETE http://localhost:8080/api/bicycles/1

echo.
echo === Security Test completed ===
pause