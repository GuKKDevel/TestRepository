#!/usr/bin/perl -w

use strict;
use warnings;

use POSIX;
use open qw< :encoding(UTF-8) >;

#
# Definition of variables used more than once
#
my $temp      = "undef";
my $workdir   = undef;
my $workfile  = undef;
my $gitqual   = undef;
my @reposname = ( "GuKKDevel", "github", "CAcert" );
my @reposdir  = (
	"/home/gitRepos/CAcert/CAcert-devel",
	"/home/gitRepos/temp/github", "/home/gitRepos/temp/cacert"
);
my @branches = undef;
my $branchNr = 0;
my $branch   = "";

# returncode for systemcalls
my $rc = 0;

#
#temporary directory for program-output
#
#getting actual date
my $actualDate = time();

#my $actualDateS = strftime( "%Y-%m-%dT%H-%M-%S", gmtime($actualDate) );
my $actualDateS = "";
my $outputqual  = "/home/GuKKDevel/TestFiles/";
my $tmpqual     = "diffAnalyse" . $actualDateS;
my $tmpdir      = $outputqual . $tmpqual;
my $actdir      = undef;
#
# subroutines for
#
# logging
# pre-processing
# post-processing
#
my $logstmt = undef;
my $openLOG = 1;

sub logging ($) {
	return if ( not defined( $_[0] ) );
	my $timestamp = strftime( "%Y-%m-%d %H:%M:%S", gmtime );
	if ($openLOG) {

		open LOG, ">$outputqual" . "logfile-$tmpqual.txt"
		  or die("$0: Couldn't open LOG: $!");
		$openLOG = 0;
	}
	print LOG "$timestamp $_[0]";

	flush LOG;
	print "$timestamp $_[0]";
}

sub preprocessing () {
	logging("Start Pre-processing\n");
	$actdir = getcwd;
	logging("Aktueller Pfad: $actdir\n");

	$rc      = mkdir($tmpdir);
	$logstmt = "mkdir $tmpdir";
	$logstmt .= " -> ";
	$logstmt .= "nicht " if ( $rc == 0 );
	$logstmt .= ("erfolgreich\n");
	logging($logstmt);

}

sub postprocessing () {
	logging("\n");
	logging("Start Post-processing\n");
	$rc      = chdir($actdir);
	$logstmt = "chdir $actdir";
	$logstmt .= " -> ";
	$logstmt .= "nicht " if ( $rc == 0 );
	$logstmt .= ("erfolgreich\n");
	logging($logstmt);

	#	$rc = system("rm $tmpdir/* ");
	#	print("rm $tmpdir/*: $rc\n");
	#	$rc = rmdir("$tmpdir");
	#	print("rmdir $tmpdir: $rc\n");
}

#
# Subroutines
#
sub branchAnalyse ($) {
	my $repo = $_;

}

#
# Start of main structure
#

preprocessing();

for ( my $repo = 0 ; $repo < 3 ; $repo++ ) {
	$branch   = "";
	$branchNr = 0;
	
	logging("\n");
	$workdir = $reposdir[$repo];
	$rc      = chdir("$workdir");
	$logstmt = "chdir $workdir";
	$logstmt .= " -> ";
	$logstmt .= "nicht " if ( $rc == 0 );
	$logstmt .= ("erfolgreich\n");
	logging($logstmt);

	$workfile = $tmpdir . "/$reposname[$repo]";
	$temp     = system("git branch --list --all > $workfile");
	$logstmt  = "git branch --list --all > $workfile";
	$logstmt .= " -> ";
	$logstmt .= "nicht " if ( $rc == 0 );
	$logstmt .= ("erfolgreich\n");
	logging($logstmt);

	open BRANCHFILE, "< $workfile"
	  or die("$0: couldn't open $workfile: $!");
	while (<BRANCHFILE>) {
		$branch = $_;
		$branch =~ s/^\s+//;
		$branch =~ s/\s+$//;
		$branches[$repo][$branchNr] = $branch;
		print $branches [$repo][$branchNr] . "\n";
		$branchNr++;
		if ( $branchNr > 100 ) { last }
	}

}

#postprocessing();
