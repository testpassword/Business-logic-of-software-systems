curl -X DELETE \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTc5MDQ5OTcsImV4cCI6MTYxNzkwNTA4M30.P0yMuik2VMTeHwPqG6NfO_Doyy_otwywwyEUtLBNn4W6GGPjCT6gzQstENtKsRVwhDFs57l5hkMgpkQfSAujwQ" \
  -d '{"password": "root"}' \
  http://localhost:17502/user/delete