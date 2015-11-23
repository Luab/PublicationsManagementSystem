README.txt

This is README for PMS project.
Requirments:
1. Tomcat server 7 or above
2. Java 7 or above
3. PostgreSQL 9.1
4. IDE for Java (optional)

Simple Setup guide:
1. Ensure you have sucessfully installed our dump
2. Make sure that your DB has user psm with password "PSM" and All privileges on everything in database (Super user privileges should be fine too)
3. Deploy PMS.war to your Tomcat server
4. Enjoy

Hard Setup guide:
1. Ensure you have sucessfully installed our dump
2. Open source code.
3. Insert your DB user credentials into ru.pumas.DBVredentials
4. Insert your Telegram Bot token into bot.BuildVars
5. (optional) configure your bot settings (such as sleep value) in bot.Bot
6. Compile your project into war file
7. Don't forget to create cron script to run EverydayParser everyday
8. Deploy your war file to Tomcat server
