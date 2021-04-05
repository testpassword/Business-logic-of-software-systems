curl -X DELETE \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTc2MzExMzQsImV4cCI6MTYxODIzNTkzNH0.GyDXrxSh0SetcK4K_NL8zMcQ8-13YuSj-Y0cVjzSsBXyx-zlIYkEe9T8fOIcqAi-8P0t6aIRIHbzQW8dzyf3lg" \
  -d '{"password": "root"}' \
  http://localhost:17502/user/delete