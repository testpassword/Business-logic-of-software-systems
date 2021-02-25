curl -X POST \
  -H "Content-Type:application/json" \
  -d '{"advertsIds": [1, 3, 5]}' \
  http://localhost:8080/advert/getFiltered