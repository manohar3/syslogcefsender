# syslogcefsender

**What this does:**
Sends events from cef file by replacing source user name, destination user name, source ip, target ip using events from input file

**How to Use:**
java -cp syslog-0.0.1-SNAPSHOT.jar com.mc.eventsender.syslog.CEFEventSylogSender syslogserverip=x.x.x.x port=x eps=y userscount=y hostscount=n numberofevents=x cefinputfile="ad-pxy-dataset.cef" 
