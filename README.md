# KaellyHorn [![Build Status](https://api.travis-ci.com/KaellyBot/KaellyHorn.svg)](https://travis-ci.com/KaellyBot/KaellyHorn) [![Known Vulnerabilities](https://snyk.io/test/github/kaellybot/kaellyhorn/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/kaellybot/kaellyhorn?targetFile=pom.xml) [![Docker Hub](https://img.shields.io/docker/v/kaellybot/kaellyhorn.svg)](https://hub.docker.com/repository/docker/kaellybot/kaellyhorn)  

KaellyHorn is a Discord bot dedicated to play nice sounds, available in FR, EN! If you have some questions, suggestions or just want to say hello, come in the support server: [![Support Server Invite](https://img.shields.io/badge/Join-KaellyBOT%20Support-7289DA.svg?style=flat)](https://discord.gg/CyJCFDk)

## Add KaellyHorn to your server:
**There is no running official instance for this bot** but there is solutions to start it on your own!

### Use Docker
[Here](https://hub.docker.com/repository/docker/kaellybot/kaellyhorn) you can find every built docker images and start a container after completing the following docker compose file:
```dockerfile
version: '2'
services:
   kaellyhorn:
      image: kaellybot/kaellyhorn:TAG
      container_name: Kaellyhorn
      ports:
      - "8080:8080"
      environment:
      - DISCORD_TOKEN=
      - LANGUAGE=
      - PREFIX=
      - SOUNDS_DIRECTORY=./kaellyhorn/sounds
      volumes:
      - ./volume:/kaellyhorn
volumes:
 kaellyhorn:
```
Pay attention that you have to provide a directory where the bot can find the sounds (in MP3 format) that will be used as a volume.

### By hour hands
If you are fed up with Docker, you can also build this project by following these steps:
1. Fill the [application.properties](./src/main/resources/application.properties). Be careful with the sounds directory, it is an external folder!
2. Build the project with the Maven command `mvn package` at the root of the project
3. Take the generated JAR file from target directory
4. Start it with the Java command `java -jar yourOwnBot.jar` and here we go!

## License  

KaellyHorn is [GPL(v3) licensed](./LICENSE).

## Partners  
[JetBrains](https://www.jetbrains.com/?from=KaellyBot) supports this project since the beginning by providing us its products!  
[![Image](https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/JetBrains_Logo_2016.svg/100px-JetBrains_Logo_2016.svg.png)](https://www.jetbrains.com/?from=KaellyBot)  


## Thank you!  

The development and the availability of KaellyBot 24/7 generate ongoing cost. Do not hesitate to help the project grow with a donation!   
[![Donate](https://www.paypalobjects.com/en_US/FR/i/btn/btn_donateCC_LG.gif)](https://www.paypal.me/kaysoro)
