# CHAT
Spring websocket chat

Create database "chat" with PostgreSQL in localhost:5432

Run the server with maven and visit localhost:9999

You may create new user or login by exists one (test1, test2, tes3).
Login and password are same

Supported commands:  
//room create "roomName"  
//room connect "roomName" -l "login"  
//room delete "roomName"  
//room rename "roomNewName"  
//room disconnect "roomName" -l "login"  