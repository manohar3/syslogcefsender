package com.mc.eventsender.syslog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;
import com.google.common.net.InetAddresses;
import com.revinate.guava.util.concurrent.RateLimiter;

public class CEFEventSylogSender
{
	//messageSender.sendMessage(d + " su: 'su root' failed for lonvick on /dev/pts/8");
	//messageSender.sendMessage(d + " CEF:0|Microsoft|Microsoft Windows||Microsoft-Windows-Security-Auditing:6272|Network Policy Server granted access to a user.|Low| eventId=20 externalId=6272 vulnerabilityExternalID=FileReader[C:\\\\depot\\\\feature\\\\typhoon\\\\agent\\\\tmerzlyak\\\\test\\\\gate\\\\data\\\\agent\\\\windowsfg\\\\windows_2012\\\\security\\\\security.log] categorySignificance=/Informational categoryBehavior=/Authorization/Verify categoryDeviceGroup=/Operating System catdt=Operating System categoryOutcome=/Success categoryObject=/Host/Application/Service art=1589226636135 cat=Security deviceSeverity=Audit_success rt=1589226636135 suser=- suid=- dhost=N195-H085.Sigma_Test_2012.net.local src=164.99.135.69 dst=10.150.150.111 destinationZoneURI=/All Zones/ArcSight System/Private Address Space Zones/RFC1918: 10.0.0.0-10.255.255.255 dntdom=SIGMA_TEST_2012 dpt=579 duser=alice duid=Sigma_Test_2012.net.local/Users/Alice dpriv=Full Access cs1=Use Windows authentication for all users cs2=12552 cs3=15.214.195.18 cs5=PAP cs6=- c6a4=fe80:0:0:0:348c:bb0a:e8f5:4e8b cs1Label=Connection Request Policy Name cs2Label=EventlogCategory cs3Label=RADIUS Client IP Address cs4Label=Reason or Error Code cs5Label=Authentication Type cs6Label=Account Session Identifier cn1Label=LogonType cn2Label=CrashOnAuditFail cn3Label=Count c6a4Label=Agent IPv6 Address ahost=laptop-es6h12fn agt=164.99.208.195 agentZoneURI=/All Zones/ArcSight System/Public Address Space Zones/Hewlett-Packard Company amac=00-05-9A-3C-7A-00 av=7.2.1.0.0 atz=America/Los_Angeles at=wuc dvchost=N195-H085.Sigma_Test_2012.net.local dtz=America/Los_Angeles _cefVer=0.1 ad.AuthenticatioufMKyQ_~_~ation_,Provider=Windows ad.NAS:NAS_,Port-Type=Virtual ad.WindowsKeyMapFamily=Windows 2012 ad.RADIUS_,Client:Client_,Friendly_,Name=Cisco ad.EventIndex=1062484 ad.Client_,Machine:Security_,ID=S-1-0-0 ad.NAS:NAS_,IPv6_,Address=- ad.WindowsParserFamily=Windows 2012|8 ad.Client_,Machine:OS-Version=- ad.AuthenticatioFbGqIw_~_~ogging_,Results=Accounting information was written to the local log file. ad.NAS:NAS_,Identifier=- ad.Authentication_,Details:EAP_,Type=- ad.AuthenticatioC+-ZJA_~_~ication_,Server=N195-H085.Sigma_Test_2012.net.local ad.Client_,MachinMPhIjg_~_~ion_,Identifier=- ad.Quarantine_,In84mHVQ_~_~ion_,Identifier=- ad.Authenticatiox0bFFg_~_~rk_,Policy_,Name=1 ad.User:Security_,ID=S-1-5-21-2536335849-3061521316-1825165326-1105 ad.WindowsVersion=Windows Server 2012 aid=3Xmf6AnIBABCAA54sCgFp5Q\\=\\=\r\n");

