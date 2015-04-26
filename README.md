What is the problem statement?
Create a real-time log stream monitoring and alerting system which can process continuous stream of log data, monitor it for user defined keywords and generate alerts when you detect the key words. 

What are the Use Cases?
Development of a scalable system for the below 4 use cases -
a.Real-time streaming log monitoring and collection from multiple applications
b.Real-time streaming log processing in online mode and generation of events/incidents based on rules/keywords
c.Querying on the real-time data for finding events/incidents, reporting and visualization
d.Real-time log alerting/notification through different modes (Email/SMS) 

Software and Hardware Details
Splunk Enterprise 6.2.x
Splunk Development Platform – SDK/APIs
Bootstrap 3.0
Jboss 7.1.1
JDK 7
Perl
Linux OS

What is our Solution?
Developed a scalable platform using the above softwares. In this solution, we have used four different streaming log sources from accesslog,syslog,emaillog, seclog. Logs are being generated with the help of log simulators. Each of the streaming log sources is being accessed/monitored by a forwarder/monitor which reads the streaming logs using pull mechanism. Forwarders are created with Perl scripts. The streaming data is ingested by respective indexers and stored in MongoDB. We have created a custom Dashboard which provides search and reporting capabilities.

This platform is integrated with a Custom Portal developed using SDK/APIs, Bootstrap and hosted on Jboss app server. The custom Portal displays the total events, critical incidents with real-time graphs.
Also it is integrated with an Alert Engine which sends the alerts based on the configured keywords.

