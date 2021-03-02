#!/usr/bin/perl -w
#
use strict;
use warnings;

use POSIX;
use open qw< :encoding(UTF-8) >;

my $filenameInput = undef;
my $filenameOutput  = undef;

$filenameInput  = "/home/gitRepos/TestRepository/CAcert/bug-1443-Website-document-current-code/DIR-www-utf8_to_ascii";
$filenameOutput = "/home/gitRepos/TestRepository/CAcert/bug-1443-Website-document-current-code/DIR-www-utf8_to_ascii.rst";

my $line0 = "\n";
my $line1part1 =".. _www-utf8_to_ascii-";
my $line1part2 ="\:\n";
my $line2part1 =".. sourcefile: www/utf8_to_ascii/";
my $line2part2 ="\n";
my $line3part1 ="- :file:\`";
my $line3part2 ="\`\n";

my $fileInput = undef;
open( $fileInput, "<", "$filenameInput" )
  || die "$0: can't open $filenameInput for reading: $!";

my $fileOutput = undef;
open( $fileOutput, ">", "$filenameOutput" )
  || die "$0: can't open $filenameOutput for writing: $!";  
  
  
my $string = undef;  
while (<$fileInput>) {
	my $zeile = $_; 
	my $name = substr($zeile, 0, length($zeile)-1); 
	print $fileOutput ($line1part1. $name. $line1part2);
	print $fileOutput ($line0);
	print $fileOutput ($line2part1. $name. $line2part2);
	print $fileOutput ($line0);
	print $fileOutput ($line3part1. $name. $line3part2);
	print $fileOutput ($line0);
}
	
close $fileOutput;
close $fileInput;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	