curl -X DELETE \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTQzNDQ2MzYsImV4cCI6MTYxNDk0OTQzNn0.sYjsHw3DHluZYmZkIyR5WOCI0pdyTl_wvAPuiWIzuoev79iZe3gywz7o9cHoPNywvZ2N54AwBLweSjk4uFK_bw" \
  -d '{"password": "root"}' \
  http://localhost:8080/user/delete