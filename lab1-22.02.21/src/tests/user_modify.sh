curl -X PATCH \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTQ4MTAwMjUsImV4cCI6MTYxNTQxNDgyNX0.1BgERoVQj1Ov3za04WqhFjeFnpApo4Dr58FKxWLrVzLL1a6yUIm-gX3lJYbOTthGIxa4cky25SwwmGVJ8j-uyw" \
  -d '{"name": "tester"}' \
  http://localhost:8080/user/modify