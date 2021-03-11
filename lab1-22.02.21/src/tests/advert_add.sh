curl -X PUT \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTUxMTA4NTgsImV4cCI6MTYxNTcxNTY1OH0.iNf0VzmSHtU51sc_P9xXDNTROW2-gpfzIlF4eIdoQxl1A38rymG-ue-onVJonnKfcDCF-w5agqU5FmPsgkH8FA" \
  -d '{"cost": "10000", "location": "SPb", "quantityOfRooms": "3", "area": "86", "name": "Good flat", "mobileNumber": "88005553535"}' \
  http://localhost:17502/advert/add