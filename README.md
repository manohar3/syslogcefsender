# syslogcefsender

**What this does:**
Sends events from cef file by replacing source user name, destination user name, source ip, target ip using events from input file

- eps- Number of events to send
- usercount- Total number distinct users for whom events will get genereted
- hostscount- Total number distinct hosts for whom events will get generated

### Command to send specific numberofevents in real time:
java -cp syslog-0.0.1-SNAPSHOT.jar com.mc.eventsender.syslog.CEFEventSylogSender syslogserverip=x.x.x.x 
port=1468 eps=1000 userscount=15000 hostscount=15000 numberofevents=4000 cefinputfile="ad-pxy-dataset.cef" 

Note: It will generate 4000 events with 1000 events per sec (and will be terminated after sending specified 4000 numberofevents)

### Command to send events in real time:
java -cp syslog-0.0.1-SNAPSHOT.jar com.mc.eventsender.syslog.CEFEventSylogSender syslogserverip=x.x.x.x
port=1468 eps=1000 userscount=15000 hostscount=15000 cefinputfile="ad-pxy-dataset.cef" 

### Command to send historical events b/n dates:
java -cp syslog-0.0.1-SNAPSHOT.jar com.mc.eventsender.syslog.CEFEventSylogSender syslogserverip=x.x.x.x
port=1468 eps=1000 userscount=15000 hostscount=15000 starttime=2019-06-01/00:00:00 endtime=2019-06-01/23:59:59 
cefinputfile="ad-pxy-dataset.cef" 

### Command to Send events from historical date to till now:
java -cp syslog-0.0.1-SNAPSHOT.jar com.mc.eventsender.syslog.CEFEventSylogSender syslogserverip=x.x.x.x port=1999 eps=500 userscount=10000 hostscount=10000 
cefinputfile="ad-pxy-dataset.cef" eventsfromdays=30 forwardbyminutes=5
