set native_directory=android
set default_directory=default
set destination_ANE=..\..\AirTestProject\ane\MediaPlayerAne.ane
set extension_XML=extension.xml
set platform_XML=platform.xml
set android_Res_Dir=res
set library_SWC=As3Code.swc
set library_JAR=MediaPlayerAne.jar
set library_SWF=library.swf
adt -package -target ane "%destination_ANE%" "%extension_XML%" -swc "%library_SWC%" -platform Android-ARM -platformoptions "%platform_XML%" "%android_Res_Dir%" -C "%native_directory%" "%library_SWF%" "%library_JAR%" -platform default -C "%default_directory%" "%library_SWF%"
