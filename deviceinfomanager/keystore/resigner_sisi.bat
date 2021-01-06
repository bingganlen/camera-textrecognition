@echo off
echo jarsigner -verbose -keystore keystore文件路径 -signedjar 签名后生成的apk路径 待签名的apk路径 别名  

jarsigner -verbose -keystore sisi.jks -storepass dfsbLPL6wjG4t68kjd -keypass dfsbLPL6wjG4t68kjd -signedjar %~n1_signed.apk -digestalg SHA1 -sigalg MD5withRSA %~n1.apk sisi

pause