	public static List<String> readcefevents(String ceffile){
		List<String> events = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			String cefevt;
			reader = new BufferedReader(new FileReader(ceffile));
			while ((cefevt = reader.readLine()) != null) {
				events.add(cefevt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return events;
	}


	public static void usage() {
   		System.out.println("syslogserverip=<ip> port=<port> eps=<eps> userscount=<userscount> hostscount=<hostscount> numberofevents=<numberofevents> eventsfromdays=<days> "
   				+ "forwardbyminutes=<minutes> starttime=<yyyy-MM-dd HH:mm:ss> endtime=<yyyy-MM-dd HH:mm:ss> cefinputfile=<ceffilename> ");
   		System.exit(0);
	}

    public static void main( String[] args ) throws Exception
    {
    	String syslogserverip = null;
    	int port = 0;
    	int eps = 0;
    	int userscount = 0;
    	int hostscount = 0;
    	long numberofevents = Long.MAX_VALUE;
    	String cefinputfile = null;
    	boolean replaceusers = true;
    	boolean replacehosts = true;
    	int eventsfromdays = 0;
    	int forwardbyminutes = 5;//default five minutes
    	long evtStarttime = 0;
    	long evtEndtime = 0;
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    	String mode = "realtime";


    	double d = 100.992567;
    	System.out.println((float)d);

    	for(String arg:args) {
    		String [] input=arg.split("=");
    		String key = input[0];
    		String value = input[1];

    		if(key.equals("syslogserverip")) {
    			syslogserverip = value;
    		}
    		else if(key.equals("port")) {
    			port = Integer.parseInt(value);
    		}
    		else if(key.equals("eps")) {
    			eps = Integer.parseInt(value);
    		}
    		else if(key.equals("userscount")) {
    			userscount = Integer.parseInt(value);
    		}
    		else if(key.equals("hostscount")) {
    			hostscount = Integer.parseInt(value);
    		}
    		else if(key.equals("numberofevents")) {
    			numberofevents = Long.parseLong(value);
    		}
    		else if(key.equals("cefinputfile")) {
    			cefinputfile = value;
    		}
    		else if(key.equals("replaceusers")) {
    			replaceusers = Boolean.parseBoolean(value);
    		}
    		else if(key.equals("replacehosts")) {
    			replacehosts = Boolean.parseBoolean(value);
    		}
    		else if(key.equals("eventsfromdays")) {
    			eventsfromdays = Integer.parseInt(value);
    			mode = "historical";
    		}
    		else if(key.equals("forwardbyminutes")) {
    			forwardbyminutes = Integer.parseInt(value);
    		}
    		else if(key.equals("starttime")){
    			dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
    			evtStarttime = dateformat.parse(value).getTime();
    			mode = "daterange";
    		}
    		else if(key.equals("endtime")) {
    			dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
    			evtEndtime = dateformat.parse(value).getTime();
    			mode = "daterange";
    		}
    	}

    	//validate
    	if(syslogserverip == null) {
    		System.out.println("Provide Syslog server ip");
    		usage();
    	}
    	if(port == 0) {
    		System.out.println("Provide Syslog server port");
    		usage();
    	}
    	if(eps == 0) {
    		System.out.println("Provide eps");
    		usage();
    	}
    	if(replaceusers && userscount == 0) {
    		System.out.println("Provide number of users to generate");
    		usage();
    	}
    	if(replaceusers && hostscount == 0) {
    		System.out.println("Provide number of hosts ip to generate");
    		usage();
    	}

    	Date eventTime = new Date();
    	//generate data from last days
    	if(mode.equals("historical")) {
    		eventTime = new Date(System.currentTimeMillis() - (eventsfromdays * 24 * 60 * 60 * 1000l));
    		System.out.println("Generating events from " + eventTime);
    	}
    	//generate data for specific days
    	else if(mode.equals("daterange")){
    		eventTime = new Date(evtStarttime);
    		System.out.println("Generating events from " + dateformat.format(eventTime) +"GMT");
    	}
    	else {
    		System.out.println("Generating events from " + eventTime);
    	}

    	List<String> events = readcefevents(cefinputfile);
    	Random random = new Random();
    	TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
    	RateLimiter limiter = RateLimiter.create(eps);
    	long i = 0;
    	long metricStartTime = System.currentTimeMillis();
    	while(i < numberofevents) {
    		if(mode.equals("daterange") && eventTime.getTime() >= evtEndtime) {
    			System.out.println("Total event sent " + i);
    			break;
    		}
    		if(mode.equals("historical") && eventTime.getTime() > System.currentTimeMillis()) {
    			System.out.println("Total event sent " + i);
    			break;
    		}

    		messageSender.setDefaultMessageHostname("myhostname");
    		messageSender.setDefaultAppName("myapp");
    		messageSender.setDefaultFacility(Facility.USER);
    		messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
    		messageSender.setSyslogServerHostname(syslogserverip);
    		messageSender.setSyslogServerPort(port);
    		messageSender.setMessageFormat(com.cloudbees.syslog.MessageFormat.RFC_3164); // optional, default is RFC 3164
    		messageSender.setSsl(false);

    		for(String event:events) {
		        if(i > numberofevents){
				break;
			}
			limiter.acquire();
    			i++;
    			int randomIpIndex = random.nextInt(hostscount);
        		String randomIp = InetAddresses.fromInteger(randomIpIndex + 168427520).getHostAddress();//generates ip in range of 10.10.x.x
        		int randomUserIndex = random.nextInt(userscount);
        		String randonUserName = "username-"+randomUserIndex;
        		String randomHostname = "host-"+ randomIpIndex;
        		// use current time only real time events, for historical it will fast forwarded for every 10000 events
        		if(mode.equals("realtime")) {
        			eventTime = new Date();
        			if(i % 500 == 0) {
        				long totaltimeInSeconds = (System.currentTimeMillis() - metricStartTime)/1000;
        		//		System.out.println("EPS is:"+ 50000/totaltimeInSeconds);
        				System.out.println("Sent total "+ i + " events ");
        				metricStartTime = System.currentTimeMillis();
        			}
        		}
        		else if(mode.equals("historical")) {
        			if(i % 500 == 0 ) {
        				long totaltimeInSeconds = (System.currentTimeMillis() - metricStartTime)/1000;
        				//System.out.println("EPS is:"+ 50/totaltimeInSeconds);
        				System.out.println("Sent total "+ i + " events ");
        				eventTime.setTime(eventTime.getTime() + forwardbyminutes * 60 * 1000);
        				System.out.println("Fast forward by " + forwardbyminutes +" minutes. Now Generating data for time " + eventTime);
        				if(eventTime.getTime() > System.currentTimeMillis()) {
        					System.out.println("Generated all historical data..switching to realtime event generation....");
        					eventsfromdays = 0;
        				}
        				metricStartTime = System.currentTimeMillis();
        			}
        		}
        		else if(mode.equals("daterange")) {
        			float totaltimeInSeconds = (System.currentTimeMillis() - metricStartTime);
        			if(totaltimeInSeconds >= 1000 ) {
        				eventTime = new Date(eventTime.getTime() + 1000);
        				metricStartTime = System.currentTimeMillis();
        				System.out.println(i + " " + dateformat.format(eventTime) + " " + eventTime.getTime());
            			if(i % 50000 == 0 ) {
            				System.out.println("Sent total " + i + " events till time " + dateformat.format(eventTime));
            			}
        			}
        			if(i % 50000 == 0 ) {
        				System.out.println("Sent total " + i + " events till time " + dateformat.format(eventTime));
        			}
        		}
    			if(replacehosts) {
    				event = event.replaceAll("src=([\\S]+)", "src="+randomIp);
    				event = event.replaceAll("dst=([\\S]+)", "dst="+randomIp);
    				event = event.replaceAll("dhost=([\\S]+)", "dhost="+randomHostname);
    				event = event.replaceAll("shost=([\\S]+)", "shost="+randomHostname);
    			}
    			if(replaceusers) {
    				event = event.replaceAll("duser=([\\S]+)", "duser="+randonUserName);
    				event = event.replaceAll("suser=([\\S]+)", "suser="+randonUserName);
    			}
   				event = event.replaceAll("rt=([\\S]+)", "rt="+ eventTime.getTime());
   				event = event.replaceAll("art=([\\S]+)", "art="+eventTime.getTime());
    			messageSender.sendMessage(event);
    		}
    	}
    }
}
