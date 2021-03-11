curl -X DELETE \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTQzNDQyMzgsImV4cCI6MTYxNDk0OTAzOH0.pgDyozBiuev_fduTouBh9d2DVHLzvqwP7l2zS-Fm817-PfuyGfTO8R1QFNdr4IpgfPfQFEZmxSp_Za5Vd9cd8A" \
  -d '{"advertsIds": [1, 3, 5]}' \
  http://localhost:17502/advert/delete