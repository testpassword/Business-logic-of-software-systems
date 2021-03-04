curl -X DELETE \
  -H "Content-Type:application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZWtAbG9sLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MTQ4MDczMjUsImV4cCI6MTYxNTQxMjEyNX0.OOJaokfSlWtBHmV6khx-LgI_CUNQElkXSUuUE2oG3EzrGTqVd6zHC3A0dnq4Pj9Q2HTJ_BS46LnhY75UcW5L5w" \
  -d '{"password": "root"}' \
  http://localhost:8080/user/delete