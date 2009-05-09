rem mmmpass
rem Takuya Nishimoto
keytool -genkey -keystore myKeystore -alias myself
keytool -selfcert -alias myself -keystore myKeystore
keytool -list -keystore myKeystore
