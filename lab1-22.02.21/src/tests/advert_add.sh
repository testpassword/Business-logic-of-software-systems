curl -X PUT \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTQ4MTU3ODAsImV4cCI6MTYxNTQyMDU4MH0.JlSMytJy4zYDITmUfVu4iJn6Tll9hqPEeiKEmJon61EzlVMhEJObhtpmQJLwfK4zOb_56dxjfejoWG593755xQ" \
  -d '{"cost": "10000", "location": "SPb", "quantityOfRooms": "3", "area": "86", "name": "Good flat", "mobileNumber": "88005553535"}' \
  http://localhost:8080/advert/add