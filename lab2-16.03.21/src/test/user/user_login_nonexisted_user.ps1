$JSON = @'
{
    "username":"lupa@pupa.com",
    "password":"root"
}
'@

Invoke-RestMethod -Uri "http://localhost:8080/login" -Method Post -Body $JSON -ContentType "application/json"
