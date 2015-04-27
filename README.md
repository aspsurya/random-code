What is the problem statement?

Create a real-time log stream monitoring and alerting system which can process continuous stream of log data, monitor it for user defined keywords and generate alerts when you detect the key words. 


What are the Use Cases?

Development of a scalable system for the below 4 use cases -
1.Real-time streaming log monitoring and collection from multiple applications
2.Real-time streaming log processing in online mode and generation of events/incidents based on rules/keywords
3.Querying on the real-time data for finding events/incidents, reporting and visualization
4.Real-time log alerting/notification through different modes (Email/SMS) 


Software and Hardware Details

Splunk Enterprise 6.2.x
Splunk Development Platform – SDK/APIs
Bootstrap 3.0
Jboss 7.1.1
JDK 7
Perl
Linux OS


What is our Solution?

Developed a scalable platform using the above softwares. we have used four different streaming log sources from accesslog,syslog,emaillog. Logs are being generated with the help of log simulators. Each of the streaming log source is being accessed/monitored by a forwarder or a monitor which reads the streaming logs using pull mechanism. Forwarders are created in Perl. The streaming data is ingested by respective indexers and stored in MongoDB. Here we have enabled a custom Dashboard for the users.

We have created a custom portal in Bootstrap which provides visualization and reporting capabilities for users. The custom Portal displays the total events, critical incidents with real-time graphs. Custom Portal is integrated using SDK/APIs and hosted on Jboss app server.  The solution is integrated with an Alert Engine which sends the alerts based on the configured keywords.
