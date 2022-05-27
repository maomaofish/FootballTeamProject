# FootballTeamProject
A REST API  service to create football teams and will be able to sell or buy players with JWT authentication
This is a project only provid APIs to customers. 
1. signup:  POST http://localhost:8080/api/signup , no need to use any authentication, anonymous users can use this.
            body :
            {
              "email": "newLina11@gmail.com",
              "firstName": "tester1",
              "lastName": "Last" ,
              "password": "root",
              "teamId": 0
             }
             response: 
             {
                "email": "newLina12@gmail.com",
                "firstName": "tester1",
                "lastName": "Last",
                "teamName": "Team Name"
             }
             
 2. User login : Get http://localhost:8080/api/login, should use your registered email and password(basic Auth) to anthenticate.
                 response:
                 {
                    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJGb290YmFsbCBUZWFtIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJuZXdMaW5hMTJAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjUzNjcxNzEzLCJleHAiOjE2NTM2NzQ3MTN9.QWC2_8KTcHop_Y_e0OzeneVIhZsHEyteEHrUcfF4Sps"
                 }
3. Get your team and players info: Get http://localhost:8080/api/user_info, need use the token you got.
4. Get all free players in the market who are not belong to any team : Get http://localhost:8080/api/market_players JWT needed.
5. Buy Player in market: Put http://localhost:8080/api/market_players/102 JWT needed.
6. Change one of your own player info: Patch http://localhost:8080/api/players/102 JWT needed.
