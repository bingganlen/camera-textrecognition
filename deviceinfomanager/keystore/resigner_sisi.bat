@echo off
echo jarsigner -verbose -keystore keystore�ļ�·�� -signedjar ǩ�������ɵ�apk·�� ��ǩ����apk·�� ����  

jarsigner -verbose -keystore sisi.jks -storepass dfsbLPL6wjG4t68kjd -keypass dfsbLPL6wjG4t68kjd -signedjar %~n1_signed.apk -digestalg SHA1 -sigalg MD5withRSA %~n1.apk sisi

pause