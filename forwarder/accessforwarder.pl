#!/usr/bin/perl
use POSIX qw(strftime);

### OLD logs
# uplherc.upl.com - - [01/Aug/1995:00:00:07 -0400] "GET / HTTP/1.0" 304 0
# uplherc.upl.com - - [01/Aug/1995:00:00:08 -0400] "GET /images/ksclogo-medium.gif HTTP/1.0" 304 0
# uplherc.upl.com - - [01/Aug/1995:00:00:08 -0400] "GET /images/MOSAIC-logosmall.gif HTTP/1.0" 304 0

### NEW Logs 
# 166.45.20.12 - - [05/Jan/2015:14:47:42 +0530] "GET /uid_local/index.php/demologin HTTP/1.1" 200 18760
# 166.45.20.12 - - [05/Jan/2015:14:49:01 +0530] "GET /uid_local/index.php/demologin HTTP/1.1" 200 18881
# 166.45.20.12 - - [05/Jan/2015:14:49:01 +0530] "GET /branding/UIS/css/theme.css?v=1420449541 HTTP/1.1" 404 224

# Declare the IN File and OUT File 
my $inFile  = "/root/Surya/NASA_access_log_Aug95";
# my $outFile = "access.log";
my $outFile = "/var/hack/applogs/access.log";

# Open the OLD logs file -- NEW log file in append mode
open (INFH,$inFile) || die ("Unable to open the File: $inFile, $!");
# while ( $line = <INFH>)
while ( 1 )
{
  open (OUTFH,">>$outFile") || die "Couldn't open file $outFile, $!";
  chop($line = <INFH>);
  # my $line = "uplherc.upl.com - - [01/Aug/1995:00:00:07 -0400] \"GET / HTTP/1.0\" 304 0";
  my @data = split(/\[/, $line);
  my $server = $data[0];
  my @junk = split(/\]/, $data[1]);
  my $serverDate = $junk[0];
  my $status = $junk[1];
  
  # Get the current system date and time and format like access.log
  $datestring = strftime "%e/%b/%Y:%H:%M:%S", localtime;
  $datestring = $datestring . " +0530";
  # printf("date and time - $datestring\n");
  my $newLine = "$data[0]\[$datestring\]$status";
  # print "OLD LINE: $line\n";
  # print "NEW LINE: $newLine\n";

  print OUTFH "$newLine\n";
  close(OUTFH);
  sleep 5;
}

close(INFH);
exit 0;

