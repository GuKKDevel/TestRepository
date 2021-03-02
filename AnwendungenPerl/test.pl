#!/usr/bin/perl -w
#
use strict;
use warnings;

use POSIX;

my $filenameOutput = undef;
my $fileNameStart  = undef;

$fileNameStart  = "/home/GuKKDevel/bug-1306/tmp/";
$filenameOutput = $fileNameStart . "index.temp.new";
my $actualDate = time();
my $actualDateS = strftime( "%Y-%m-%d", gmtime($actualDate) );

sub getFilename($$) {
	my $name     = $_[0];
	my $qualif   = $_[1];
	my $namepart = $name . $qualif;
	
	if ( -f $fileNameStart . $namepart . $actualDateS ) {
		while ( -f $fileNameStart . $namepart . $actualDateS ) {
			$namepart .= $qualif;
		}
	}

	return $namepart . $actualDateS;
}
print getFilename( "index.txt.", "kept." ) . "\n";
print getFilename( "index.txt.", "elim." ) . "\n";

#print("Renaming\n");
#print $filenameOutput. "\n";
#my $new = "new.";
#if ( -f $fileNameStart . "index." . $new . $actualDateS ) {
#	print(
#		"existiert	" . $fileNameStart . "index." . $new . $actualDateS . "\n" );
#}
#$new .= "new.";
#if ( -f $fileNameStart . "index." . $new . $actualDateS ) {
#	print(
#		"existiert	" . $fileNameStart . "index." . $new . $actualDateS . "\n" );
#}
#$new .= "new.";
#if ( -f $fileNameStart . "index." . $new . $actualDateS ) {
#	print(
#		"existiert	" . $fileNameStart . "index." . $new . $actualDateS . "\n" );
#}
#
#print("while-variante\n");
#$new = "new.";my $dw = 0;
#while ( -f $fileNameStart . "index." . $new . $actualDateS ) {
#
#	$dw++; print(
#		"$dw existiert	" . $fileNameStart . "index." . $new . $actualDateS . "\n" );
#	$new .= "new."
#}
#print ("abschliessendes new: $new\n");
#
#print("unless-variante\n");
#$new = "new.";my $du = 0;
#unless ( !-f $fileNameStart . "index." . $new . $actualDateS ) {
#
#	$du++; print(
#		"$du existiert	" . $fileNameStart . "index." . $new . $actualDateS . "\n" );
#	$new .= "new."
#}
#
#print ("abschliessendes new: $new\n");
#
#
#unless ( !-f $fileNameStart . "index." . $new . $actualDateS ) {
#	print $fileNameStart . "index." . $new . $actualDateS . "\n";
#	$new .= "new.";
#}
